package pt.ulisboa.tecnico.bubbledocs.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import mockit.Mocked;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotInSessionException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.ulisboa.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.service.GetSpreadSheetContent;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

public class GetSpreadSheetContentTest extends BubbleDocsServiceTest {

	@Mocked IDRemoteServices idRemote;

	private int _columns = 6;
	private int _lines = 6;
	private String _name = "joaoDoc";
	private String _user = "joao";
	private String _user1 = "artur";
	private String _userToken;
	private String _userToken1;
	private String _literal0 = "0";
	private String _literal1 = "1";
	private String _literal2 = "2";
	private Integer _sheetId;
	private Integer _sheetId1;
	private String[][] matrix = new String[_lines][_columns];

	@Override
	public void populate4Test() throws BubbleException {
		BubbleDocs bd = BubbleDocs.getInstance();

		User u = new User(_user, "Joao", "123@tecnico");
		bd.addUser(u);

		User u1 = new User(_user1, "Artur", "123@tecnico");
		bd.addUser(u1);

		LoginUser log = new LoginUser(_user, "123");
		log.execute();

		LoginUser log1 = new LoginUser(_user1, "123");
		log1.execute();

		Session session = Session.getInstance();
		this._userToken = session.getTokenByUsername(_user);
		this._userToken1 = session.getTokenByUsername(_user1);


		CreateSpreadSheet s1 = new CreateSpreadSheet(_userToken1, _name, _lines,
				_columns);
		s1.execute();
		_sheetId1 = s1.getSheetId();

		CreateSpreadSheet s = new CreateSpreadSheet(_userToken, _name, _lines,
				_columns);
		s.execute();
		_sheetId = s.getSheetId();	

		session.removeUserFromSession(_userToken1);

		SpreadSheet sheet = bd.getSpreadSheetByID(_sheetId);

		for (int i = 1; i < _lines; i++) {
			for (int j = 1; j < _columns; j++) {
				matrix[i-1][j-1] = " ";
			}
		}

		Cell cell0 = sheet.getCellByLinCol(1,1);
		String cell0Id = cell0.get_line() + ";" + cell0.get_column();
		matrix[0][0] = _literal0;

		Cell cell1 = sheet.getCellByLinCol(3,4);
		String cell1Id = cell1.get_line() + ";" + cell1.get_column();
		matrix[2][3] = _literal1;

		Cell cell2 = sheet.getCellByLinCol(4,5);
		String cell2Id = cell2.get_line() + ";" + cell2.get_column();
		matrix[3][4] = _literal2;

		Cell cell3 = sheet.getCellByLinCol(2,5);
		String cell3Id = cell3.get_line() + ";" + cell3.get_column();
		matrix[1][4] = _literal0;

		Cell cell4 = sheet.getCellByLinCol(1,4);
		String cell4Id = cell4.get_line() + ";" + cell4.get_column();
		matrix[0][3] = cell3Id;

		Cell cell5 = sheet.getCellByLinCol(2,2);
		String cell5Id = cell5.get_line() + ";" + cell5.get_column();
		matrix[1][1] = cell4Id;



		AssignLiteralCell assign0 = new  AssignLiteralCell(_userToken , s.getSheetId(), cell0Id, _literal0);
		assign0.execute();

		AssignLiteralCell assign1 = new  AssignLiteralCell(_userToken , s.getSheetId(), cell1Id, _literal1);
		assign1.execute();

		AssignLiteralCell assign2 = new  AssignLiteralCell(_userToken , s.getSheetId(), cell2Id, _literal2);
		assign2.execute();

		AssignLiteralCell assign3 = new  AssignLiteralCell(_userToken , s.getSheetId(), cell3Id, _literal0);
		assign3.execute();

		AssignReferenceCell assign4 = new  AssignReferenceCell( _userToken, s.getSheetId(), cell4Id, cell3Id);
		assign4.execute();

		AssignReferenceCell assign5 = new  AssignReferenceCell( _userToken, s.getSheetId(), cell5Id, cell4Id);
		assign5.execute();


	}

	@Test
	public void success() throws BubbleException {

		LoginUser log = new LoginUser(_user, "123");
		log.execute();

		GetSpreadSheetContent getcontent = new GetSpreadSheetContent(_userToken, _sheetId);
		getcontent.execute();

		String[][] matrixAux = getcontent.GetSpreadSheetMatrix();

		for (int i = 0; i < _lines-1; i++) {
			for (int j = 0; j < _columns-1; j++) {
				assertEquals(matrix[i][j],matrixAux[i][j]);
			}
		}		
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void SpreadsheetDoesNotExistException() throws BubbleException {
		GetSpreadSheetContent service = new GetSpreadSheetContent(_userToken, 75376);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void UserNoSession() throws BubbleException {
		GetSpreadSheetContent service = new GetSpreadSheetContent(_userToken1, _sheetId1);
		service.execute();
	}	

	@Test(expected = UnauthorizedOperationException.class)
	public void UserDoesNotExists() throws BubbleException {
		GetSpreadSheetContent service = new GetSpreadSheetContent("manel1", _sheetId1);
		service.execute();
	}
}
