package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidContentException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _cont;
	
	public InvalidContentException() {}

	public InvalidContentException(String cont) {
		this._cont = cont;
	}

	public String getInvalidLiteral() {
		return _cont;
	}

	@Override
	public String getMessage() {
		return "Content: " + this._cont + " is invalid";
	}

}
