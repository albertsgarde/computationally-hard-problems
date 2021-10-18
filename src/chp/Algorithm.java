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
				.map(symbol -> input.getSubsets().get(Main.GAMMA.get(symbol)).size())
				.collect(Collectors.toList());
		var expandIndices = new int[Main.GAMMA.size()];
		Arrays.fill(expandIndices, 0);
		while (iterateIndices(expandIndices, maxIndices)) {
			var expansionMap = input.chooseSubsets(expandIndices);
			
			if (input.getUnexpandedSubstrings().stream()
				.allMatch(s -> input.getS().contains(input.expand(s, expansionMap))))
				return Optional.of(expansionMap);
				
		}
		return Optional.empty();
	}
}
