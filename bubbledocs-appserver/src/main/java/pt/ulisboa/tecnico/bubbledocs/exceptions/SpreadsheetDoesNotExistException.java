package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class SpreadsheetDoesNotExistException extends BubbleException {

	private static final long serialVersionUID = 1L;
	private int _sheetId;
	
	public SpreadsheetDoesNotExistException() {
	}

	public SpreadsheetDoesNotExistException(int sheetId) {
		this._sheetId = sheetId;
	}

	public String getSpreadsheetDoesNotExist() {
		return Integer.toString(_sheetId);
	}

	@Override
	public String getMessage() {
		return "Spreadsheet with ID:" + this._sheetId + "does not exist";
	}
}
