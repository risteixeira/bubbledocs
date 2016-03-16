package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class RenewPasswordService extends BubbleDocsService {
	
	private String _userToken;
	private String _username;

	public RenewPasswordService(String _token){
		this._userToken = _token;
		this._username = _userToken.substring(0, _userToken.length()-1);		
	}
	
	@Override
	protected void dispatch() throws BubbleException{		
		
		if(Session.getInstance().getTokenByUsername(_username) == null)
			throw new LoginBubbleDocsException();
		User u = BubbleDocs.getInstance().getUserByUsername(_username);
		u.set_password(null);
	}

	@Override
	protected void checkAuthorization() throws UnauthorizedOperationException {}

}
