package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidEmailException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _invalidEmail;

	public InvalidEmailException(String invalidEmail) {
		this._invalidEmail = invalidEmail;
	}

	public String getDuplicateUsername() {
		return this._invalidEmail;

	}
	
	@Override
	public String getMessage() {
		return "This email " + this._invalidEmail + " is invalid";
	}

}
