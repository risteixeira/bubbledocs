package pt.ulisboa.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.RenewPasswordIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.BubbleDocsService;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUser;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

public class RenewPasswordIT extends
		BubbleDocsServiceIT {
	// @Mocked IDRemoteServices _idRemote;

	private String root;
	private static final String USERNAME = "ars";
	private static final String PASSWORD = "ars";
	private static final String ROOT_USERNAME = "root";
	private static final String ROOT_PASSWORD = "rootroot";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";

	@Override
	public void populate4Test() throws BubbleException {
		createUser(ROOT_USERNAME, "SuperUser", "root@ist");
		createUser(USERNAME, "António Rito Silva", "ars@ist");
		createUser("carla", "Ccc3", "carla@tecnico.pt");
		root = addUserToSession("root");
		addUserToSession("ars");
		addUserToSession("carla");
		User u = getUserFromUsername(USERNAME);
		u.set_password(PASSWORD);
	}

	@Test
	public void success() throws BubbleException {

		LoginUser loginRoot = new LoginUser(ROOT_USERNAME, ROOT_PASSWORD);
		loginRoot.execute();

		User user = getUserFromUsername("carla");

		String carlaToken = BubbleDocsService.getSession().getTokenByUsername(
				"carla");

		RenewPasswordIntegrator newPass = new RenewPasswordIntegrator(carlaToken);
		newPass.execute();

		assertNull(user.get_password());
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void InvalidUser() throws BubbleException {
		RenewPasswordIntegrator newPass = new RenewPasswordIntegrator("he");
		newPass.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void UserNotInSession() throws BubbleException {

		RenewPasswordIntegrator newpass = new RenewPasswordIntegrator("hello");
		newpass.execute();
	}
}
