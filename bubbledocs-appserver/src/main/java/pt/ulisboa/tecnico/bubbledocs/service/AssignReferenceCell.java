package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidReferenceException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class AssignReferenceCell extends BubbleDocsService {

	private String _result;
	private String _reference;
	private String _cellId;
	private int _docId;
	private String _tokenUser;

	public AssignReferenceCell(String tokenUser, int docId, String cellId,
			String reference) {

		this._reference = reference;
		this._cellId = cellId;
		this._docId = docId;
		this._tokenUser = tokenUser;

	}

	@Override
	protected void dispatch() throws BubbleException {
		Session session = BubbleDocsService.getSession();
		BubbleDocs bd = BubbleDocsService.getBubbledocs();

		SpreadSheet ss = bd.getSpreadSheetByID(_docId);
		String[] parse = _cellId.split(";");
		String[] refParse = _reference.split(";");
		int row = Integer.parseInt(parse[0]);
		int column = Integer.parseInt(parse[1]);

		try {
			Integer.parseInt(refParse[0]);
			Integer.parseInt(refParse[1]);

		} catch (NumberFormatException e) {
			throw new InvalidReferenceException(_reference);
		}

		int refRow = Integer.parseInt(refParse[0]);
		int refColumn = Integer.parseInt(refParse[1]);

		Reference ref = new Reference(refRow, refColumn);

		session.updateTime(_tokenUser);

		checkAuthorization();
		Cell c = ss.getCellByLinCol(row, column);
		c.cellProtection();
		c.setContent(ref);
		_result = c.getContent().getDescription();

	}

	public final String getResult() {
		return _result;
	}

	@Override
	protected void checkAuthorization() throws BubbleException {
		SpreadSheet ss = BubbleDocsService.getBubbledocs().getSpreadSheetByID(
				_docId);
		User user = BubbleDocsService.getSession().getUserFromSession(
				_tokenUser);

		if (!(_tokenUser.equals("root"))
				&& !(ss.get_owner().equals(user.get_username()))
				&& !(user.getAccessbySpreadsheet(_docId).get_permission()
						.equals("write")))
			throw new UnauthorizedOperationException(user.get_username());

	}
}
