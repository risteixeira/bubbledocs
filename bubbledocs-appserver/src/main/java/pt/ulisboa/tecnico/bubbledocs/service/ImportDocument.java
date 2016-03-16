package pt.ulisboa.tecnico.bubbledocs.service;

import java.io.IOException;
import java.io.StringReader;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pt.ulisboa.tecnico.bubbledocs.domain.Access;
import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class ImportDocument extends BubbleDocsService {
	private int _docId;
	private String _userToken;
	private String _username;
	private byte[] _bytes;// = {1};
	
	
	public void checkAuthorization() throws UnauthorizedOperationException, SpreadsheetDoesNotExistException{
		String username = _userToken.substring(0, _userToken.length() - 1);
		
		Access userAccess = BubbleDocs.getInstance().getSpreadSheetByID(_docId)
				.getAccessbyUser(
						_userToken.substring(0, _userToken.length() - 1));

		String sheetOwner = BubbleDocs.getInstance().getSpreadSheetByID(_docId)
				.get_owner();
		
		if (!(username.equals("root")) && userAccess == null
				&& !sheetOwner.equals(username))
			throw new UnauthorizedOperationException(username);
	}
	
	public ImportDocument(String userToken, int docId){
		_docId = docId;
		_userToken = userToken;
		this._username = _userToken.substring(0, _userToken.length() - 1);
	}
	
	public void setBytes (byte[] bytes) {
		_bytes = bytes;
	}
	
	@Override
	protected void dispatch() throws BubbleException {
		checkAuthorization();
		if (Session.getInstance().validUserSession(_userToken))
			Session.getInstance().updateTime(_userToken);
		else
			throw new UnauthorizedOperationException(_userToken);


        BubbleDocs.getInstance().getSpreadSheetByID(_docId);
        
        try {
            
            SAXBuilder builder = new SAXBuilder();
            
            org.jdom2.Document jdomDoc = builder.build(new StringReader(new String(_bytes, "UTF-8")));
        	Element rootElement = jdomDoc.getRootElement();
        	BubbleDocs.getInstance().importFromXML(rootElement, _username);
        	
        } catch (JDOMException | IOException e) {
        	e.printStackTrace();

        }


	}
}