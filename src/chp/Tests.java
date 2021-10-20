package chp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Tests {
	private static BufferedReader testReader(int file) {
		try {
			return new BufferedReader(new FileReader("test_data\\test0" + file + ".swe"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			throw new RuntimeException("Could not read file.");
		}
	}
	
	public static void test_input_build_example() {
		var reader = testReader(1);
		try {
			var input = Input.buildFromReader(reader);
			if (!input.getS().equals("abdde")) 
				throw new RuntimeException("String S loaded incorrectly. Value: " + input.getS());
			if (input.getUnexpandedSubstrings().size() != 4)
				throw new RuntimeException("Wrong number of unexpanded substrings found. Found " + input.getUnexpandedSubstrings().size());
			if (!input.getUnexpandedSubstrings().get(0).equals("ABD"))
				throw new RuntimeException("First unexpanded substring loaded wrongly. Value: " + input.getUnexpandedSubstrings().get(0));
			if (input.getSubsets().size() != 5)
				throw new RuntimeException("Wrong number of subsets found. Found " + input.getSubsets().size());
			if (!input.getSubsets().containsKey('A'))
				throw new RuntimeException("No subset for character A found.");
			var expectedASubset = Arrays.stream(new String[] {"a", "b", "c", "d", "e", "f", "dd"}).collect(Collectors.toList());
			if (!input.getSubsets().get('A').containsAll(expectedASubset))
				throw new RuntimeException("Incorrect subset found for A. Subset: " + input.getSubsets().get('A'));
		} catch (IncorrectInputException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to validate correct input.");
		}
	}
	
	public static void test_expansion() {
		var reader = testReader(1);
		Input input;
		try {
			input = Input.buildFromReader(reader);
		} catch (IncorrectInputException e) {
			throw new RuntimeException("Test failed.");
		}
		var expansionMap = input.chooseSubsets(new int[] {3,6,0,1,1});
		{
			var expansion = input.expand("BCE", expansionMap);
			if(!expansion.equals("ddabd"))
				throw new RuntimeException("Incorrect expansion. Got " + expansion);
		}
	}
	
	public static void test_correctness_basic_yes() {
		var lines = new String[] {
	 		"2",
	 		"aaa",
	 		"ABD",
	 		"ABa",
			"A:a",
			"B:a",
			"C:a",
			"D:a"
		};
		Input input;
		try {
			input = Input.buildFromInput(Arrays.stream(lines).collect(Collectors.toList()));
		} catch (IncorrectInputException e) {
			throw new RuntimeException("Test failed.");
		}
		var solution_option = Algorithm.basic_solve(input);
		if (solution_option.isEmpty()) {
			throw new RuntimeException("Returned NO incorrectly.");
		}
		var solution = solution_option.get();
		if (!(solution.get('A').equals("a") && solution.get('B').equals("a") && solution.get('D').equals("a"))) {
			System.out.println("Incorrect solution:");
			for (var entry : solution.entrySet()) 
				System.out.println(entry.getKey() + ": " + entry.getValue());
			throw new RuntimeException("Incorrect solution.");
		}
	}
	
	public static void test_correctness_basic_no() {
		var reader = testReader(1);
		Input input;
		try {
			input = Input.buildFromReader(reader);
		} catch (IncorrectInputException e) {
			throw new RuntimeException("Test failed.");
		}
		var solution_option = Algorithm.basic_solve(input);
		if (solution_option.isPresent()) {
			throw new RuntimeException("Return YES incorrectly.");
		}
	}
	
	public static void test_correctness_input_optimized_yes() {
		var lines = new String[] {
	 		"2",
	 		"aaa",
	 		"ABD",
	 		"ABa",
			"A:a",
			"B:a",
			"C:a",
			"D:a"
		};
		Input input;
		try {
			input = Input.buildFromInput(Arrays.stream(lines).collect(Collectors.toList()));
		} catch (IncorrectInputException e) {
			throw new RuntimeException("Test failed.");
		}
		var optimizedInputOption = input.optimize();
		if (optimizedInputOption.isEmpty()) {
			throw new RuntimeException("Returned NO incorrectly.");
		}
		input = optimizedInputOption.get();
		var solution_option = Algorithm.basic_solve(input);
		if (solution_option.isEmpty()) {
			throw new RuntimeException("Returned NO incorrectly.");
		}
		var solution = solution_option.get();
		if (!(solution.get('A').equals("a") && solution.get('B').equals("a") && solution.get('D').equals("a"))) {
			System.out.println("Incorrect solution:");
			for (var entry : solution.entrySet()) 
				System.out.println(entry.getKey() + ": " + entry.getValue());
			throw new RuntimeException("Incorrect solution.");
		}
	}
	
	public static void test_correctness_input_optimized_no() {
		var reader = testReader(1);
		Input input;
		try {
			input = Input.buildFromReader(reader);
		} catch (IncorrectInputException e) {
			throw new RuntimeException("Test failed.");
		}
		var optimizedInputOption = input.optimize();
		if (optimizedInputOption.isEmpty()) {
			throw new RuntimeException("Returned NO incorrectly.");
		}
		input = optimizedInputOption.get();
		var solution_option = Algorithm.basic_solve(input);
		if (solution_option.isPresent()) {
			throw new RuntimeException("Return YES incorrectly.");
		}
	}
	
	public static void test_speed_optimized_all() {
		var correctResults = new boolean[] {false, true, false, true, false, true};
		for (var i = 1; i <= 6; ++i) {
			var reader = testReader(i);
			Input input;
			try {
				input = Input.buildFromReader(reader);
			} catch (IncorrectInputException e) {
				throw new RuntimeException("Test failed.");
			}
			var optimizedInputOption = input.optimize();
			if (optimizedInputOption.isEmpty()) {
				if (correctResults[i-1]) 
					throw new RuntimeException("Returned NO incorrectly.");
				System.out.println(i + ": NO. Optimization");
				continue;
			}
			input = optimizedInputOption.get();
			var solution_option = Algorithm.basic_solve(input);
			if (solution_option.isPresent()) {
				if (!correctResults[i-1]) 
					throw new RuntimeException("Returned YES incorrectly.");
				System.out.println(i + ": YES");
			} else {
				if (correctResults[i-1]) 
					throw new RuntimeException("Returned NO incorrectly.");
				System.out.println(i + ": NO. Algorithm");
			}
		}
		
	}
}
