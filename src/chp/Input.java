package chp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public String getS() {
		return this.s;
	}
	public List<String> getUnexpandedSubstrings() {
		return this.unexpandedSubstrings;
	}
	public Map<Character, List<String>> getSubsets() {
		return this.subsets;
	}
}
