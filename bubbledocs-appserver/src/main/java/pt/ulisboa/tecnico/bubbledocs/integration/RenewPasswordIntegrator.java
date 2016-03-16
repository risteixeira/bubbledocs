package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.RenewPasswordService;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator{
	private String _userToken;
	private String _username;

	public RenewPasswordIntegrator(String _token){
		this._userToken = _token;
		this._username = _userToken.substring(0, _userToken.length()-1);		
	}

	@Override
	protected void dispatch() throws BubbleException {
	
		IDRemoteServices remID = new IDRemoteServices();
		
		try {
			try {
				remID.renewPassword(_userToken.substring(0, _userToken.length() - 1));
			} catch (LoginBubbleDocsException e) {
				throw e;
			}
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}
		
		RenewPasswordService service = new RenewPasswordService(_userToken);
		service.execute();
	}
	
	
}
