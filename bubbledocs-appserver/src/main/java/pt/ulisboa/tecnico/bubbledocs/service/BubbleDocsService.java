package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public abstract class BubbleDocsService {

	
	@Atomic
	public final void execute() throws BubbleException {
		checkAuthorization();
		dispatch();
	}

	public static BubbleDocs getBubbledocs() {
		return BubbleDocs.getInstance();
	}

	public static Session getSession() {
		return Session.getInstance();
	}

	
	
	public static User getUser(String usrname) throws InvalidUsernameException,
			EmptyUsernameException {

		return getBubbledocs().getUserByUsername(usrname);

	}

	public static SpreadSheet getSpreadsheet(int sheetId)
			throws SpreadsheetDoesNotExistException {

		return getBubbledocs().getSpreadSheetByID(sheetId);

	}

	protected abstract void dispatch() throws BubbleException;
	protected abstract void checkAuthorization() throws UnauthorizedOperationException, BubbleException;
}