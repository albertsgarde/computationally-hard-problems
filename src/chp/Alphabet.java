package chp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Alphabet implements Iterable<Character> {
	private final List<Character> symbols;
	
	public Alphabet(Collection<Character> symbols) {
		this.symbols = new ArrayList<Character>(symbols);
	}
	
	public Alphabet(String symbols) {
		this.symbols = symbols.chars().mapToObj(e->(char)e).collect(Collectors.toList());
	}
	
	public Alphabet join(Alphabet alphabet) {
		List<Character> newAlphabetList = Stream.of(this.symbols, alphabet.symbols)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
		return new Alphabet(newAlphabetList);
	}
	
	public boolean isInAlphabet(char c) {
		return this.symbols.contains(c);
	}
	
	public boolean isInLanguage(String s) {
		// Perform some black magic to get a list of chars in the input string.
		var charList = s.chars().mapToObj(e->(char)e).collect(Collectors.toList());
		return this.symbols.containsAll(charList);
	}
	
	public Character get(int index) {
		return symbols.get(index);
	}
	
	public int size() {
		return symbols.size();
	}

	@Override
	public Iterator<Character> iterator() {
		return symbols.iterator();
	}
}
