package pt.ulisboa.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSpreadsheetException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

public class CreateSpreadSheetTest extends BubbleDocsServiceTest {
	
	private int columns = 10;
	private int lines = 10;
	private String name = "folha da rita";
	private String user = "rita";

	@Override
	public void populate4Test() throws BubbleException {
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = new User(user, "Rita", "123@tecnico");
		bd.addUser(u);
		User a = new User("manual1", "Manual1", "1@tecnico");
		bd.addUser(a);
		SpreadSheet s = new SpreadSheet(columns, name, columns, lines, user);
		
		LoginUser log = new LoginUser(user, "123");

		log.execute();

		u.addSpreadsheet(s);
	}

	@Test
	public void success() throws BubbleException {

		LoginUser log = new LoginUser(user, "123");
		log.execute();
		
		Session session = Session.getInstance();
		String userToken = session.getTokenByUsername(user);

		CreateSpreadSheet sheet = new CreateSpreadSheet(userToken,
				"folha da rita", 10, 10);
		sheet.execute();

		String username = userToken.substring(0, userToken.length() - 1);
		int SpreadId = sheet.getSheetId();
		SpreadSheet s = BubbleDocs.getInstance().getSpreadSheetByID(SpreadId);
		
		assertEquals("folha da rita", s.get_name());
		assertEquals(username, s.get_owner());
		assertEquals(Integer.toString(columns),
				Integer.toString(s.get_column()));
		assertEquals(Integer.toString(lines), Integer.toString(s.get_line()));
	}

	@Test(expected = UserNotInSessionException.class)
	public void UserNoSession() throws BubbleException {

		CreateSpreadSheet service = new CreateSpreadSheet("manual1", "hello",
				2, 4);

		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void UserDoesNotExist() throws BubbleException {
		
		LoginUser log = new LoginUser("no-one", "1");
		log.execute();
		
		CreateSpreadSheet service = new CreateSpreadSheet("no-one1", name, 5, 5);

		service.execute();

	}

	@Test(expected = InvalidSpreadsheetException.class)
	public void InvalidRowsColumns() throws BubbleException {

		LoginUser log = new LoginUser(user, "123");
		log.execute();

		Session.getInstance().addUserToSession(user);
		String userToken = Session.getInstance().getTokenByUsername(user);

		CreateSpreadSheet service = new CreateSpreadSheet(userToken, "folha",
				-1, -1);

		service.execute();

	}
}