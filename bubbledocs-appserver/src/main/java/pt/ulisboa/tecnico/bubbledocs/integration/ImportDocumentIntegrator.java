package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.ImportDocument;

public class ImportDocumentIntegrator extends BubbleDocsIntegrator{
	private int _docId;
	private String _userToken;
	private String _username;
	private ImportDocument importD;
	private String docName;

	
	public ImportDocumentIntegrator(String userToken, int docId) throws BubbleException {

		this.importD = new ImportDocument(userToken, docId);
		this._docId = docId;
		this._userToken = userToken;
		this._username = _userToken.substring(0, _userToken.length() - 1);
	}

	@Override
	protected void dispatch() throws BubbleException {
		StoreRemoteServices remStore = new StoreRemoteServices();
		
		try {	
			this.docName = BubbleDocs.getInstance().getSpreadSheetByID(_docId).get_name();
			byte [] coise = remStore.loadDocument(_username, docName);
			//importD.setBytes(remStore.loadDocument(_username, docName));
			//importD.execute();
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		}  catch (CannotLoadDocumentException e) {
			throw e;
		}
		
	}

}
