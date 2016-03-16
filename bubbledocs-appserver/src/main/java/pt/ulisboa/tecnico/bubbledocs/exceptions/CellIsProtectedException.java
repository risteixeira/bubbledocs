package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class CellIsProtectedException extends BubbleException {
	
	private static final long serialVersionUID = 1L;
	private String _cellId;
	
	public CellIsProtectedException(){}

	public CellIsProtectedException(String cellId) {
		this._cellId = cellId;
	}

	public String getCellIsProtected() {
		return _cellId;
	}

	@Override
	public String getMessage() {
		return "Cell: " + this._cellId + " is protected";
	}

}
