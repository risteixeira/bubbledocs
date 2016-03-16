package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidSpreadsheetException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private String _invalidSs;

	public InvalidSpreadsheetException(String invalidSs) {
		this._invalidSs = invalidSs;
	}

	@Override
	public String getMessage() {
		return "This spreadsheet " + this._invalidSs + " is invalid";
	}

}
