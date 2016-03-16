package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class GetUsername4TokenService extends BubbleDocsService{

	String userToken;

	public GetUsername4TokenService(String userToken){
		this.userToken = userToken;
	}

	
	@Override
	protected void dispatch() throws BubbleException{
		if (getSession().validUserSession(userToken)){
			User urs = getSession().getUserFromSession(userToken);
			String username = urs.get_username();
			System.out.println("Username: " + username);
		}	
	}

	public String getUsername4Token(){
		return userToken;
	}
	
	@Override
	protected void checkAuthorization() throws UnauthorizedOperationException {}
}
