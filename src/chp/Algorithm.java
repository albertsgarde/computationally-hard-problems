package chp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Algorithm {
	
	private static boolean iterateIndices(int[] indices, List<Integer> maxIndices) {
		for (var i = 0; i < indices.length; ++i) {
			++indices[i];
			if (indices[i] >= maxIndices.get(i)) {
				indices[i] = 0;
			} else {
				return true;
			}
		}
		return false;
	}
	
	public static Optional<Map<Character, String>> basic_solve(Input input) {
		List<Integer> maxIndices = Main.GAMMA.symbols().stream()
				.map(symbol -> {
					if (input.getSubsets().containsKey(symbol))
						return input.getSubsets().get(symbol).size();
					else 
						return 0;
				})
				.collect(Collectors.toList());
		var expandIndices = new int[maxIndices.size()];
		Arrays.fill(expandIndices, 0);
		do {
			var expansionMap = input.chooseSubsets(expandIndices);
			
			if (input.getUnexpandedSubstrings().stream()
				.allMatch(s -> input.getS().contains(input.expand(s, expansionMap))))
				return Optional.of(expansionMap);
				
		} while (iterateIndices(expandIndices, maxIndices));
		return Optional.empty();
	}
}
