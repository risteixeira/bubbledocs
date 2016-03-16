package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class CellDoesNotExistException extends BubbleException {
	
	private static final long serialVersionUID = 1L;
	private String _cellId;

	public CellDoesNotExistException(String cellId) {
		this._cellId = cellId;
	}

	public String getCellDoesNotExist() {
		return _cellId;
	}

	@Override
	public String getMessage() {
		return "Cell with ID: " + this._cellId + " does not exist";
	}
}
