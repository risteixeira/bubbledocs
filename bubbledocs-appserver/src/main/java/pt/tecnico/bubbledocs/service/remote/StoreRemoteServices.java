package pt.tecnico.bubbledocs.service.remote;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.FrontEnd;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.Store;
import pt.ulisboa.tecnico.sdis.store.ws.StoreClient;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.handler.IDClientHandler;

public class StoreRemoteServices {
	
	
    public void storeDocument(String username, String docName, byte[] document)
           throws CannotStoreDocumentException, RemoteInvocationException {
    	System.out.println("estou no storeDocument do store remote");
    	DocUserPair d = new DocUserPair();
    	
    	//SD-ID Handlers
//    	SDStore_Service service = new SDStore_Service();
//    	SDStore port = service.getSDStoreImplPort();
//    	
//    	BindingProvider bindingProvider = (BindingProvider) port;
//        Map<String, Object> requestContext = bindingProvider.getRequestContext();
//        
//        requestContext.put(IDClientHandler.REQUEST_PROPERTY, username);
        ////
    	
    	d.setDocumentId(docName);
    	d.setUserId(username);
    	System.out.println("foda-se");
    	try {
			//StoreClient client = new StoreClient();
    		FrontEnd client = new FrontEnd(null,"SD-STORE","http://localhost:8081");
    		System.out.println("e isto???");
			try {
				//client.createDoc(d);
				System.out.println(username + "!!!" + docName + "!!!" + document);
				client.store(d, document);
			} catch (CapacityExceeded_Exception | DocDoesNotExist_Exception
					| UserDoesNotExist_Exception e) {
				throw new CannotStoreDocumentException();
			}
		} catch (Exception e) {
			throw new RemoteInvocationException();
		}

    }
    public byte[] loadDocument(String username, String docName)
           throws CannotLoadDocumentException, RemoteInvocationException {
    	DocUserPair d = new DocUserPair();
    	d.setDocumentId(docName);
    	d.setUserId(username);
    	
    	//SD-ID Handler
//    	SDStore_Service service = new SDStore_Service();
//    	SDStore port = service.getSDStoreImplPort();
//    	
//    	BindingProvider bindingProvider = (BindingProvider) port;
//        Map<String, Object> requestContext = bindingProvider.getRequestContext();
//        
//        requestContext.put(IDClientHandler.REQUEST_PROPERTY, username);
        ////
        
        
			try {
				//StoreClient client = new StoreClient();
				FrontEnd client = new FrontEnd(null,"SD-STORE","http://localhost:8081");
				try {
					client.load(d);
				} catch (DocDoesNotExist_Exception | UserDoesNotExist_Exception e) {
					throw new CannotLoadDocumentException();
					
				}
				
			} catch (Exception e) {
				throw new RemoteInvocationException();
			}
			return null;
    } 
    
}
