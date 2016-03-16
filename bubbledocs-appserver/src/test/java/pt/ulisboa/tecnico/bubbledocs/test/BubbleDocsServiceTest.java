package pt.ulisboa.tecnico.bubbledocs.test;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ulisboa.tecnico.bubbledocs.domain.*;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.service.BubbleDocsService;

// add needed import declarations

public class BubbleDocsServiceTest {

    @Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
            populate4Test();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    // should redefine this method in the subclasses if it is needed to specify
    // some initial state
    
    public void populate4Test() throws BubbleException{
    	BubbleDocs db = BubbleDocsService.getBubbledocs();
    	
    	User u = new User("manel", "Manel", "123@tecnico");
    	
    	db.addUser(u);
    	
    	SpreadSheet s = new SpreadSheet("Folha do Manel", 10, 10, u.get_username());
    	
    	db.addSpreadsheet(s);
    	
    	Cell c1 = new Cell(3, 4);
		Cell c2 = new Cell(1, 1);
		Cell c3 = new Cell(5, 6);

		Literal l = new Literal(5);
		c1.setContent(l);

		Reference r = new Reference(c1);
		c2.setContent(r);
		
		s.addCell(c1);
		s.addCell(c2);
		s.addCell(c3);
    }

    // auxiliary methods that access the domain layer and are needed in the test classes
    // for defining the iniital state and checking that the service has the expected behavior
    
    public User createUser(String username, String name, String email) throws BubbleException {
    	
    	User u = new User(username, name, email);
    	BubbleDocsService.getBubbledocs().createUser(u);
    	return u;
    }

    public SpreadSheet createSpreadSheet(User user, String name, int row,
            int column) throws BubbleException {
    	
    	SpreadSheet s = new SpreadSheet(name, row, column,user.get_username());
    	user.addSpreadsheet(s);
    	
    	for(int i = 1; i <= row; i++){
			for(int j = 1; j <= column; j++){
				Cell c = new Cell(i, j);
				s.addCell(c);
			}
		}
    	
    	return s;
    }

    // returns a spreadsheet whose name is equal to name
	public SpreadSheet getSpreadSheet(String name)
			throws InvalidUsernameException, EmptyUsernameException,
			SpreadsheetDoesNotExistException {
	   
	   String username = "manel";
	   User user = BubbleDocsService.getBubbledocs().getUserByUsername(username);
	   SpreadSheet sheet = (SpreadSheet) user.getSpreadSheetsByName(name).toArray()[0];
	   return sheet;
	   
    }

 
    // returns the user registered in the application whose username is equal to username
    public User getUserFromUsername(String username) throws BubbleException {
    	return BubbleDocsService.getBubbledocs().getUserByUsername(username);
    }

    
    // put a user into session and returns the token associated to it
    public String addUserToSession(String username) throws BubbleException {
    	return BubbleDocsService.getSession().addUserToSession(username);
    }

    
    // remove a user from session given its token
    public void removeUserFromSession(String token) {
    	BubbleDocsService.getSession().removeUserFromSession(token);
    }
    

    // return the user registered in session whose token is equal to token
    public User getUserFromSession(String token) throws BubbleException {	
    	return BubbleDocsService.getSession().getUserFromSession(token);
    }
    
}
