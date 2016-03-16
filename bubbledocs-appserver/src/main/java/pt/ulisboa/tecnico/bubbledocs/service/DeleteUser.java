package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class DeleteUser extends BubbleDocsService {

	private String _username;
	private String _token;

	public DeleteUser(String userToken, String toDeleteUsername) {
		this._token = userToken;
		this._username = toDeleteUsername;
	}

	@Override
	protected void dispatch() throws BubbleException{
		checkAuthorization();
		String usrtoken = getSession().getTokenByUsername(_username);
		if (usrtoken == null)
			throw new UserNotInSessionException(_username);
		getSession().removeUserFromSession(usrtoken);
		getBubbledocs().removeUser(_username);
	}

	@Override
	protected void checkAuthorization() throws UnauthorizedOperationException, BubbleException {
		String userName = _token.substring(0, _token.length() - 1);
		if (!(userName.equals("root")))
			throw new UnauthorizedOperationException(_token);
		else if (getSession().getTokenByUsername(userName) == null)
			throw new UnauthorizedOperationException(_token);

	}
}
