package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignReferenceCell;

public class AssignReferenceCellIntegrator extends BubbleDocsIntegrator {
	private String tokenUser;
	private int docId;
	private String cellId;
	private String reference;
	private String result;

	public AssignReferenceCellIntegrator(String tokenUser, int docId,
			String cellId, String reference) {
		this.tokenUser = tokenUser;
		this.docId = docId;
		this.cellId = cellId;
		this.reference = reference;

	}

	@Override
	protected void dispatch() throws BubbleException {

		AssignReferenceCell aRefCell = new AssignReferenceCell(tokenUser,
				docId, cellId, reference);
		aRefCell.execute();
		result = aRefCell.getResult();

	}
	
	public String getResult() {
		return result;
	}

}
