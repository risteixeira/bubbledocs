package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUser;
import pt.ulisboa.tecnico.bubbledocs.service.DeleteUser;

public class CreateUserIntegrator extends BubbleDocsIntegrator {
	
	private String _userToken;
	private String _username;
	private String _email;
	private String _name;

	public CreateUserIntegrator(String userToken, String newUsername, String email,
			String name) {
		this._userToken = userToken;
		this._username = newUsername;
		this._email = email;
		this._name = name;
	}
	
	protected void dispatch() throws BubbleException{
		
		CreateUser createUser = new CreateUser(_userToken, _username, _email, _name);
		createUser.execute();
		
		IDRemoteServices remID = new IDRemoteServices();
		
		try {
			
			try {
				remID.createUser(_username, _email);
			} catch (InvalidUsernameException | DuplicateUsernameException
					| DuplicateEmailException | InvalidEmailException e) {
				DeleteUser deleteUser = new DeleteUser(_userToken, _username);
				deleteUser.execute();
				throw e;
			}
			
		} catch (RemoteInvocationException e) {
			DeleteUser deleteUser = new DeleteUser(_userToken, _username);
			deleteUser.execute();
			throw new UnavailableServiceException();
		}
	}
}
