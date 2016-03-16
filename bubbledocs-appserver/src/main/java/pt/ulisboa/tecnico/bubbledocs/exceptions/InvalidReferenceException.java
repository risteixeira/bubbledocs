package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidReferenceException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _ref;

	public InvalidReferenceException(String ref) {
		this._ref = ref;
	}

	public String getInvalidReference() {
		return _ref;
	}

	@Override
	public String getMessage() {
		return "Reference: " + this._ref + " is invalid";
	}

}
