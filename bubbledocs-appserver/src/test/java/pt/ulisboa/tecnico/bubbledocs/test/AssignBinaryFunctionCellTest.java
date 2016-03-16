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
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignBinaryFunctionCell;

public class AssignBinaryFunctionCellTest extends BubbleDocsServiceTest {
	private static final String USERNAME = "ba456";
	private static final String PASSWORD = "ba123";
	private static final String USERNAME_WITH_NO_PERMISSION = "rs456";
	private static final String PASSWORD_WITH_NO_PERMISSION = "rs321";
	private static final String SHEETNAME = "FolhaBrunex";
	private static final String CELLID = "5;5";
	private static final String ADD = "Add(4,6)";
	private static final String ADDANS = "10";
	private static final String SUB = "Sub(6,4)";
	private static final String SUBANS = "2";
	private static final String MUL = "Mul(2,5)";
	private static final String MULANS = "10";
	private static final String DIV = "DIV(4,2)";
	private static final String DIVANS = "2";
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
	public void successAdd() throws BubbleException {
		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(ba, SHEETID, CELLID,
				ADD);
		service.execute();

		Session session = Session.getInstance();
		BubbleDocs bd = BubbleDocs.getInstance();

		SpreadSheet ss = bd.getSpreadSheetByID(SHEETID);

		assertEquals(ba, session.getTokenByUsername(USERNAME));
		assertEquals(SHEETID, ss.get_id().intValue());
		assertEquals(ADDANS, service.getResult());
	}
	
	@Test
	public void successSub() throws BubbleException {
		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(ba, SHEETID, CELLID,
				SUB);
		service.execute();

		Session session = Session.getInstance();
		BubbleDocs bd = BubbleDocs.getInstance();

		SpreadSheet ss = bd.getSpreadSheetByID(SHEETID);

		assertEquals(ba, session.getTokenByUsername(USERNAME));
		assertEquals(SHEETID, ss.get_id().intValue());
		assertEquals(SUBANS, service.getResult());
	}
	
	@Test
	public void successMul() throws BubbleException {
		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(ba, SHEETID, CELLID,
				MUL);
		service.execute();

		Session session = Session.getInstance();
		BubbleDocs bd = BubbleDocs.getInstance();

		SpreadSheet ss = bd.getSpreadSheetByID(SHEETID);

		assertEquals(ba, session.getTokenByUsername(USERNAME));
		assertEquals(SHEETID, ss.get_id().intValue());
		assertEquals(MULANS, service.getResult());
	}
	
	@Test
	public void successDiv() throws BubbleException {
		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(ba, SHEETID, CELLID,
				DIV);
		service.execute();

		Session session = Session.getInstance();
		BubbleDocs bd = BubbleDocs.getInstance();

		SpreadSheet ss = bd.getSpreadSheetByID(SHEETID);

		assertEquals(ba, session.getTokenByUsername(USERNAME));
		assertEquals(SHEETID, ss.get_id().intValue());
		assertEquals(DIVANS, service.getResult());
	}
	
	@Test(expected = CellIsProtectedException.class)
	public void cellIsProtected() throws BubbleException {
		String[] parse = CELLID.split(";");
		BubbleDocs
				.getInstance()
				.getSpreadSheetByID(SHEETID)
				.getCellByLinCol(Integer.parseInt(parse[0]),
						Integer.parseInt(parse[1])).set_protected(true);
		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(ba, SHEETID, CELLID,
				ADD);
		service.execute();
	}
	
	@Test(expected = UserNotInSessionException.class)
	public void userNotInSession() throws BubbleException {
		removeUserFromSession(ba);
		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(ba, SHEETID, CELLID,
				SUB);
		service.execute();
	}
	
	@Test(expected = UnauthorizedOperationException.class)
	public void userHasNoPermission() throws BubbleException {
		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(rs, SHEETID, CELLID,
				MUL);
		service.execute();
	}
	
	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadSheetDoesNotExist() throws BubbleException {
		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(ba, 42, CELLID,
				DIV);
		service.execute();
	}
	
	@Test(expected = CellDoesNotExistException.class)
	public void cellDoesNotExist() throws BubbleException {
		AssignBinaryFunctionCell service = new AssignBinaryFunctionCell(ba, SHEETID, "11;11",
				ADD);
		service.execute();
	}
}
