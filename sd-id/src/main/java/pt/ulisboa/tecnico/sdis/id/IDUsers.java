package pt.ulisboa.tecnico.sdis.id;

import java.util.Hashtable;

public class IDUsers {
	
	private static IDUsers instance = null;
	private Hashtable<String, User> users = new Hashtable<String, User>();
	
	public static IDUsers getInstance() {
		
		if (instance == null)
			instance = new IDUsers();

		return instance;
	}

	private IDUsers() {
		
	}
	
	public Hashtable<String, String> getEmailSet() {
		Hashtable<String, String> result = new Hashtable<String, String>();
		
		for(String item : users.keySet()) {
			result.put(users.get(item).getEmail(), "");
		}
		
		return result;
	}
	
	public Hashtable<String, User> getUsers() {
		return users;
	}

	public void setUsers(Hashtable<String, User> users) {
		this.users = users;
	}
	
	public void addUser(User newUser) {
		this.users.put(newUser.getUsername(), newUser);
	}
	
	public void removeUser(String userId) {
		this.users.remove(userId);
	}

}
