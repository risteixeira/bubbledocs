package pt.ulisboa.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.ulisboa.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.LoginUserIntegrator;

// add needed import declarations

public class CreateUserIT extends BubbleDocsServiceIT {

	// the tokens
	private String root;
	private String ars;

	private static final String USERNAME = "alice";
	private static final String PASSWORD = "Aaa1";
	private static final String ROOT_USERNAME = "root";
	private static final String ROOT_PASSWORD = "rootroot";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";

	@Override
	public void populate4Test() throws BubbleException {
		createUser(ROOT_USERNAME, "root@ist", "SuperUser");
		createUser(USERNAME, "ars@ist", "António Rito Silva");
		root = addUserToSession("root");
		ars = addUserToSession("ars");

	}

	@Test
	public void successIntegrator() throws BubbleException {

		LoginUserIntegrator loginRoot = new LoginUserIntegrator(ROOT_USERNAME,
				ROOT_PASSWORD);
		loginRoot.execute();

		CreateUserIntegrator service = new CreateUserIntegrator(root, "macaco",
				"macaco@ist", "Macaco Ferreira");
		service.execute();

		// User is the domain class that represents a User
		User user = getUserFromUsername("macaco");

		assertEquals("macaco", user.get_username());
		assertEquals("macaco@ist", user.get_email());
		assertEquals("Macaco Ferreira", user.get_name());
	}

	@Test(expected = DuplicateUsernameException.class)
	public void usernameExists() throws BubbleException {

		CreateUserIntegrator service = new CreateUserIntegrator(root, USERNAME,
				"jose@ist", "José Ferreira");

		service.execute();

	}

	@Test(expected = EmptyUsernameException.class)
	public void emptyUsername() throws BubbleException {

		LoginUserIntegrator loginRoot = new LoginUserIntegrator(ROOT_USERNAME,
				ROOT_PASSWORD);
		loginRoot.execute();

		CreateUserIntegrator integrator = new CreateUserIntegrator(root, "",
				"jose@ist", "José Ferreira");
		integrator.execute();

	}

	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedUserCreation() throws BubbleException {

		LoginUserIntegrator loginArs = new LoginUserIntegrator(USERNAME,
				PASSWORD);
		loginArs.execute();

		CreateUserIntegrator service = new CreateUserIntegrator(ars,
				USERNAME_DOES_NOT_EXIST, "jose@ist", "José Ferreira");
		service.execute();

	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() throws BubbleException {

		LoginUserIntegrator loginRoot = new LoginUserIntegrator(ROOT_USERNAME,
				ROOT_PASSWORD);
		loginRoot.execute();

		removeUserFromSession(root);
		CreateUserIntegrator service = new CreateUserIntegrator(root,
				"coiso33", "jose@ist", "José Ferreira");
		service.execute();

	}

	@Test(expected = InvalidUsernameException.class)
	public void usernameIsBig() throws BubbleException {

		LoginUserIntegrator loginRoot = new LoginUserIntegrator(ROOT_USERNAME,
				ROOT_PASSWORD);
		loginRoot.execute();

		CreateUserIntegrator service = new CreateUserIntegrator(root, "hi",
				"jose@ist", "José Ferreira");
		service.execute();

	}
}
