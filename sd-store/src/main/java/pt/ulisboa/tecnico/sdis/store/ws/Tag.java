package pt.ulisboa.tecnico.sdis.store.ws;

public class Tag {
	private int seq;
	private int cid;
	

	
	public Tag(int parseInt, int parseInt2) {
		this.seq = parseInt;
		this.cid = parseInt2;
	}

	public Tag() {
	}

	public void updateSeq(){
		this.seq = seq ++;
	}

	public int getCid() {
		return cid;
	}

	public int getseq() {
		return seq;
	}


	public void setCid(int cid) {
		this.cid = cid;
	}
}
