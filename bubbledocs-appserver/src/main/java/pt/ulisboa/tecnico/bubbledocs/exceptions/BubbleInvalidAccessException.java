package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class BubbleInvalidAccessException extends BubbleException {

	private static final long serialVersionUID = 1L;

	private String _permission;

	public BubbleInvalidAccessException(String permission) {
		this._permission = permission;
	}

	public String getPermission() {
		return this._permission;
	}
	
	@Override
	public String getMessage() {
		return "Don't have permission";
	}

}
