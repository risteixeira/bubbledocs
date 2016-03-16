package pt.ulisboa.tecnico.bubbledocs.domain;

import java.time.LocalTime;
import java.util.Hashtable;
import java.util.Random;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;

public class Session extends Session_Base {

	private Hashtable<String, String> userSessions = new Hashtable<String, String>();

	public static Session getInstance() {
		Session session = FenixFramework.getDomainRoot().getSession();

		if (session == null)
			session = new Session();

		return session;
	}

	private Session() {
		FenixFramework.getDomainRoot().setSession(this);
	}

	public Hashtable<String, String> getUserSessions() {
		return userSessions;
	}

	public void setUserSessions(Hashtable<String, String> userSessions) {
		this.userSessions = userSessions;
	}

	public String createToken(String username) {
		Random r = new Random();
		Integer n = r.nextInt(10);
		String token = username + Integer.toString(n);
		return token;
	}

	public boolean userInSession(String UserToken) {

		if (userSessions.containsKey(UserToken)) {
			return true;
		}
		return false;
	}

	public boolean validUserSession(String token) throws EmptyUsernameException {

		if (token == null)
			throw new EmptyUsernameException();
		if (!userInSession(token))
			// throw new UserNotInSessionException(token);
			return false;

		LocalTime date = LocalTime.now();
		String timesession = userSessions.get(token);
		LocalTime dateTime = LocalTime.parse(timesession);

		if ((date.getHour() - dateTime.getHour()) > 2) {
			removeUserFromSession(token);
			return false;
		}

		return true;
	}

	public void updateTime(String token) {
		LocalTime date = LocalTime.now();
		String time = date.toString();
		userSessions.put(token, time);
	}

	public String addUserToSession(String username)
			throws InvalidUsernameException, EmptyUsernameException {
		if (username == null)
			throw new InvalidUsernameException(username);
		// Verificacao da existencia do utilizador
		BubbleDocs.getInstance().getUserByUsername(username);
		//
		String token = createToken(username);
		String time = LocalTime.now().toString();
		userSessions.put(token, time);
		return token;
	}

	public void removeUserFromSession(String token) {
		userSessions.remove(token);
	}

	public User getUserFromSession(String token)
			throws UserNotInSessionException, InvalidUsernameException,
			EmptyUsernameException {

		if (token == null)
			throw new EmptyUsernameException();

		if (userSessions.containsKey(token)) {
			String username = token.substring(0, token.length() - 1);
			User u = BubbleDocs.getInstance().getUserByUsername(username);
			return u;
		}
		throw new UserNotInSessionException(token);
	}

	public String getTokenByUsername(String username)
			throws EmptyUsernameException, UserNotInSessionException {
		if (username == null)
			throw new EmptyUsernameException();
		String token = null;
		if (!userSessions.keySet().isEmpty()) {
			for (String usertoken : userSessions.keySet()) {
				String user = usertoken.substring(0, usertoken.length() - 1);
				if (user.equals(username)) {
					token = usertoken;
				}
			}
		}

		return token;
	}

}
