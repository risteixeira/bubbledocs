package pt.ulisboa.tecnico.sdis.store.ws;

public class Document {
	private String _owner;
	private String _docId;
	private byte[] _content;
	private Tag tag = new Tag();
	
	public Document(String owner,String docId,byte[] content,int cliente){
		this._owner = owner;
		this._docId = docId;
		this._content = content;
		tag.setCid(cliente);
		tag.updateSeq();
	}
	
	public void changeTag(Tag _tag){
		this.tag = _tag;
		
	}
	public String get_owner() {
		return _owner;
	}
	
	public String get_docId() {
		return _docId;
	}

	public byte[] get_content() {
		return _content;
	}
	
	public void set_content(byte[] content) {
		 this._content = content;
	}
	
	public Tag get_tag(){
		return this.tag;
	}
	
}
