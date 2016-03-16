package pt.ulisboa.tecnico.sdis.store.ws;
import java.util.ArrayList;
import java.util.List;


public class Repository {
	private String _owner;
	//private int _capacity = 0;
	//private int _maxcapacity = 10485760;
	private ArrayList<Document> _documents = new ArrayList<Document>();
	

	public Repository(String _userid) {
		this._owner = _userid;
	}

	public void addDocument(String _docId,byte[] _doc){
		//TEMOS QUE VER O NUMERO DO CLIENTE!!!!
		Document doc = new Document(_owner, _docId, _doc,0);
		_documents.add(doc);
	}

	public boolean docInRepository(String _docId){
		for (Document _doc : _documents) {
			if (_doc.get_docId().equals(_docId)) {
				return true;
			}
		}
		return false;
	}

	public String getowner(){
		return _owner;
	}

	//public int getContBytes(){
	//	return _capacity;
	//}

	public List<String> ListDocs(){
		List<String> docList = new ArrayList<String>();
		for (Document auxDoc : _documents){
			docList.add(auxDoc.get_docId());
		}
		return docList;	 
	}

	public void addContent(byte[] newContent, String doc) throws CapacityExceeded_Exception{
		//if(_capacity + newContent.length > _maxcapacity)
		//	throw new CapacityExceeded_Exception(_owner, null);

		for (Document _auxDoc : _documents){
			if(_auxDoc.get_docId().equals(doc))
				_auxDoc.set_content(newContent);
				_auxDoc.get_tag().updateSeq();
		}
		
	//	_capacity = _capacity + newContent.length;
	}

	public byte[] readDoc(String DocId){
		for (Document _auxDoc : _documents){
			if(_auxDoc.get_docId().equals(DocId))
				return _auxDoc.get_content();
		}
		return null;
	}
	
	public Document getDoc(String _docId){
		for (Document _doc : _documents) {
			if (_doc.get_docId().equals(_docId)) {
				return _doc;
			}
		}
		return null;
	}

}