package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class GetUserInfoService extends BubbleDocsService {
	
	String username;
	
	public GetUserInfoService(String username){
		this.username = username;
	}

	@Override
	protected void dispatch() throws BubbleException{
		User u =  UserInfo(username);
		String usr = username;
		String name = u.get_name();
		String email = u.get_email();
		System.out.println("Username: " + usr + "\n" + "Name : " + name + "\n" + "Email" + email + "\n");
	}

	public User UserInfo(String username) throws BubbleException{
		User u =  getBubbledocs().getUserByUsername(username);
		return u;
	}
	
	@Override
	protected void checkAuthorization() throws UnauthorizedOperationException {}

}
