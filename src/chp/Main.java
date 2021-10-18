package chp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
	
	public static final boolean RUN_TESTS = true;
	
	public static final Alphabet SIGMA = new Alphabet("abcdefghijklmnopqrstuvwxyz");
	public static final Alphabet GAMMA = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	
	public static int charToIndex(char c) {
		if (c>=65 && c <= 90) 
			return c-65;
		else if (c >= 97 && c <= 122) 
			return c-97;
		else
			throw new IllegalArgumentException("c must be a letter.");
	}
	
	

	public static void main(String[] args) {
		if (RUN_TESTS) {
			Tests.test_input_build_example();
			System.out.println("Test 'input_build_example' successful");
			Tests.test_expansion();
			System.out.println("Test 'expansion' successful");
			Tests.test_correctness_basic_yes();
			System.out.println("Test 'correctness_basic_yes' successful");
			Tests.test_correctness_basic_no();
			System.out.println("Test 'correctness_basic_no' successful");
			Tests.test_correctness_input_optimized_yes();
			System.out.println("Test 'correctness_input_optimized_yes' successful");
			Tests.test_correctness_input_optimized_no();
			System.out.println("Test 'correctness_input_optimized_no' successful");
			Tests.test_speed_optimized_all();
			System.out.println("Test 'speed_optimized_all' successful");
		}
		
		
		var reader = new BufferedReader(new InputStreamReader(System.in));
		
		Input input;
		try {
			input = Input.buildFromReader(reader);
		} catch (IncorrectInputException e) {
			System.out.println("NO");
			return;
		}
		var optimizedInputOption = input.optimize();
		if (optimizedInputOption.isEmpty()) {
			System.out.println("NO");
			return;
		}
		input = optimizedInputOption.get();
		var solution_option = Algorithm.basic_solve(input);
		if (solution_option.isEmpty()) {
			System.out.println("NO");
			return;
		}
		var solution = solution_option.get();
		for (var entry : solution.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		
	}

}
