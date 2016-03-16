package pt.ulisboa.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CellDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.service.ExportDocument;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

public class ExportDocumentTest extends BubbleDocsServiceTest {
	String _userToken;
	int _docId;

	public void populate4Test() throws BubbleException {
		super.populate4Test();
		_userToken = Session.getInstance().addUserToSession("manel");
		_docId = super.getSpreadSheet("Folha do Manel").get_id();
	}

	@Test
	public void success() throws BubbleException {

		LoginUser login = new LoginUser("manel", "123");
		login.execute();

		_userToken = Session.getInstance().getTokenByUsername("manel");

		SpreadSheet origSpread = BubbleDocs.getInstance().getSpreadSheetByID(
				_docId);

		ExportDocument service = new ExportDocument(_userToken, _docId);
		service.execute();

		BubbleDocs.getInstance().removeSpreadsheet(origSpread);

		try {
			Reader xml = new StringReader(new String(service.getDocXML(), "UTF-8"));
			SAXBuilder sa = new SAXBuilder();
			Document doc = sa.build(xml);
			Element elem = doc.getRootElement();
			BubbleDocs.getInstance().importFromXML(elem, "manel");
			
			assertEquals(origSpread.get_owner(), BubbleDocs.getInstance()
					.getSpreadSheetByID(_docId+1).get_owner());
			assertEquals(origSpread.get_name(), BubbleDocs.getInstance()
					.getSpreadSheetByID(_docId+1).get_name());
			assertEquals(origSpread.get_line(), BubbleDocs.getInstance()
					.getSpreadSheetByID(_docId+1).get_line());
			assertEquals(origSpread.get_column(), BubbleDocs.getInstance()
					.getSpreadSheetByID(_docId+1).get_column());

			for (int i = 0; i < origSpread.get_line(); i++) {
				for (int j = 0; j < origSpread.get_column(); j++) {
					try {
						if (origSpread.getCellByLinCol(i, j) != null
								&& origSpread.getCellByLinCol(i, j).getContent() != null) {

							assertEquals(origSpread.getCellByLinCol(i, j).getContent()
									.getClass(), BubbleDocs.getInstance()
									.getSpreadSheetByID(_docId+1).getCellByLinCol(i, j)
									.getContent().getClass());

							assertEquals(origSpread.getCellByLinCol(i, j).getContent()
									.getValue(), BubbleDocs.getInstance()
									.getSpreadSheetByID(_docId+1).getCellByLinCol(i, j)
									.getContent().getValue());

						}
					} catch (CellDoesNotExistException e) {
						continue;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidContentException e) {
			e.printStackTrace();
		}

	}

	@Test(expected = UnauthorizedOperationException.class)
	public void testExportAccess() throws BubbleException {

		User u = new User("mariah", "Maria", "hello@tecnico");

		BubbleDocs.getInstance().addUser(u);

		LoginUser log = null;
		log = new LoginUser(u.get_username(), u.get_password());
		log.execute();

		Session.getInstance().getTokenByUsername(
				u.get_username());

		ExportDocument service = new ExportDocument(u.get_username(), _docId);
		service.execute();

	}
}
