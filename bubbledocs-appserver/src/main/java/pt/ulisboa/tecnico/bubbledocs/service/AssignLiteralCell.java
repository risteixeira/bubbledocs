package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidLiteralException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class AssignLiteralCell extends BubbleDocsService {

	private String _result;
	private String _userToken;
	private int _sheetId;
	private String _cellId;
	private String _literal;

	public AssignLiteralCell(String userToken, int docId, String cellId,
			String literal) {
		this._userToken = userToken;
		this._sheetId = docId;
		this._cellId = cellId;
		this._literal = literal;
	}

	@Override
	protected void dispatch() throws BubbleException{
		Session session = BubbleDocsService.getSession();
		BubbleDocs bd = BubbleDocsService.getBubbledocs();
		
		SpreadSheet ss = bd.getSpreadSheetByID(_sheetId);
		String[] parse = _cellId.split(";");
		int row = Integer.parseInt(parse[0]);
		int column = Integer.parseInt(parse[1]);
		
		try {
			Integer.parseInt(_literal);

		} catch (NumberFormatException e) {
			throw new InvalidLiteralException(_literal);
		}

		int lit = Integer.parseInt(_literal);
		Literal l = new Literal(lit);

		session.updateTime(_userToken);
		
		checkAuthorization();
		Cell c = ss.getCellByLinCol(row, column);
		c.cellProtection();
		c.setContent(l);
		_result = c.getContent().getValue().toString();
}

	public String getResult() {
		return _result;
	}

	@Override
	protected void checkAuthorization() throws BubbleException {
		SpreadSheet ss = BubbleDocsService.getBubbledocs().getSpreadSheetByID(_sheetId);
		User user = BubbleDocsService.getSession().getUserFromSession(_userToken);

		if (!(_userToken.equals("root"))
				&& !(ss.get_owner().equals(user.get_username()))
				&& !(user.getAccessbySpreadsheet(_sheetId).get_permission()
						.equals("write")))
			throw new UnauthorizedOperationException(user.get_username());

	}

}
