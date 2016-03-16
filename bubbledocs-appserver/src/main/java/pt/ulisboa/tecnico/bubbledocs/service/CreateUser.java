package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class CreateUser extends BubbleDocsService {

	private String _userToken;
	private String _username;
	private String _email;
	private String _name;

	public CreateUser(String userToken, String newUsername, String email,
			String name) {
		this._userToken = userToken;
		this._username = newUsername;
		this._email = email;
		this._name = name;
	}

	public void checkAuthorization() throws UnauthorizedOperationException{
		if(_userToken == null)
			throw new UnauthorizedOperationException(_userToken);		
		String rootUsername = _userToken.substring(0, _userToken.length()-1);
		if (!(rootUsername.equals("root")))
			throw new UnauthorizedOperationException(_userToken);		
	}
	
	@Override
	protected void dispatch() throws BubbleException {
		checkAuthorization();
		Session session = BubbleDocsService.getSession();
		User user = new User(_username, _name, _email);
		session.getUserFromSession(_userToken);
		getBubbledocs().createUser(user);
		
	}
}
