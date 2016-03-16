package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.Set;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleDividedByZeroException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSpreadsheetException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class BubbleDocs extends BubbleDocs_Base {

	public static BubbleDocs getInstance() {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();

		if (bd == null)
			bd = new BubbleDocs();

		return bd;
	}

	private BubbleDocs() {
		FenixFramework.getDomainRoot().setBubbledocs(this);
		set_sheetCounter(0);

	}

	public User getUserByUsername(String username)
			throws InvalidUsernameException, EmptyUsernameException {

		User usr = null;
		if (username.equals(""))
			throw new EmptyUsernameException();

		for (User user : getUserSet()) {
			if (user.get_username().equals(username)) {
				usr = user;
			}
		}

		return usr;

	}

	public boolean hasUser(String username) throws InvalidUsernameException,
			EmptyUsernameException {
		return getUserByUsername(username) != null;
	}

	public void createUser(User user) throws EmptyUsernameException,
			InvalidUsernameException, UnauthorizedOperationException {

		if ((user.get_username().equals("")))
			throw new EmptyUsernameException();

		if ((user.get_username().length() < 3)
				|| (user.get_username().length() > 8))
			throw new InvalidUsernameException(user.get_username());

		super.addUser(user);
	}

	public void importFromXML(Element spreadSheetElement, String usrname)
			throws BubbleDividedByZeroException, InvalidContentException,
			InvalidSpreadsheetException {
		try {
			String owner = spreadSheetElement.getAttributeValue("owner");
			Integer lines = spreadSheetElement.getAttribute("lines")
					.getIntValue();
			Integer columns = spreadSheetElement.getAttribute("columns")
					.getIntValue();
			String name = spreadSheetElement.getAttributeValue("name");

			if (usrname.equals(owner)) {
				SpreadSheet spreadsheet = new SpreadSheet(name, lines,
						columns, owner);
				spreadsheet.importFromXML(spreadSheetElement, usrname);
				this.addSpreadsheet(spreadsheet);
			}

		} catch (DataConversionException e) {
			System.out.println("Error in importing SpreadSheet from XML");
			e.printStackTrace();
		}
	}

	public SpreadSheet getSpreadSheetByID(Integer sheetID)
			throws SpreadsheetDoesNotExistException {

		if (sheetID == null || sheetID < 0) {
			throw new SpreadsheetDoesNotExistException();
		}

		Set<SpreadSheet> sheets = this.getSpreadsheetSet();
		SpreadSheet sheet = null;

		for (SpreadSheet item : sheets) {
			if (item.get_id().equals(sheetID))
				sheet = item;
		}

		if (sheet == null)
			throw new SpreadsheetDoesNotExistException();

		return sheet;
	}

	public void incrSheetCounter() {
		Integer newCounter;
		newCounter = get_sheetCounter() + 1;
		set_sheetCounter(newCounter);
	}

	public void removeUser(String usrname) {
		User usr = null;
		Set<User> usrSet = BubbleDocs.getInstance().getUserSet();
		for (User item : usrSet) {
			if (item.get_username().equals(usrname)) {
				usr = item;
				break;
			}
		}

		Set<SpreadSheet> ssSet = BubbleDocs.getInstance().getSpreadsheetSet();

		for (SpreadSheet item : ssSet) {
			if (item.get_owner().equals(usrname)) {
				BubbleDocs.getInstance().removeSpreadsheet(item);
			}
		}

		BubbleDocs.getInstance().removeUser(usr);
	}
}
