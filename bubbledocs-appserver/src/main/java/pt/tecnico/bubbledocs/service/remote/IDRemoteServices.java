package pt.tecnico.bubbledocs.service.remote;

import javax.xml.bind.DatatypeConverter;

import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.id.SDIdClient;
import pt.ulisboa.tecnico.sdis.id.SDIdClientException;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.Session;

public class IDRemoteServices {

	public void createUser(String username, String email)
			throws InvalidUsernameException, DuplicateUsernameException,
			DuplicateEmailException, InvalidEmailException,
			RemoteInvocationException {
		
		try {
			SDIdClient client = new SDIdClient();
			client.createUser(username, email);
		} catch (SDIdClientException e) {
			throw new RemoteInvocationException();
		} catch (EmailAlreadyExists_Exception e) {
			throw new DuplicateEmailException(email);
		} catch (InvalidEmail_Exception e) {
			throw new InvalidEmailException(email);
		} catch (InvalidUser_Exception e) {
			throw new InvalidUsernameException(username);
		} catch (UserAlreadyExists_Exception e) {
			throw new DuplicateUsernameException(username);
		}
	}
	
	public void loginUser(String username, String password) throws LoginBubbleDocsException, RemoteInvocationException {
		
		try {
			SDIdClient client = new SDIdClient();
			byte[] ticket = client.requestAuthentication(username, password.getBytes());
			Session.getInstance().addTicket(username, ticket);
		} catch (SDIdClientException e) {
			throw new RemoteInvocationException();
		} catch (AuthReqFailed_Exception e) {
			throw new LoginBubbleDocsException();
		}
		
	}
	
	public void removeUser(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		
		try {
			SDIdClient client = new SDIdClient();
			client.removeUser(username);
		} catch (SDIdClientException e) {
			throw new RemoteInvocationException();
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		}
		
	}
	public void renewPassword(String username) throws LoginBubbleDocsException, RemoteInvocationException {
		
		try {
			SDIdClient client = new SDIdClient();
			client.renewPassword(username);
		} catch (SDIdClientException e) {
			throw new RemoteInvocationException();
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		}	
	}
}
