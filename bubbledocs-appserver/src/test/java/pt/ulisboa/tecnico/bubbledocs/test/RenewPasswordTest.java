package pt.ulisboa.tecnico.bubbledocs.test;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUser;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;
import pt.ulisboa.tecnico.bubbledocs.service.RenewPasswordService;

public class RenewPasswordTest extends BubbleDocsServiceTest {
	
	//@Mocked IDRemoteServices _idRemote;
	
	private String root;
	private static final String USERNAME = "ars";
	private static final String PASSWORD = "ars";
	private static final String ROOT_USERNAME = "root";
	private static final String ROOT_PASSWORD = "rootroot";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	
	
	@Override
	public void populate4Test() throws BubbleException {
		createUser(ROOT_USERNAME, "SuperUser" , "root@ist");
		createUser(USERNAME,"António Rito Silva" , "ars@ist");
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

		CreateUser service = new CreateUser(root, USERNAME_DOES_NOT_EXIST,
				"jose@ist", "José Ferreira");
		service.execute();
		
		LoginUser loginJose = new LoginUser(USERNAME_DOES_NOT_EXIST, PASSWORD);
		loginJose.execute();
		User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);
		
		String joseToken = Session.getInstance().getTokenByUsername(USERNAME_DOES_NOT_EXIST);
		
		RenewPasswordService newPass = new RenewPasswordService(joseToken);
		newPass.execute();
		
		assertNull(user.get_password());
	}
	
	@Test(expected = LoginBubbleDocsException.class)
	public void InvalidUser() throws BubbleException {
		RenewPasswordService newPass = new RenewPasswordService("he");
		newPass.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void UserNotInSession() throws BubbleException {
		
		RenewPasswordService newpass = new RenewPasswordService("hello");
		newpass.execute();
	}
}
