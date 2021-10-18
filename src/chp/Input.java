package chp;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		this.subsets = subsets;
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
		return Collections.unmodifiableMap(this.subsets);
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
