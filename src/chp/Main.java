package chp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Main {
	
	public static final boolean RUN_TESTS = true;
	
	public static final Alphabet SIGMA = new Alphabet("abcdefghijklmnopqrstuvwxyz");
	public static final Alphabet GAMMA = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	
	

	public static void main(String[] args) {
		if (RUN_TESTS) {
			Tests.test_input_build_example();
			System.out.println("Test 'input_build_example' successful");
		}
		
		
		var reader = new BufferedReader(new InputStreamReader(System.in));
		
		var lines = reader.lines().collect(Collectors.toList());
		try {
			var input = Input.buildFromInput(lines);
		} catch (IncorrectInputException e) {
			System.out.println("NO");
			return;
		}
		System.out.println("YES");
		
	}

}
