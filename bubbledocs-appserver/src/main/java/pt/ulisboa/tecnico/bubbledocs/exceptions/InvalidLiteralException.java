package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidLiteralException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _lit;

	public InvalidLiteralException(String lit) {
		this._lit = lit;
	}

	public String getInvalidLiteral() {
		return _lit;
	}
	
	@Override
	public String getMessage() {
		return "Literal: " + this._lit + " is invalid";
	}

}
