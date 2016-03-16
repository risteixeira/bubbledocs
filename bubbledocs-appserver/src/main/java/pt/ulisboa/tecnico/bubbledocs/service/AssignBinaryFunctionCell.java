package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Add;
import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Content;
import pt.ulisboa.tecnico.bubbledocs.domain.Div;
import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Mul;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.Sub;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class AssignBinaryFunctionCell extends BubbleDocsService {

	private String _result;
	private String _userToken;
	private int _sheetId;
	private String _cellId;
	private String _binary;
	
	public AssignBinaryFunctionCell(String accessUsername, int docId, String cellId,
			String binary) {
		this._userToken = accessUsername;
		this._sheetId = docId;
		this._cellId = cellId;
		this._binary = binary;
	}
	
	@Override
	protected void dispatch() throws BubbleException{
		Session session = BubbleDocsService.getSession();
		BubbleDocs bd = BubbleDocsService.getBubbledocs();
		
		SpreadSheet ss = bd.getSpreadSheetByID(_sheetId);
		String[] parse = _cellId.split(";");
		int row = Integer.parseInt(parse[0]);
		int column = Integer.parseInt(parse[1]);
		String[] binaryParse = _binary.split("\\(|,|\\)");
		Content c1, c2;
		
		if(binaryParse[1].contains(";")){
			
			String[] refParse = binaryParse[1].split(";");
			int refRow = Integer.parseInt(refParse[0]);
			int refColumn = Integer.parseInt(refParse[1]);
			c1 = new Reference(refRow, refColumn);
		}
		else
			c1 = new Literal(Integer.parseInt(binaryParse[1]));
		
		if(binaryParse[2].contains(";")){
			String[] refParse = binaryParse[2].split(";");
			int refRow = Integer.parseInt(refParse[0]);
			int refColumn = Integer.parseInt(refParse[1]);
			c2 = new Reference(refRow, refColumn);
		}
		else
			c2 = new Literal(Integer.parseInt(binaryParse[2]));

		session.updateTime(_userToken);
		
		checkAuthorization();
		Cell c = ss.getCellByLinCol(row, column);
		c.cellProtection();
		
		if(binaryParse[0].equalsIgnoreCase("Add")){
			Add add = new Add(c1, c2);
			Integer aux = add.getValue();
			Literal result = new Literal(aux);
			c.setContent(result);
			_result = c.getContent().getValue().toString();
		}
		if(binaryParse[0].equalsIgnoreCase("Sub")){
			Sub sub = new Sub(c1, c2);
			Integer aux = sub.getValue();
			Literal result = new Literal(aux);
			c.setContent(result);
			_result = c.getContent().getValue().toString();
		}
		if(binaryParse[0].equalsIgnoreCase("Div")){
			Div div = new Div(c1, c2);
			Integer aux = div.getValue();
			Literal result = new Literal(aux);
			c.setContent(result);
			_result = c.getContent().getValue().toString();
		}
		if(binaryParse[0].equalsIgnoreCase("Mul")){
			Mul mul = new Mul(c1, c2);
			Integer aux = mul.getValue();
			Literal result = new Literal(aux);
			c.setContent(result);
			_result = c.getContent().getValue().toString();
		}
	}
	
	public String getResult() {
		return _result;
	}

	@Override
	protected void checkAuthorization() throws BubbleException {
		SpreadSheet ss = BubbleDocsService.getBubbledocs().getSpreadSheetByID(_sheetId);
		User user = BubbleDocsService.getSession().getUserFromSession(_userToken);

		if (!(_userToken.equals("root"))
				&& !(ss.get_owner().equals(user.get_username()))
				&& !(user.getAccessbySpreadsheet(_sheetId).get_permission()
						.equals("write")))
			throw new UnauthorizedOperationException(user.get_username());
	}
}
