package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class DuplicateEmailException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _duplicateEmail;

	public DuplicateEmailException(String duplicateEmail) {
		this._duplicateEmail = duplicateEmail;
	}

	public String getDuplicateEmail() {
		return this._duplicateEmail;

	}

	@Override
	public String getMessage() {
		return "This email " + this._duplicateEmail + " is duplicated";
	}

}
