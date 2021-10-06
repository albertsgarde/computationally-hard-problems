package chp;

public class IncorrectInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IncorrectInputException(String errorMessage) {
		super(errorMessage);
	}
}
