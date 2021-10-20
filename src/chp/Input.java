package chp;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Input {
	private final String s;
	
	/**
	 * The k strings t_1..t_k.
	 */
	private final List<String> unexpandedSubstrings;
	
	private final Map<Character, List<String>> subsets;
	
	private Input(String s, List<String> unexpandedSubstrings, Map<Character, List<String>> subsets) {
		this.s = s;
		this.unexpandedSubstrings = unexpandedSubstrings;
		this.subsets = Collections.unmodifiableMap(subsets);
	}
	
	public static Input buildFromInput(Collection<String> inputLines) throws IncorrectInputException {
		var lineIterator = inputLines.iterator();
		int k;
		try {
			k = Integer.parseInt(lineIterator.next());
		} catch (NumberFormatException nfe) {
			throw new IncorrectInputException("First input not an integer.");
		}
		
		var s = lineIterator.next();
		if (!Main.SIGMA.isInLanguage(s)) {
			throw new IncorrectInputException("Second input not in SIGMA* language.");
		}
		
		var unexpandedSubstrings = new ArrayList<String>(); 
		var joinedAlphabet = Main.SIGMA.join(Main.GAMMA);
		for (var i = 0; i < k; ++i) {
			var unexpandedSubstring = lineIterator.next();
			if (!joinedAlphabet.isInLanguage(unexpandedSubstring)) {
				throw new IncorrectInputException("Unexpanded substring with index " + i + " not in the joined language.");
			}
			unexpandedSubstrings.add(unexpandedSubstring);
		}
		
		var subsets = new HashMap<Character, List<String>>();
		while (lineIterator.hasNext()) {
			var line = lineIterator.next();
			var split = line.split(":");
			if (split.length != 2) {
				throw new IncorrectInputException("Subset line must have exactly one ':'.");
			}
			if (split[0].length() != 1) {
				throw new IncorrectInputException("Subset line must have exactly one character before the ':'.");
			}
			var character = split[0].charAt(0);
			if (!Main.GAMMA.isInAlphabet(character)) {
				throw new IncorrectInputException("Subset line first character must be in alphabet GAMMA.");
			}
			var words = split[1].split(",");
			for (var word : words) {
				if (!joinedAlphabet.isInLanguage(word)) {
					throw new IncorrectInputException("Subset elements must be in the joined language.");
				}
			}
			subsets.put(character, Arrays.asList(words));
		}
		
		return new Input(s, unexpandedSubstrings, subsets);
	}

	public static Input buildFromReader(BufferedReader reader) throws IncorrectInputException {
		var lines = reader.lines().collect(Collectors.toList());
		return Input.buildFromInput(lines);
	}
	
	public String getS() {
		return this.s;
	}
	public List<String> getUnexpandedSubstrings() {
		return this.unexpandedSubstrings;
	}
	public Map<Character, List<String>> getSubsets() {
		return subsets;
	}
	
	/**
	 * Performs various optimizations on the input to reduce the work needed to find the solution and sometimes fail early.
	 * @return The optimized input or an empty optional if no solution is possible.
	 */
	public Optional<Input> optimize() {
		var optimizedUnexpandedSubstrings = unexpandedSubstrings;
		var optimizedSubsets = subsets;
		
		// Find the symbols in the GAMMA alphabet actually used in the unexpanded substrings.
		var usedGammaSymbols = new HashSet<Character>();
		for (var s : optimizedUnexpandedSubstrings) {
			Collection<Character> sGammaSymbols = s.chars().mapToObj(c->(char)c)
					.filter(c -> Main.GAMMA.isInAlphabet(c))
					.collect(Collectors.toList());
			usedGammaSymbols.addAll(sGammaSymbols);
		}
		{
			// Remove expansion lists for symbols that aren't in any of the unexpanded substrings.
			var subsets = new HashMap<Character, List<String>>();
			for (var entry : this.subsets.entrySet()) {
				if (usedGammaSymbols.contains(entry.getKey()))
					subsets.put(entry.getKey(), entry.getValue());
			}
			optimizedSubsets = subsets;
		}
		
		var optimizedInput = new Input(s, optimizedUnexpandedSubstrings, optimizedSubsets);
		var curTotalSubsets = totalSubsets(optimizedInput.getSubsets());
		var prevTotalSubsets = -1;
		
		do {
			var subsetOptimizedOption = optimizedInput.optimizeSubsets();
			if (subsetOptimizedOption.isEmpty())
				return Optional.empty();
			
			optimizedInput = subsetOptimizedOption.get().optimizeUnexpandedSubstrings();
			prevTotalSubsets = curTotalSubsets;
			curTotalSubsets = totalSubsets(optimizedInput.getSubsets());
		} while (curTotalSubsets < prevTotalSubsets);

		
		return Optional.of(optimizedInput);
	}
	
	private Optional<Input> optimizeSubsets() {
		// GAMMA symbols in unexpandedSubstrings surrounded by SIGMA symbols may have some expansions removed.
		var optimizedSubsets = new HashMap<Character,List<String>>();
		for (var entry : subsets.entrySet()) {
			var expandableStrings = new HashSet<String>();
			var string = new StringBuilder();
			for (var unexpandedSubstring : unexpandedSubstrings) {
				unexpandedSubstring.chars().forEach(cInt -> {
					var c = (char)cInt;
					if (c == entry.getKey() || Main.SIGMA.isInAlphabet(c))
						string.append(c);
					else if (string.length() > 0) {
						expandableStrings.add(string.toString());
						string.setLength(0);
					}
				});
				string.setLength(0);
			}
			var optimizedExpansions = entry.getValue().stream()
					.filter(expansion -> expandableStrings.stream()
							.map(s -> s.replace("" + entry.getKey(), expansion))
							.allMatch(s -> this.s.contains(s)))
					.collect(Collectors.toList());
			if (optimizedExpansions.size() == 0)
				return Optional.empty();
			optimizedSubsets.put(entry.getKey(), optimizedExpansions);
		}
		
		return Optional.of(new Input(s, unexpandedSubstrings, optimizedSubsets));
	}
	
	private Input optimizeUnexpandedSubstrings() {
		// Expand GAMMA symbols that only have one possible expansion.
		var optimizedUnexpandedSubstrings = new ArrayList<String>();
		for (var unexpandedSubstring : unexpandedSubstrings) {
			for (var entry : subsets.entrySet()) {
				if (entry.getValue().size() == 1) {
					unexpandedSubstring = unexpandedSubstring.replaceAll("" + entry.getKey(), entry.getValue().get(0));
				}
			}
			optimizedUnexpandedSubstrings.add(unexpandedSubstring);
		}
		return new Input(s, optimizedUnexpandedSubstrings, subsets);
	}
	
	private static int totalSubsets(Map<Character, List<String>> subsets) {
		var result = 0;
		for (var subset : subsets.values()) {
			result +=subset.size();
		}
		return result;
	}
	
	public Map<Character, String> chooseSubsets(int[] expansionIndices) {
		var result = new HashMap<Character, String>();
		for (var entry : subsets.entrySet()) {
			result.put(entry.getKey(), entry.getValue().get(expansionIndices[Main.charToIndex(entry.getKey())]));
		}
		return result;
	}
	public String expand(String s, Map<Character, String> expansions) {
		var result = new StringBuilder();
		for (var c : s.toCharArray()) {
			if (Main.SIGMA.isInAlphabet(c)) {
				result.append(c);
			} else if (Main.GAMMA.isInAlphabet(c)) {
				result.append(expansions.get(c));
			}
		}
		return result.toString();
	}
	public String expand(String s, int[] expansionIndices) {
		var result = new StringBuilder();
		for (var c : s.toCharArray()) {
			if (Main.SIGMA.isInAlphabet(c)) {
				result.append(c);
			} else if (Main.GAMMA.isInAlphabet(c)) {
				result.append(subsets.get(c).get(expansionIndices[Main.charToIndex(c)]));
			}
		}
		return result.toString();
	}
}
