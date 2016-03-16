package pt.ulisboa.tecnico.sdis.id;

import java.math.BigInteger;
import java.security.SecureRandom;

public class User {
	private String username;
	private String email;
	private String password;
	
	private SecureRandom random = new SecureRandom();
	
	public User(String _username, String _email) {
		this.username = _username;
		this.email = _email;
		this.generatePassword();
	}
	
	//Bob
	public User(String _username, String _password, String _email){
		this.username = _username;
		this.password = _password;
		this.email = _email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String _username) {
		this.username = _username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String _email) {
		this.email = _email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String _password) {
		this.password = _password;
	}
	
	public void generatePassword(){
		this.password = new BigInteger(130, random).toString(32);
	}
	
}
