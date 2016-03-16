package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class BubbleDividedByZeroException extends BubbleException {

	private static final long serialVersionUID = 1L;

	public BubbleDividedByZeroException() {}
	
	@Override
	public String getMessage() {
		return "Can't be divided by Zero";
	}

}
