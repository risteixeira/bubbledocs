package pt.ulisboa.tecnico.bubbledocs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.ulisboa.tecnico.bubbledocs.domain.Add;
import pt.ulisboa.tecnico.bubbledocs.domain.BubbleDocs;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Div;
import pt.ulisboa.tecnico.bubbledocs.domain.Function;
import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.SpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleDividedByZeroException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSpreadsheetException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.ulisboa.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.ulisboa.tecnico.bubbledocs.service.BubbleDocsService;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUser;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUser;

public class BubbleApplication {

	public static void main(String[] args) throws InvalidUsernameException,
			EmptyUsernameException, SpreadsheetDoesNotExistException,
			UnauthorizedOperationException, InvalidSpreadsheetException {

		boolean isCommited = false;
		try {
			populateDomain();
		} catch (IllegalStateException | SecurityException | SystemException
				| InvalidContentException e1) {
			e1.printStackTrace();
		}

		TransactionManager tm = FenixFramework.getTransactionManager();

		try {
			tm.begin();

			// BubbleDocs bd = BubbleDocs.getInstance();
			// User pf = bd.getUserByUsername("pf");
			//
			// printSpreadXML(pf);
			// SpreadSheet notasES = bd.getSpreadSheetByID(0);
			// listUserInfo();
			// listSheetInfo(pf);
			//
			// SAXBuilder sa = new SAXBuilder();
			// FileInputStream fd = new FileInputStream("notasES.xml");
			// Document doc = sa.build(fd);
			// Element elem = doc.getRootElement();
			// try {
			// bd.importFromXML(elem, pf.get_username());
			// } catch (InvalidContentException e) {
			// e.printStackTrace();
			// }
			//
			// bd.removeSpreadsheet(notasES);
			// notasES.delete();

			tm.commit();
			isCommited = true;

		} catch (NotSupportedException | SystemException | SecurityException
				| IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}

		finally {
			if (!isCommited) {
				try {
					tm.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e) {
					e.printStackTrace();
				}
			}
		}
		
		isCommited = false;
		
		try {
			tm.begin();


			tm.commit();
			isCommited = true;

		} catch (NotSupportedException | SystemException | SecurityException
				| IllegalStateException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException e) {
			e.printStackTrace();
		}

		finally {
			if (!isCommited) {
				try {
					tm.rollback();
				} catch (IllegalStateException | SecurityException
						| SystemException e) {
					e.printStackTrace();
				}
			}
		}

	}

	static void populateDomain() throws IllegalStateException,
			SecurityException, SystemException, InvalidContentException,
			EmptyUsernameException, InvalidUsernameException,
			UnauthorizedOperationException, InvalidSpreadsheetException {
		TransactionManager tm = FenixFramework.getTransactionManager();
		boolean isCommited = false;

		try {
			tm.begin();

			BubbleDocs bd = BubbleDocsService.getBubbledocs();
			Session sess = BubbleDocsService.getSession();

			User root = new User("root", "Super User", "");

			bd.addUser(root);
			sess.addUserToSession("root");
			String rootToken = sess.getTokenByUsername("root");

			new CreateUser(rootToken, "pf", "paul@door", "Paul Door").execute();
			new CreateUser(rootToken, "ra", "step@rabbit", "Step Rabbit")
					.execute();

			new LoginUser("pf", "sub").execute();
			String pfToken = sess.getTokenByUsername("pf");

			new CreateSpreadSheet(pfToken, "Notas ES", 300, 20).execute();
			SpreadSheet es = bd.getSpreadSheetByID(0);

			Cell c1 = new Cell(3, 4);
			Cell c2 = new Cell(1, 1);
			Cell c3 = new Cell(5, 6);
			Cell c4 = new Cell(2, 2);

			es.addCell(c1);
			es.addCell(c2);
			es.addCell(c3);
			es.addCell(c4);

			new AssignLiteralCell(pfToken, 0, "3;4", "5").execute();
			new AssignReferenceCell(pfToken, 0, "1;1", "5;6").execute();

			Literal l1 = new Literal(2);
			Reference r1 = new Reference(c1);
			Function f = new Add(l1, r1);
			c3.setContent(f);

			Reference r2 = new Reference(c2);
			Reference r3 = new Reference(c1);
			Function f1 = new Div(r2, r3);
			c4.setContent(f1);

			tm.commit();
			isCommited = true;

		} catch (SystemException | NotSupportedException | RollbackException
				| HeuristicMixedException | HeuristicRollbackException
				| BubbleException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			System.out.println("erro");
		} finally {
			if (!isCommited) {
				tm.rollback();
			}
		}

	}

	/* LIST USER & LIST SHEET INFO */
	@Atomic
	public static void listUserInfo() {

		BubbleDocs bd = BubbleDocs.getInstance();

		for (User user : bd.getUserSet()) {
			System.out.println("");
			System.out.println("User name:" + user.get_name());
			System.out.println("User username:" + user.get_username());
			System.out.println("User password:" + user.get_password());
			System.out.println("");
		}
	}

	public static void listSheetInfo(User u) {
		BubbleDocs bd = BubbleDocs.getInstance();

		for (SpreadSheet ss : bd.getSpreadsheetSet()) {
			if (ss.get_owner().equals(u.get_username())) {
				System.out.println("");
				System.out.println("SpreadSheet name:" + ss.get_name());
				System.out.println("SpreadSheet ID:" + ss.get_id());
				System.out.println("");
			}
		}
	}

	/* CONVERT XML */
	@Atomic
	public static org.jdom2.Document convertToXML(SpreadSheet ss)
			throws BubbleDividedByZeroException {

		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(ss.exportToXML());

		return jdomDoc;
	}

	public static void printDomainInXML(org.jdom2.Document jdomDoc)
			throws IOException {
		XMLOutputter out = new XMLOutputter();
		out.setFormat(Format.getPrettyFormat());
		File notasES = new File("notasES.xml");
		FileOutputStream stream = new FileOutputStream(notasES);
		out.output(jdomDoc, stream);
		System.out.println(out.outputString(jdomDoc));
	}

	public static org.jdom2.Document printSpreadXML(User owner)
			throws BubbleDividedByZeroException, IOException {

		BubbleDocs bd = BubbleDocs.getInstance();
		org.jdom2.Document doc = new org.jdom2.Document();

		for (SpreadSheet ss : bd.getSpreadsheetSet()) {
			if (ss.get_owner().equals(owner.get_username())) {

				doc = convertToXML(ss);
				printDomainInXML(doc);
			}

		}
		return doc;
	}

}