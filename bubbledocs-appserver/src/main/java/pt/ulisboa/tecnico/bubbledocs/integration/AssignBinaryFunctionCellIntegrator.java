package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignBinaryFunctionCell;

public class AssignBinaryFunctionCellIntegrator extends BubbleDocsIntegrator {
	private String accessUsername;
	private int docId;
	private String cellId;
	private String binary;
	private String result;

	public AssignBinaryFunctionCellIntegrator(String accessUsername, int docId,
			String cellId, String binary) {
		this.accessUsername = accessUsername;
		this.docId = docId;
		this.cellId = cellId;
		this.binary = binary;
	}

	@Override
	protected void dispatch() throws BubbleException {

		AssignBinaryFunctionCell aBinaryFunction = new AssignBinaryFunctionCell(
				accessUsername, docId, cellId, binary);
		aBinaryFunction.execute();
		result = aBinaryFunction.getResult();

	}
	
	public String getResult() {
		return result;
	}

}
