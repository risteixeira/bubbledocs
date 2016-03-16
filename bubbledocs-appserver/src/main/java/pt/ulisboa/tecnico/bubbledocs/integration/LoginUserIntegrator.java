package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private String _username;
	private String _password;

	public LoginUserIntegrator(String username, String password) {
		this._username = username;
		this._password = password;
	}
	
	
	@Override
	protected void dispatch() throws BubbleException {

		IDRemoteServices remID = new IDRemoteServices();
		
		try {
			try {
				remID.loginUser(_username, _password);
			} catch (LoginBubbleDocsException e) {
				throw e;
			}
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		LoginUser service = new LoginUser(_username, _password);
		service.execute();
		
	}

}
