package pt.ulisboa.tecnico.bubbledocs.test;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.service.ExportDocument;
import pt.ulisboa.tecnico.bubbledocs.service.ImportDocument;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

public class ImportDocumentTest extends BubbleDocsServiceTest{
	private String _userToken;
	private String _username = "antonio";
	private String _userToken1;
	private String _username1 = "antonio1";
	int _docId;
	int _docId2;
	byte [] testbyte = {1};
	
	public void populate4Test() throws BubbleException {
		BubbleDocs bd = BubbleDocs.getInstance();		
		super.populate4Test();
		
		User u = new User(_username, "Antonio", "123");
		bd.addUser(u);

		LoginUser login = new LoginUser(_username, "123");
		login.execute();
		this._userToken = Session.getInstance().getTokenByUsername(_username);
		CreateSpreadSheet s = new CreateSpreadSheet(_userToken, "folhas", 1, 1);
		s.execute();
		_docId2=s.getSheetId();
			
		User u1 = new User(_username1, "Antonio1", "123");
		bd.addUser(u1);

		LoginUser login1 = new LoginUser(_username1, "123");
		login1.execute();
		this._userToken1 = Session.getInstance().getTokenByUsername(_username1);
		CreateSpreadSheet s1 = new CreateSpreadSheet(_userToken1, "folhas1", 1, 1);
		s1.execute();
		_docId=s1.getSheetId();
	}
	
	@Test
	public void success() throws BubbleException {

		SpreadSheet origSpread = BubbleDocs.getInstance().getSpreadSheetByID(
				_docId2);
		
		ExportDocument xml = new ExportDocument(_userToken, _docId2);
		xml.execute();
		
		ImportDocument service = new ImportDocument(_userToken, _docId2);
		service.setBytes(xml.getDocXML());
		service.execute();
		
		assertEquals(origSpread.get_id(), BubbleDocs.getInstance()
				.getSpreadSheetByID(_docId2).get_id());
		assertEquals(origSpread.get_owner(), BubbleDocs.getInstance()
				.getSpreadSheetByID(_docId2).get_owner());
		assertEquals(origSpread.get_name(), BubbleDocs.getInstance()
				.getSpreadSheetByID(_docId2).get_name());
		assertEquals(origSpread.get_line(), BubbleDocs.getInstance()
				.getSpreadSheetByID(_docId2).get_line());
		assertEquals(origSpread.get_column(), BubbleDocs.getInstance()
				.getSpreadSheetByID(_docId2).get_column());
		
		for (int i = 0; i < origSpread.get_line(); i++) {
			for (int j = 0; j < origSpread.get_column(); j++) {
				try {
					if (origSpread.getCellByLinCol(i, j) != null
							&& origSpread.getCellByLinCol(i, j).getContent() != null) {

						assertEquals(origSpread.getCellByLinCol(i, j).getContent()
								.getClass(), BubbleDocs.getInstance()
								.getSpreadSheetByID(_docId2).getCellByLinCol(i, j)
								.getContent().getClass());

						assertEquals(origSpread.getCellByLinCol(i, j).getContent()
								.getValue(), BubbleDocs.getInstance()
								.getSpreadSheetByID(_docId2).getCellByLinCol(i, j)
								.getContent().getValue());

					}
				} catch (CellDoesNotExistException e) {
					
				}
			}
		}
	}
	
	@Test(expected = UnauthorizedOperationException.class)
	public void InvalidUser() throws BubbleException {
		User u = new User("ruteS", "Rute", "hi");
		BubbleDocs.getInstance().addUser(u);
		SpreadSheet s = new SpreadSheet("folha da rute", 1, 2, u.get_username());
		BubbleDocs.getInstance().addSpreadsheet(s);
		int docId = s.get_id();

		ImportDocument service = new ImportDocument(u.get_username(), docId);
		service.execute();
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void SheetDoesNotExist() throws BubbleException{
		
		BubbleDocs bd = BubbleDocs.getInstance();
		User u = new User("mariah", "Maria", "hello");
		bd.addUser(u);

		BubbleDocs.getInstance().addUser(u);
		
		LoginUser log = new LoginUser(u.get_username(), u.get_password());;
		log.execute();
				
		ImportDocument service = new ImportDocument(u.get_username(), -1);
		service.execute();
		
	}
	
	@Test(expected = UnauthorizedOperationException.class)
	public void UserDoesNotExist() throws BubbleException {
		ImportDocument service = new ImportDocument("olga", _docId);
		service.execute();
	}

}
