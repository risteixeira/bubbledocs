package pt.ulisboa.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.ulisboa.tecnico.bubbledocs.integration.DeleteUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.GetUserInfoService;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

public class DeleteUserIT extends BubbleDocsServiceIT {
	private static final String USERNAME_TO_DELETE = "smf";
	private static final String USERNAME = "ars";
	private static final String PASSWORD = "ars";
	private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";

	// the tokens for user root
	private String root;

	@Override
	public void populate4Test() throws BubbleException {

		createUser(ROOT_USERNAME, "root", "Root@User");
		createUser(USERNAME, PASSWORD, "António@Rito");
		createUser("duarte", "Ddd4", "duarte@tecnico.pt");

		User smf = createUser(USERNAME_TO_DELETE, "smf", "Sérgio@Fernandes");

		createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);
		
		new LoginUserIntegrator(ROOT_USERNAME, "rootroot").execute();
		
		root = Session.getInstance().getTokenByUsername(ROOT_USERNAME);

	};

	@Test
	public void successIntegrator() throws BubbleException {
		new LoginUserIntegrator("duarte", "Ddd4").execute();
		DeleteUserIntegrator service = new DeleteUserIntegrator(root, "duarte");
		service.execute();

		boolean deleted;
		try {
			deleted = getUserFromUsername("duarte") == null;
		} catch (InvalidUsernameException e) {
			deleted = true;
		}

		assertTrue("user was not deleted", deleted);
	}

	/*
	 * accessUsername exists, is in session and is root toDeleteUsername exists
	 * and is in session Test if user and session are both deleted
	 */

	@Test(expected = UserNotInSessionException.class)
	public void userToDeleteDoesNotExist() throws BubbleException {
		new DeleteUserIntegrator(root, USERNAME_DOES_NOT_EXIST).execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void notRootUser() throws BubbleException {
		String ars = addUserToSession(USERNAME);
		new DeleteUserIntegrator(ars, USERNAME_TO_DELETE).execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void rootNotInSession() throws BubbleException {
		removeUserFromSession(root);

		new DeleteUserIntegrator(root, USERNAME_TO_DELETE).execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void notInSessionAndNotRoot() throws BubbleException {
		String ars = addUserToSession(USERNAME);
		removeUserFromSession(ars);

		new DeleteUserIntegrator(ars, USERNAME_TO_DELETE).execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void accessUserDoesNotExist() throws BubbleException {
		new DeleteUserIntegrator(USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE)
				.execute();
	}
}
