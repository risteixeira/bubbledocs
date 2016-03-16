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
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidLiteralException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralCell;

public class AssignLiteralCellTest extends BubbleDocsServiceTest {
	private static final String USERNAME = "ba456";
	private static final String PASSWORD = "ba123";
	private static final String USERNAME_WITH_NO_PERMISSION = "rs456";
	private static final String PASSWORD_WITH_NO_PERMISSION = "rs321";
	private static final String SHEETNAME = "FolhaBrunex";
	private static final String CELLID = "5;5";
	private static final String LITERAL = "10";
	private static final int SHEETROWS = 10;
	private static final int SHEETCOLUMNS = 10;
	private static int SHEETID;

	private String ba;
	private String rs;

	@Override
	public void populate4Test() throws BubbleException {
		BubbleDocs bd = BubbleDocs.getInstance();
		User user = createUser(USERNAME, PASSWORD, "Bruno@Almeida");
		User fail = createUser(USERNAME_WITH_NO_PERMISSION,
				PASSWORD_WITH_NO_PERMISSION, "Rita@Soares");

		ba = addUserToSession(USERNAME);
		rs = addUserToSession(USERNAME_WITH_NO_PERMISSION);
		SpreadSheet ss = createSpreadSheet(user, SHEETNAME, SHEETROWS,
				SHEETCOLUMNS);

		SHEETID = ss.get_id();
		String[] parse = CELLID.split(";");
		Cell cell = ss.getCellByLinCol(Integer.parseInt(parse[0]),
				Integer.parseInt(parse[1]));

		ss.addCell(cell);
		bd.addUser(user);
		bd.addUser(fail);
		bd.addSpreadsheet(ss);

	}

	@Test
	public void success() throws BubbleException {
		AssignLiteralCell service = new AssignLiteralCell(ba, SHEETID, CELLID,
				LITERAL);
		service.execute();

		Session session = Session.getInstance();
		BubbleDocs bd = BubbleDocs.getInstance();

		SpreadSheet ss = bd.getSpreadSheetByID(SHEETID);

		assertEquals(ba, session.getTokenByUsername(USERNAME));
		assertEquals(SHEETID, ss.get_id().intValue());
		assertEquals(LITERAL, service.getResult());
	}

	@Test(expected = CellIsProtectedException.class)
	public void cellIsProtected() throws BubbleException {
		String[] parse = CELLID.split(";");
		BubbleDocs
				.getInstance()
				.getSpreadSheetByID(SHEETID)
				.getCellByLinCol(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).set_protected(true);
		AssignLiteralCell service = new AssignLiteralCell(ba, SHEETID, CELLID,
				LITERAL);
		service.execute();

	}

	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() throws BubbleException {
		removeUserFromSession(ba);
		AssignLiteralCell service = new AssignLiteralCell(ba, SHEETID, CELLID,
				LITERAL);
		service.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void userHasNoPermission() throws BubbleException {
		AssignLiteralCell service = new AssignLiteralCell(rs, SHEETID, CELLID,
				LITERAL);
		service.execute();
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadSheetDoesNotExist() throws BubbleException {
		AssignLiteralCell service = new AssignLiteralCell(ba, 42, CELLID,
				LITERAL);
		service.execute();
	}

	@Test(expected = CellDoesNotExistException.class)
	public void cellDoesNotExist() throws BubbleException {
		AssignLiteralCell service = new AssignLiteralCell(ba, SHEETID, "11;11",
				LITERAL);
		service.execute();
	}

	@Test(expected = InvalidLiteralException.class)
	public void invalidLiteral() throws BubbleException {
		AssignLiteralCell service = new AssignLiteralCell(ba, SHEETID, CELLID,
				"x");
		service.execute();
	}
}
