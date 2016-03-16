package pt.ulisboa.tecnico.bubbledocs.service;

import java.io.UnsupportedEncodingException;

import org.jdom2.output.XMLOutputter;

import pt.ulisboa.tecnico.bubbledocs.domain.Access;
import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class ExportDocument extends BubbleDocsService {
	private byte[] _docXML;
	private int _docId;
	private String _userToken;
	private String _docName;
	private String _username;

	public byte[] getDocXML() {
		return _docXML;
	}
	

	public ExportDocument(String userToken, int docId) throws BubbleException {
		this._userToken = userToken;
		this._docId = docId;
		this._docName = BubbleDocs.getInstance().getSpreadSheetByID(_docId)
				.get_name();
		this._username = _userToken.substring(0, _userToken.length() - 1);
	}

	@Override
	protected void dispatch() throws BubbleException {
		checkAuthorization();
		if (Session.getInstance().validUserSession(_userToken))
			Session.getInstance().updateTime(_userToken);
		else
			throw new UnauthorizedOperationException(_userToken);

		

		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		SpreadSheet s = null;

		s = getBubbledocs().getSpreadSheetByID(this._docId);

		jdomDoc.setRootElement(s.exportToXML());

		XMLOutputter xml = new XMLOutputter();
		try {
			this._docXML = xml.outputString(jdomDoc).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	protected final byte[] getResult() {
		return this._docXML;
	}

	@Override
	protected void checkAuthorization() throws BubbleException {
		String username = _userToken.substring(0, _userToken.length() - 1);
		
		Access userAccess = getBubbledocs().getSpreadSheetByID(_docId)
				.getAccessbyUser(
						_userToken.substring(0, _userToken.length() - 1));

		String sheetOwner = getBubbledocs().getSpreadSheetByID(_docId)
				.get_owner();
		
		if (!(username.equals("root")) && userAccess == null
				&& !sheetOwner.equals(username))
			throw new UnauthorizedOperationException(username);
	}
}
