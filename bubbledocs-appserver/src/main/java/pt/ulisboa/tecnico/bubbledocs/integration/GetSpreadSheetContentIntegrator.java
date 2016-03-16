package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.service.GetSpreadSheetContent;

public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator {
	private String userToken;
	int sheetId;
	String[][] matrix;

	public GetSpreadSheetContentIntegrator(String userToken, int sheetId) {
		this.userToken = userToken;
		this.sheetId = sheetId;
	}

	@Override
	protected void dispatch() throws BubbleException {

		// GetUsername4TokenService urs = new
		// GetUsername4TokenService(userToken);
		GetSpreadSheetContent getSSContent = new GetSpreadSheetContent(
				userToken, sheetId);
		getSSContent.execute();
		matrix = getSSContent.GetSpreadSheetMatrix();
	}

	public String[][] getMatrix() {
		return matrix;
	}

}
