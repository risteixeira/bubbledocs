package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class LoginUser extends BubbleDocsService {

	private String _username;
	private String _password;

	public LoginUser(String username, String password) {
		this._username = username;
		this._password = password;
	}
	
	
	@Override
	protected void dispatch() throws BubbleException {
		
		Session session = getSession();
		String token = session.getTokenByUsername(_username);
		
		if(!getBubbledocs().hasUser(_username)){
			throw new LoginBubbleDocsException();
		}
		User user = getBubbledocs().getUserByUsername(_username);
		
		user.set_password(_password);
		
		if(token != null && session.validUserSession(token)){
			session.updateTime(token);
		}
		
		else{
			session.addUserToSession(_username);
		}
	
	}


	@Override
	protected void checkAuthorization() throws UnauthorizedOperationException {}
}
