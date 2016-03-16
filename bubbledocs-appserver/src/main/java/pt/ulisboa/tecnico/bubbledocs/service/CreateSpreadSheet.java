package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class CreateSpreadSheet extends BubbleDocsService {
	private int sheetId;
	private int columns;
	private int lines;
	private String name;
	private String userToken;

	public int getSheetId() {
		return sheetId;
	}

	public String getToken() {
		return userToken;
	}

	public CreateSpreadSheet(String userToken, String name, int lines,
			int columns) {
		this.userToken = userToken;
		this.name = name;
		this.lines = lines;
		this.columns = columns;
	}

	@Override
	protected void dispatch() throws BubbleException{
		if (getSession().validUserSession(userToken))
			getSession().updateTime(userToken);
		else
			throw new UserNotInSessionException(userToken);

		String username = userToken.substring(0, userToken.length() - 1);

		//proxima linha faz testes necessarios
		getSession().getUserFromSession(userToken);

		BubbleDocs db = getBubbledocs();

		SpreadSheet s = new SpreadSheet(name, lines, columns, username);
		
		sheetId = s.get_id();
		db.addSpreadsheet(s);
		
		for(int i = 1; i <= lines; i++){
			for(int j = 1; j <= columns; j++){
				Cell c = new Cell(i, j);
				s.addCell(c);
			}
		}
	}

	@Override
	protected void checkAuthorization() throws UnauthorizedOperationException {
	}
}
