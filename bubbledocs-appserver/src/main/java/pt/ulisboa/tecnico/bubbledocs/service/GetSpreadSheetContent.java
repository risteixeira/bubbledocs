package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Access;
import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class GetSpreadSheetContent extends BubbleDocsService {
	private String _userToken;
	private int _sheetId;
	private int _columns;
	private int _lines;
	private SpreadSheet _sheet;
	private String[][] matrix;

	public GetSpreadSheetContent(String userToken, Integer sheetId) {
		this._userToken = userToken;
		this._sheetId = sheetId;
	}

	public String[][] GetSpreadSheetMatrix() {
		return matrix;
	}

	@Override
	protected void dispatch() throws BubbleException {
		checkAuthorization();
		BubbleDocs db = getBubbledocs();

		if (getSession().validUserSession(_userToken))
			getSession().updateTime(_userToken);
		else
			throw new UserNotInSessionException(_userToken);

		if (db.getSpreadSheetByID(_sheetId) != null)
			this._sheet = db.getSpreadSheetByID(_sheetId);
		else
			throw new SpreadsheetDoesNotExistException(_sheetId);

		getSession().getUserFromSession(_userToken);

		this._columns = _sheet.get_column();
		this._lines = _sheet.get_line();
		this.matrix = new String[_lines][_columns];

		for (int i = 1; i <= _lines; i++) {
			for (int j = 1; j <= _columns; j++) {
				String _content = " ";

				try {
					if (_sheet.getCellByLinCol(i, j).getContent() != null) {
						_content = _sheet.getCellByLinCol(i, j).getContent()
								.getDescription();
					}
				} catch (CellDoesNotExistException e) {
					_content = " ";
				}

				matrix[i - 1][j - 1] = _content;
			}
		}

		for (int i = 0; i <= _lines - 1; i++) {
			 System.out.print("|");
			for (int j = 0; j <= _columns - 1; j++) {
				System.out.print(matrix[i][j]);
				System.out.print("\t");
				System.out.print("|");
			}
			System.out.print("\n");
		}
	}

	@Override
	protected void checkAuthorization() throws UnauthorizedOperationException,
			BubbleException {
		String username = _userToken.substring(0, _userToken.length() - 1);

		Access userAccess = getBubbledocs().getSpreadSheetByID(_sheetId)
				.getAccessbyUser(
						_userToken.substring(0, _userToken.length() - 1));

		String sheetOwner = getBubbledocs().getSpreadSheetByID(_sheetId)
				.get_owner();

		if (!(username.equals("root")) && userAccess == null
				&& !sheetOwner.equals(username))
			throw new UnauthorizedOperationException(username);
	}

}
