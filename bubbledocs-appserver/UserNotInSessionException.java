package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class UserNotInSessionException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _conflictingUsername;

	public UserNotInSessionException(String conflictingName) {
		this._conflictingUsername = conflictingName;
	}

	public String getUserNotInSession() {
		return this._conflictingUsername;
	}

	public String getMessage() {
		return "This user " + this._conflictingUsername + " not in session";
	}

}
