package pt.ulisboa.tecnico.bubbledocs.integration;

import java.io.UnsupportedEncodingException;

import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.ExportDocument;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {
	private int _docId;
	private String _docName;
	private String _username;
	private byte[] _bytes;
	private ExportDocument exportD;

	public ExportDocumentIntegrator(String userToken, int docId)
			throws BubbleException {
		exportD = new ExportDocument(userToken, docId);
		this._docId = docId;
		this._docName = BubbleDocs.getInstance().getSpreadSheetByID(_docId)
				.get_name();
		this._username = userToken.substring(0, userToken.length() - 1);
	}

	@Override
	protected void dispatch() throws BubbleException {
		StoreRemoteServices remStore = new StoreRemoteServices();
		exportD.execute();
		this._bytes = exportD.getDocXML();

		try {
			remStore.storeDocument(_username, _docName, _bytes);
		} catch (RemoteInvocationException e) {
			throw new UnavailableServiceException();
		} catch (CannotStoreDocumentException e) {
			throw e;
		}
	}

	public byte[] getDoc() {
		return _bytes;
	}
}
