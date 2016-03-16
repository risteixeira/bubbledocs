package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class DuplicateUsernameException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _conflictingUsername;

	public DuplicateUsernameException(String conflictingName) {
		this._conflictingUsername = conflictingName;
	}

	public String getDuplicateUsername() {
		return this._conflictingUsername;

	}
	
	@Override
	public String getMessage() {
		return "This username " + this._conflictingUsername + " is duplicated";
	}

}
