package pt.ulisboa.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellIsProtectedException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidReferenceException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignReferenceCell;

public class AssignReferenceCellTest extends BubbleDocsServiceTest {

	private static final String USERNAME = "MG1";
	private static final String PASSWORD = "mg";
	private static final String USERNAME_WITHOUT_PERMISSION = "vida";
	private static final String PASSWORD_WITHOUT_PERMISSION = "vida";
	private static final String REFERENCE = "4;6";
	private static final String CELLID = "2;3";
	private static final String SHEET_NAME = "folha";
	private static final int SHEET_ROWS = 20;
	private static final int SHEET_COLUMNS = 20;
	private static int SHEETID;

	private String MGSession;
	private String vidaSession;

	@Override
	public void populate4Test() throws BubbleException {
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = createUser(USERNAME, PASSWORD, "Miguel@Geraldes");
		User v = createUser(USERNAME_WITHOUT_PERMISSION,
				PASSWORD_WITHOUT_PERMISSION, "vida@tecnico");

		MGSession = addUserToSession(USERNAME);
		vidaSession = addUserToSession(USERNAME_WITHOUT_PERMISSION);

		SpreadSheet ss = createSpreadSheet(u, SHEET_NAME, SHEET_ROWS,
				SHEET_COLUMNS);

		SHEETID = ss.get_id();
		String[] parse = CELLID.split(";");
		Cell cell = ss.getCellByLinCol(Integer.parseInt(parse[0]),
				Integer.parseInt(parse[1]));

		ss.addCell(cell);
		bd.addUser(u);
		bd.addUser(v);
		bd.addSpreadsheet(ss);

	}

	@Test
	public void success() throws BubbleException {

		AssignReferenceCell service = new AssignReferenceCell(MGSession,
				SHEETID, CELLID, REFERENCE);
		service.execute();

		Session session = Session.getInstance();
		BubbleDocs bd = BubbleDocs.getInstance();

		SpreadSheet ss = bd.getSpreadSheetByID(SHEETID);

		assertEquals(MGSession, session.getTokenByUsername(USERNAME));
		assertEquals(SHEETID, ss.get_id().intValue());
		assertEquals(REFERENCE, service.getResult());

	}

	@Test(expected = CellIsProtectedException.class)
	public void cellIsProtected() throws BubbleException {

		String[] parse = CELLID.split(";");
		SpreadSheet sheet = BubbleDocs.getInstance()
				.getSpreadSheetByID(SHEETID);
		Cell cell = new Cell(Integer.parseInt(parse[0]),
				Integer.parseInt(parse[1]));
		sheet.addCell(cell);
		Cell c1 = sheet.getCellByLinCol(Integer.parseInt(parse[0]),
				Integer.parseInt(parse[1]));
		c1.set_protected(true);
		AssignReferenceCell service = new AssignReferenceCell(MGSession,
				SHEETID, CELLID, REFERENCE);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() throws BubbleException {

		removeUserFromSession(MGSession);
		AssignReferenceCell service = new AssignReferenceCell(MGSession,
				SHEETID, CELLID, REFERENCE);
		service.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void userHasNoPermission() throws BubbleException {
		AssignReferenceCell service = new AssignReferenceCell(vidaSession,
				SHEETID, CELLID, REFERENCE);
		service.execute();
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() throws BubbleException {

		AssignReferenceCell service = new AssignReferenceCell(MGSession, -1,
				CELLID, REFERENCE);
		service.execute();
	}

	@Test(expected = CellDoesNotExistException.class)
	public void cellDoesNotExist() throws BubbleException {

		AssignReferenceCell service = new AssignReferenceCell(MGSession,
				SHEETID, "21;8", REFERENCE);
		service.execute();
	}

	@Test(expected = InvalidReferenceException.class)
	public void invalidReference() throws BubbleException {

		AssignReferenceCell service = new AssignReferenceCell(MGSession,
				SHEETID, CELLID, "kappa;kappa");
		service.execute();
	}
}
