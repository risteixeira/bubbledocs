package pt.ulisboa.tecnico.sdis.store.ws;

public class Tag {
	private int _seq;
	private int _cid;
	
	public Tag(int seq, int cid){
		this._seq = seq;
		this._cid = cid;
	}
	public void updateSeq(){
		this._seq = _seq ++;
	}

	public int getCid() {
		return _cid;
	}

	public int getseq() {
		return _seq;
	}


	public void setCid(int cid) {
		this._cid = cid;
	}
}
