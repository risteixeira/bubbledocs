package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadSheet;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {
	private String userToken;
	private String name;
	int lines;
	int columns;
	int docId;

	public CreateSpreadSheetIntegrator(String userToken, String name,
			int lines, int columns) {
		this.userToken = userToken;
		this.name = name;
		this.lines = lines;
		this.columns = columns;
	}

	@Override
	protected void dispatch() throws BubbleException {

		CreateSpreadSheet creadSS = new CreateSpreadSheet(userToken, name,
				lines, columns);
		creadSS.execute();
		this.docId = creadSS.getSheetId();

	}
	
	public int getDocID(){
		return docId;
	}

}

