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
		}
		
		
		var reader = new BufferedReader(new InputStreamReader(System.in));
		
		Input input;
		try {
			input = Input.buildFromReader(reader);
		} catch (IncorrectInputException e) {
			System.out.println("NO");
			return;
		}
		System.out.println("YES");
		
	}

}
