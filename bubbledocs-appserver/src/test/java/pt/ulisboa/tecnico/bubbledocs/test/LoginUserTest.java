package pt.ulisboa.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.service.BubbleDocsService;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

public class LoginUserTest extends BubbleDocsServiceTest {


	private static final String USERNAME = "jpa";
	private static final String PASSWORD = "jp#";
	

	@Override
	public void populate4Test() throws BubbleException {
		BubbleDocs bd = BubbleDocsService.getBubbledocs();
		User u = new User(USERNAME, "João Pereira", PASSWORD);
		bd.addUser(u);
		User u2 = new User("eduardo", "Eduardo", "eduardo@tecnico.pt");
		bd.addUser(u2);

	}

	private LocalTime getLastAccessTimeInSession(String userToken) {
		Session session = BubbleDocsService.getSession();
		String timesession = session.getUserSessions().get(userToken);
		LocalTime dateTime = LocalTime.parse(timesession);
		return dateTime;

	}

	@Test
	public void success() throws BubbleException {
		LoginUser service = new LoginUser(USERNAME, PASSWORD);
		Session session = BubbleDocsService.getSession();

		service.execute();

		LocalTime currentTime = LocalTime.now();

		String token = session.getTokenByUsername(USERNAME);

		User user = getUserFromSession(token);

		assertEquals(USERNAME, user.get_username());

		Integer difference = getLastAccessTimeInSession(token).getHour()
				- currentTime.getHour();

		assertTrue("Access time in session not correctly set", difference >= 0);
		assertTrue("diference in seconds greater than expected", difference < 2);
	}

	@Test
	public void successLoginTwice() throws BubbleException {
		Session session = BubbleDocsService.getSession();
		LoginUser service = new LoginUser(USERNAME, PASSWORD);

		service.execute();

		String token1 = session.getTokenByUsername(USERNAME);

		service.execute();

		String token2 = session.getTokenByUsername(USERNAME);

		User user = getUserFromSession(token1);

		// assertNull(user);
		user = getUserFromSession(token2);
		assertEquals(USERNAME, user.get_username());
	}
	
	@Test
	public void loginOfflineWrongPassword() throws BubbleException {
		
		LoginUser service = new LoginUser(USERNAME, "");

		service.execute();
	}


	@Test(expected = LoginBubbleDocsException.class)
	public void loginUnknownUser() throws BubbleException {

		LoginUser service = new LoginUser("jp2", "jpas");
		service.execute();

	}

	@Test(expected = LoginBubbleDocsException.class)
	public void loginUserWithinWrongPassword() throws BubbleException {

		LoginUser service = new LoginUser("bruno", "jp2");
		service.execute();

	}

}
