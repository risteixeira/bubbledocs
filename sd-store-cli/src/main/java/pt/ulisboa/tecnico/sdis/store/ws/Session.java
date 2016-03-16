package pt.ulisboa.tecnico.sdis.store.ws;

import java.security.Key;
import java.util.Hashtable;

public class Session {

	protected static Session instance = null;
	protected Key key = null;
	
	private Hashtable<String, byte[]> userTokens = new Hashtable<String, byte[]>();
	private Hashtable<String, Key> userKeys = new Hashtable<String, Key>();
	
	public static Session getInstance() {

		if (instance == null)
			instance = new Session();

		return instance;
	}

	private Session() {
		
		
	}
	
	public void addTicket(String username, byte[] ticket) {
		userTokens.put(username, ticket);
	}
	
	public byte[] getTicket(String username) {
		return userTokens.get(username);
	}
	
	public void addKey(String username, Key key) {
		userKeys.put(username, key);
	}
	
	public Key getKey(String username) {
		return userKeys.get(username);
	}
}
