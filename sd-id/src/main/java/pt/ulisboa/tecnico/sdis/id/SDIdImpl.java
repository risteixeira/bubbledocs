package pt.ulisboa.tecnico.sdis.id;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;

import javax.jws.WebService;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

@WebService(endpointInterface = "pt.ulisboa.tecnico.sdis.id.ws.SDId", wsdlLocation = "SD-ID.1_1.wsdl", name = "SDId", portName = "SDIdImplPort", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:id:ws", serviceName = "SDId")
public class SDIdImpl implements SDId {

	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {

		if (emailAddress == null) {
			InvalidEmail errorEmail = new InvalidEmail();
			errorEmail.setEmailAddress(null);
			throw new InvalidEmail_Exception("Insert an Email", errorEmail);
		}

		String[] parsedEmail = emailAddress.split("@");

		if (parsedEmail == null || parsedEmail.length != 2
				|| parsedEmail[0].equals("")) {
			InvalidEmail errorEmail = new InvalidEmail();
			errorEmail.setEmailAddress(emailAddress);
			throw new InvalidEmail_Exception("Email is not valid", errorEmail);
		}

		if (IDUsers.getInstance().getEmailSet().containsKey(emailAddress)) {
			EmailAlreadyExists errorEmail = new EmailAlreadyExists();
			errorEmail.setEmailAddress(emailAddress);
			throw new EmailAlreadyExists_Exception("That email already exists",
					errorEmail);
		}

		if (userId == null || userId.length() < 3 || userId.length() > 8) {
			InvalidUser errorId = new InvalidUser();
			errorId.setUserId(userId);
			throw new InvalidUser_Exception(
					"Username not valid. Must be between 3 and 8 characters long",
					errorId);
		}

		if (IDUsers.getInstance().getUsers().containsKey(userId)) {
			UserAlreadyExists errorId = new UserAlreadyExists();
			errorId.setUserId(userId);
			throw new UserAlreadyExists_Exception(userId, errorId);
		}

		User newUser = new User(userId, emailAddress);
		IDUsers.getInstance().addUser(newUser);

		System.out.println(newUser.getPassword());
	}

	public void renewPassword(String userId) throws UserDoesNotExist_Exception {

		if (userId == null || userId.equals("")
				|| !(IDUsers.getInstance().getUsers().containsKey(userId))) {
			UserDoesNotExist errorId = new UserDoesNotExist();
			errorId.setUserId(userId);
			throw new UserDoesNotExist_Exception(userId, errorId);
		}

		User user = IDUsers.getInstance().getUsers().get(userId);
		user.generatePassword();

		System.out.println(user.getPassword());
	}

	public void removeUser(String userId) throws UserDoesNotExist_Exception {

		if (userId == null || userId.equals("")
				|| !(IDUsers.getInstance().getUsers().containsKey(userId))) {
			UserDoesNotExist errorId = new UserDoesNotExist();
			errorId.setUserId(userId);
			throw new UserDoesNotExist_Exception(userId, errorId);
		}

		IDUsers.getInstance().removeUser(userId);
	}

	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {

		if (userId == null) {
			AuthReqFailed errorId = new AuthReqFailed();
			errorId.setReserved(null);
			throw new AuthReqFailed_Exception("Invalid username", errorId);
		}

		if (userId.equals("")
				|| !(IDUsers.getInstance().getUsers().containsKey(userId))) {
			AuthReqFailed errorId = new AuthReqFailed();
			errorId.setReserved(userId.getBytes());
			throw new AuthReqFailed_Exception("Invalid username", errorId);
		}
		
		if(reserved == null) {
			AuthReqFailed errorPass = new AuthReqFailed();
			errorPass.setReserved(null);
			throw new AuthReqFailed_Exception("Invalid password", errorPass);
		}

		User user = IDUsers.getInstance().getUsers().get(userId);
		
		String propertyValue = new String(reserved);
		
		String[] properties;
		String[] password;
		
		System.out.println(propertyValue);
		properties = propertyValue.split(",");
		password = properties[0].split("=");

		if (!(password[1].equals(user.getPassword()))) {
			AuthReqFailed errorPass = new AuthReqFailed();
			errorPass.setReserved(reserved);
			throw new AuthReqFailed_Exception("Invalid password", errorPass);
		}
		
		byte[] ticket = null;
		Key serverClientKey = null;
		
		try {
			serverClientKey = AuthenticationKeys.makeKey();
			ticket = AuthenticationKeys.generateTicket(userId, serverClientKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] splitter = new byte[] { 0x00, 0x00 };
		
		int nonce = Integer.parseInt(properties[2]);
		nonce += 1;
		
		ByteArrayOutputStream clientResponse = new ByteArrayOutputStream();
		
		try {
			clientResponse.write(serverClientKey.getEncoded());
			clientResponse.write(splitter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		clientResponse.write(nonce);
		
		byte[] encriptedClientResponse = null;
		try {
			Key clientKey = AuthenticationKeys.makeKeySpec(AuthenticationKeys.hashPassword(password[1]));
			encriptedClientResponse = AuthenticationKeys.encrypt(clientKey, clientResponse.toByteArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ByteArrayOutputStream response = new ByteArrayOutputStream();
		try {
			response.write(ticket);
			response.write(splitter);
			response.write(encriptedClientResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response.toByteArray();
	}

}
