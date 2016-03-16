package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class UnauthorizedOperationException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _conflictingUsername;

    public UnauthorizedOperationException(String conflictingName) {
        this._conflictingUsername = conflictingName;
    }

    public String getUnauthorizedOperation() {
        return this._conflictingUsername;
    }

    public String getMessage() {
        return "This user " + this._conflictingUsername + " is doing an unauthorized operation";
    }
	
	

}
