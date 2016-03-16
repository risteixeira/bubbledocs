package pt.ulisboa.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUser;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

// add needed import declarations

public class CreateUserTest extends BubbleDocsServiceTest {

	
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
		createUser(ROOT_USERNAME, "SuperUser", "root@ist");
		createUser(USERNAME, "António Rito Silva", "ars@ist");
        root = addUserToSession("root");
        ars = addUserToSession("ars");

	}

	@Test
	public void success() throws BubbleException {
		
		LoginUser loginRoot = new LoginUser(ROOT_USERNAME, ROOT_PASSWORD);
		loginRoot.execute();

		CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST,
				"jose@ist", "José Ferreira");
		service.execute();

		// User is the domain class that represents a User
		User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

		assertEquals(USERNAME_DOES_NOT_EXIST, user.get_username());
		assertEquals("jose@ist", user.get_email());
		assertEquals("José Ferreira", user.get_name());
	}
	

	@Test(expected = DuplicateUsernameException.class)
	public void usernameExists() throws BubbleException {

		
		CreateUser service = new CreateUser(root, USERNAME, "jose@ist",
				"José Ferreira");

		service.execute();

	}

	@Test(expected = EmptyUsernameException.class)
	public void emptyUsername() throws BubbleException {
		
		LoginUser loginRoot = new LoginUser(ROOT_USERNAME, ROOT_PASSWORD);
		loginRoot.execute();

		CreateUser integrator = new CreateUser(root, "", "jose@ist", "José Ferreira");
		integrator.execute();

	}

	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedUserCreation() throws BubbleException {
		
		LoginUser loginArs = new LoginUser(USERNAME, PASSWORD);
		loginArs.execute();
	   
		CreateUser service = new CreateUser(ars, USERNAME_DOES_NOT_EXIST,
				"jose@ist", "José Ferreira");
		service.execute();

	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() throws BubbleException {
		
		LoginUser loginRoot = new LoginUser(ROOT_USERNAME, ROOT_PASSWORD);
		loginRoot.execute();
		
		removeUserFromSession(root);
		CreateUser service = new CreateUser(root, "coiso33",
				"jose@ist", "José Ferreira");
		service.execute();

	}

	@Test(expected = InvalidUsernameException.class)
	public void usernameIsBig() throws BubbleException {
		
		LoginUser loginRoot = new LoginUser(ROOT_USERNAME, ROOT_PASSWORD);
		loginRoot.execute();
		
		CreateUser service = new CreateUser(root, "hi", "jose@ist", "José Ferreira");
		service.execute();

	}
}
