package pt.ulisboa.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.BubbleDocsService;

public class LoginUserIT extends BubbleDocsServiceIT {
	private static final String USERNAME = "alice";
	private static final String PASSWORD = "Aaa1";

	@Override
	public void populate4Test() throws BubbleException {
		BubbleDocs bd = BubbleDocsService.getBubbledocs();
		User u = new User(USERNAME, "Alice Pereira", "alice@tecnico.pt", PASSWORD);
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
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME,
				PASSWORD);
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
		LoginUserIntegrator service = new LoginUserIntegrator(USERNAME,
				PASSWORD);

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
	public void successLoginRemote() throws BubbleException {

		LoginUserIntegrator service = new LoginUserIntegrator("eduardo", "Eee5");
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void loginUnknownUser() throws BubbleException {

		LoginUserIntegrator service = new LoginUserIntegrator("jp2", "jpas");
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void loginUserWithinWrongPassword() throws BubbleException {

		LoginUserIntegrator service = new LoginUserIntegrator("bruno", "jp2");
		service.execute();
	}
}
