package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUser;
import pt.ulisboa.tecnico.bubbledocs.service.DeleteUser;
import pt.ulisboa.tecnico.bubbledocs.service.GetUserInfoService;

public class DeleteUserIntegrator extends BubbleDocsIntegrator{
	
	private String _username;
	private String _token;

	public DeleteUserIntegrator(String userToken, String toDeleteUsername) {
		this._token = userToken;
		this._username = toDeleteUsername;
	}

	@Override
	protected void dispatch() throws BubbleException {
		
		User u = new GetUserInfoService(_username).UserInfo(_username);
		DeleteUser deleteUser = new DeleteUser(_token, _username);
		deleteUser.execute();
		
		IDRemoteServices remID = new IDRemoteServices();
		
		try {
			try {
				remID.removeUser(_username);
			} catch (LoginBubbleDocsException e) {
				CreateUser createUser = new CreateUser(_token, _username, u.get_email(), u.get_name());
				createUser.execute();
				throw e;
			}
		} catch (RemoteInvocationException e) {
			CreateUser createUser = new CreateUser(_token, _username, u.get_email(), u.get_name());
			createUser.execute();
			throw new UnavailableServiceException();
		}
		
	}

}
