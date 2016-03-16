package pt.ulisboa.tecnico.sdis.store.ws;
import javax.annotation.Resource;
import javax.jws.*;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import pt.ulisboa.tecnico.sdis.store.ws.handler.RelayServerHandler;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;

import java.util.ArrayList;
import java.util.List;


@WebService(
		endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore", 
		wsdlLocation="SD-STORE.1_1.wsdl",
		name="SdStore",
		portName="SDStoreImplPort",
		targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
		serviceName="SDStore"
		)
@HandlerChain(file="/handler-chain.xml")

public class StoreImpl implements SDStore {
	
    public static final String CLASS_NAME = StoreImpl.class.getSimpleName();
    public static final String TOKEN = "server";
    private boolean test = false;

	private List<Repository> _repository = new ArrayList<Repository>();
	
    @Resource
    private WebServiceContext webServiceContext;
	

	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {
		String _userid = docUserPair.getUserId();
		boolean _hasRep = false;
		Repository _newRepository = null; 

		for(Repository _r : _repository){
			if(_userid.equals(_r.getowner())){
				_hasRep = true;
				_newRepository = _r;
				break;	
			}
		}
		
		if(!_hasRep){
			Repository _newRep = new Repository(_userid);
			byte[] _newDoc = new byte[0];
			CreateDoc _docPair = new CreateDoc();
			_repository.add(_newRep);
			_docPair.setDocUserPair(docUserPair);
			_newRep.addDocument(_docPair.getDocUserPair().documentId, _newDoc);
		}
		
		else{
			if(_newRepository.docInRepository(docUserPair.documentId)){
				DocAlreadyExists _docExists = new DocAlreadyExists();
				_docExists.setDocId(docUserPair.documentId);
				throw new DocAlreadyExists_Exception(_userid, _docExists);
			}
			
			
			byte[] _newDoc = new byte[0];
			CreateDoc _docPair = new CreateDoc();
			_docPair.setDocUserPair(docUserPair);
			_newRepository.addDocument(_docPair.getDocUserPair().documentId, _newDoc);
		}

	}

	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		boolean _hasRep = false;
		Repository _newRepository = null; 

		for(Repository _r : _repository){
			if(userId.equals(_r.getowner())){
				_hasRep = true;
				_newRepository = _r;
				break;
			}
		}
		
		if(!_hasRep){
			UserDoesNotExist notuser = new UserDoesNotExist();
			notuser.setUserId(userId);
			throw new UserDoesNotExist_Exception(userId, notuser);
		}
		return _newRepository.ListDocs();
	}

	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		String _propertyValue = "null";
		if(test == false){
		MessageContext messageContext = webServiceContext.getMessageContext();
		
		 String propertyValue = (String) messageContext.get(RelayServerHandler.REQUEST_PROPERTY);
		 _propertyValue = propertyValue;
		 
		 System.out.println(propertyValue);
			 
		 if(!propertyValue.equals("null")){
			 
			 
			 String[] parse = propertyValue.split(";");
			Tag newTag = new Tag(Integer.parseInt(parse[0]),Integer.parseInt(parse[1]));
			 int j = 0;
			 for(Repository _r : _repository){
					if(docUserPair.getUserId().equals(_r.getowner())){
						 
						break;	
					}
					j++;
				}
			 _repository.get(j).getDoc(docUserPair.getDocumentId()).changeTag(newTag);
		 }
		 
		 Repository _newRepository = null; 
		 Tag tag = new Tag();
		 String user = docUserPair.getUserId();
		 
		 
		 for(Repository _r : _repository){
				if(user.equals(_r.getowner())){
					_newRepository = _r;
					break;	
				}
			}
		 tag = _newRepository.getDoc(docUserPair.getDocumentId()).get_tag();
		 String newString = tag.getseq() + ";" + tag.getCid();
		 
		 messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, newString);
		}
		 
		 
		boolean _hasRep = false;
		Repository _newRep = null; 
		String userId = docUserPair.getUserId();
		String docId = docUserPair.getDocumentId();

		for(Repository _r : _repository){
			if(userId.equals(_r.getowner())){
				_hasRep = true;
				_newRep = _r;
				break;
			}
		}
		
		if(!_hasRep){
			UserDoesNotExist notuser = new UserDoesNotExist();
			notuser.setUserId(userId);
			throw new UserDoesNotExist_Exception(userId, notuser);}

		if(!_newRep.docInRepository(docId)){
			DocDoesNotExist _docExists = new DocDoesNotExist();
			_docExists.setDocId(docId);
			throw new DocDoesNotExist_Exception(docId, _docExists);
		}
		if(!_propertyValue.equals("null"))
		_newRep.addContent(contents, docId);
	}

	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		
		if(test == false){
			MessageContext messageContext = webServiceContext.getMessageContext();
			
			 String propertyValue = (String) messageContext.get(RelayServerHandler.REQUEST_PROPERTY);
				 
			 Repository _newRepository = null; 
			 Tag tag = new Tag();
			 String user = docUserPair.getUserId();
			 
			 
			 for(Repository _r : _repository){
					if(user.equals(_r.getowner())){
						_newRepository = _r;
						break;	
					}
				}
			 tag = _newRepository.getDoc(docUserPair.getDocumentId()).get_tag();
			 String newString = tag.getseq() + ";" + tag.getCid();
			 
			 messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, newString);
			}

		boolean _hasRep = false;
		Repository _newRep = null; 
		String userId = docUserPair.getUserId();
		String docId = docUserPair.getDocumentId();
	    
		
		for(Repository _r : _repository){
			if(userId.equals(_r.getowner())){
				_hasRep = true;
				_newRep = _r;
				break;
			}
		}
		
		if(!_hasRep){
			UserDoesNotExist notUser = new UserDoesNotExist();
			notUser.setUserId(userId);
			throw new UserDoesNotExist_Exception(userId, notUser);
		}

		if(!_newRep.docInRepository(docId)){
			DocDoesNotExist _docExists = new DocDoesNotExist();
			_docExists.setDocId(docId);
			throw new DocDoesNotExist_Exception(docId, _docExists);
		}

		return _newRep.readDoc(docId);
	}
	public void changeTest(boolean bool){
		this.test = bool;
	}

}