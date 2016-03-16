package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidUsernameException extends BubbleException {

	private static final long serialVersionUID = 1L;
	
	private String _invalidUsername;

	public InvalidUsernameException(String invalidUsername) {
		this._invalidUsername = invalidUsername;
	}

	@Override
	public String getMessage() {
		return "This username " + this._invalidUsername + " is invalid";
	}

}
