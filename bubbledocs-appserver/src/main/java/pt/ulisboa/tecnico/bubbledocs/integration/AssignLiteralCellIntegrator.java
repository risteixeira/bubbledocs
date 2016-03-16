package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralCell;

public class AssignLiteralCellIntegrator extends BubbleDocsIntegrator {
	private String accessUsername;
	private int docId;
	private String cellId;
	private String literal;
	private String result;

	public AssignLiteralCellIntegrator(String accessUsername, int docId,
			String cellId, String literal) {
		this.accessUsername = accessUsername;
		this.docId = docId;
		this.cellId = cellId;
		this.literal = literal;
	}

	@Override
	protected void dispatch() throws BubbleException {

		AssignLiteralCell aLiteralCell = new AssignLiteralCell(
				accessUsername, docId, cellId, literal);
		aLiteralCell.execute();
		result = aLiteralCell.getResult();
	}
	
	public String getResult() {
		return result;
	}

}
