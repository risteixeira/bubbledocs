package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleInvalidAccessException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class User extends User_Base {

	private ArrayList<SpreadSheet> _spreadSheets;

	public User() {
		super();
	}

	public User(String usrname) {
		super();
		this.set_username(usrname);
	}

	public User(String usrname, String name) {
		super();
		this.set_username(usrname);
		this.set_name(name);
	}

	public User(String usrname, String name, String email)
			throws InvalidUsernameException, EmptyUsernameException,
			DuplicateUsernameException {
		super();
		if (usrname == null || usrname.equals("")) {
			throw new EmptyUsernameException();
		}
		if (usrname.length() < 3 || usrname.length() > 8) {
			throw new InvalidUsernameException(usrname);
		}
		if (BubbleDocs.getInstance().hasUser(usrname)) {
			throw new DuplicateUsernameException(usrname);
		}
		this.set_username(usrname);
		this.set_name(name);
		this.set_email(email);
	}

	public User(String usrname, String name, String email, String password)
			throws InvalidUsernameException, EmptyUsernameException,
			DuplicateUsernameException {
		super();
		if (usrname == null || usrname.equals("")) {
			throw new EmptyUsernameException();
		}
		if (usrname.length() < 3 || usrname.length() > 8) {
			throw new InvalidUsernameException(usrname);
		}
		if (BubbleDocs.getInstance().hasUser(usrname)) {
			throw new DuplicateUsernameException(usrname);
		}
		this.set_username(usrname);
		this.set_name(name);
		this.set_email(email);
		this.set_password(password);
	}

	public SpreadSheet showSheet(Integer sheetID) {

		for (SpreadSheet sheet : _spreadSheets) {
			if (sheet.get_id() == (sheetID)) {
				return sheet;
			}
		}
		return null;
	}

	public Access getAccessbySpreadsheet(Integer sheetID)
			throws UnauthorizedOperationException {

		Set<Access> accessSet = this.getAccessSet();

		for (Access acs : accessSet) {
			if (acs.getSpreadsheet().get_id() == (sheetID))
				return acs;
		}
		throw new UnauthorizedOperationException(this.get_name());
	}

	public Session getSessionbyToken(String token) {
		Session sessionSet = getSessionbyToken(token);
		if (sessionSet != null) {
			return sessionSet;
		}
		return null;
	}

	public void changePermission(Integer sheetID, String usrname, String perm)
			throws BubbleInvalidAccessException, InvalidUsernameException,
			UnauthorizedOperationException, EmptyUsernameException,
			SpreadsheetDoesNotExistException {

		if (BubbleDocs.getInstance().getUserByUsername(usrname)
				.getAccessbySpreadsheet(sheetID).equals(null)) {
			Access accs = new Access(perm);

			BubbleDocs.getInstance().getUserByUsername(usrname).addAccess(accs);
			BubbleDocs.getInstance().getSpreadSheetByID(sheetID)
					.addAccess(accs);

			accs.setSpreadsheet(BubbleDocs.getInstance().getSpreadSheetByID(
					sheetID));
			accs.setUser(BubbleDocs.getInstance().getUserByUsername(usrname));
		}

		else if (this.getAccessbySpreadsheet(sheetID).get_permission()
				.equals("write")
				|| BubbleDocs.getInstance().getSpreadSheetByID(sheetID)
						.get_owner().equals(this.get_username())) {

			BubbleDocs.getInstance().getUserByUsername(usrname)
					.getAccessbySpreadsheet(sheetID).set_permission(perm);
		} else
			throw new BubbleInvalidAccessException(perm);
		return;
	}

	public Set<SpreadSheet> getSpreadSheetsByName(String name)
			throws SpreadsheetDoesNotExistException {

		if (name == null) {
			// esta excepcao nao esta correcta mas nao sei qual devo lancar
			throw new SpreadsheetDoesNotExistException();
		}
		Set<SpreadSheet> result = new HashSet<SpreadSheet>();
		Set<SpreadSheet> allSheets = BubbleDocs.getInstance()
				.getSpreadsheetSet();

		for (SpreadSheet item : allSheets) {
			if (item.get_name().equals(name)
					&& item.get_owner().equals(this.get_username())) {
				result.add(item);
			}
		}
		if (result.isEmpty())
			throw new SpreadsheetDoesNotExistException();

		return result;
	}

}
