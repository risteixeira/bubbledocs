package pt.ulisboa.tecnico.bubbledocs.test.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.BubbleDocsService;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.StoreClient;

public class ExportDocumentIT extends BubbleDocsServiceIT {
	private String _username = "alice";
	private String _userToken1;
	private String _username1 = "bruno";
	int _docId;
	int _docId2;
	byte[] testbyte = { 1 };

	public void populate4Test() throws BubbleException {
		BubbleDocs bd = BubbleDocsService.getBubbledocs();

		User u1 = new User(_username1, "bruno", "123");
		bd.addUser(u1);

		LoginUserIntegrator login1 = new LoginUserIntegrator
				(_username1, "Bbb2");
		login1.execute();
		this._userToken1 = BubbleDocsService.getSession().getTokenByUsername(
				_username1);
		CreateSpreadSheet s1 = new CreateSpreadSheet(_userToken1, "b1", 1,
				1);
		s1.execute();
		_docId = s1.getSheetId();
	}

	@Test
	public void successItegrator() throws BubbleException {
		DocUserPair d = new DocUserPair();
		d.setDocumentId(BubbleDocsService.getBubbledocs()
				.getSpreadSheetByID(_docId).get_name());
		d.setUserId(_username);
		SpreadSheet origSpread = BubbleDocsService.getBubbledocs()
				.getSpreadSheetByID(_docId);

		try {
			StoreClient client = new StoreClient();
			try {
				client.createDoc(d);
			} catch (DocAlreadyExists_Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		ExportDocumentIntegrator xml1 = new ExportDocumentIntegrator(
				_userToken1, _docId);
		xml1.execute();

		ImportDocumentIntegrator service = new ImportDocumentIntegrator(
				_userToken1, _docId2);
		service.execute();

		assertEquals(origSpread.get_id(), BubbleDocsService.getBubbledocs()
				.getSpreadSheetByID(_docId2).get_id());
		assertEquals(origSpread.get_owner(), BubbleDocsService.getBubbledocs()
				.getSpreadSheetByID(_docId2).get_owner());
		assertEquals(origSpread.get_name(), BubbleDocsService.getBubbledocs()
				.getSpreadSheetByID(_docId2).get_name());
		assertEquals(origSpread.get_line(), BubbleDocsService.getBubbledocs()
				.getSpreadSheetByID(_docId2).get_line());
		assertEquals(origSpread.get_column(), BubbleDocsService.getBubbledocs()
				.getSpreadSheetByID(_docId2).get_column());

		for (int i = 0; i < origSpread.get_line(); i++) {
			for (int j = 0; j < origSpread.get_column(); j++) {
				try {
					if (origSpread.getCellByLinCol(i, j) != null
							&& origSpread.getCellByLinCol(i, j).getContent() != null) {

						assertEquals(origSpread.getCellByLinCol(i, j)
								.getContent().getClass(), BubbleDocsService
								.getBubbledocs().getSpreadSheetByID(_docId2)
								.getCellByLinCol(i, j).getContent().getClass());

						assertEquals(origSpread.getCellByLinCol(i, j)
								.getContent().getValue(), BubbleDocsService
								.getBubbledocs().getSpreadSheetByID(_docId2)
								.getCellByLinCol(i, j).getContent().getValue());
					}
				} catch (CellDoesNotExistException e) {
					continue;
				}
			}
		}
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void SheetDoesNotExist() throws BubbleException {

		ExportDocumentIntegrator service = new ExportDocumentIntegrator(
				_username1, -1);
		service.execute();
	}

	@Test(expected = UnauthorizedOperationException.class)
	public void UserDoesNotExist() throws BubbleException {

		ExportDocumentIntegrator service = new ExportDocumentIntegrator(
				_username, _docId2);
		service.execute();
	}
}
