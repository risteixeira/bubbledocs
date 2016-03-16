package pt.ulisboa.tecnico.bubbledocs.test.integration.system;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.ulisboa.tecnico.bubbledocs.domain.*;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbleException;
import pt.ulisboa.tecnico.bubbledocs.integration.*;
import pt.ulisboa.tecnico.bubbledocs.service.BubbleDocsService;
import pt.ulisboa.tecnico.bubbledocs.service.ImportDocument;

public class LocalSystemIT {

	/* Criacao do root */

	@Test
	public void localSystemIT(@Mocked final IDRemoteServices idRemote,
			@Mocked final StoreRemoteServices storeRemote) {

		boolean isCommited = false;
		TransactionManager tm = FenixFramework.getTransactionManager();

		try {
			tm.begin();

			// Inicialização do super user e da sessão
			BubbleDocsService.getBubbledocs();
			Session sessions = BubbleDocsService.getSession();
			String userToken = null;
			try {
				new User("root", "SuperUser", "root@superUser", "rootroot");
				sessions.addUserToSession("root");
				userToken = sessions.getTokenByUsername("root");
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			// Criação de novos utilizadores

			try {
				new Expectations() {
					{
						idRemote.createUser("marques", "pedro@tecnico");
					}
				};

				CreateUserIntegrator marques = new CreateUserIntegrator(
						userToken, "marques", "pedro@tecnico", "pedro");
				marques.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				new Expectations() {
					{
						idRemote.createUser("almeida", "bruno@tecnico");
					}
				};
				CreateUserIntegrator almeida = new CreateUserIntegrator(
						userToken, "almeida", "bruno@tecnico", "bruno");
				almeida.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				new Expectations() {
					{
						idRemote.createUser("moncovio", "joao@tecnico");
					}
				};
				CreateUserIntegrator moncovio = new CreateUserIntegrator(
						userToken, "moncovio", "joao@tecnico", "joao");
				moncovio.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				new Expectations() {
					{
						idRemote.createUser("semiao", "rita@tecnico");
					}
				};
				CreateUserIntegrator semiao = new CreateUserIntegrator(
						userToken, "semiao", "rita@tecnico", "rita");
				semiao.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			// Login de utilizadores

			try {
				new Expectations() {
					{
						idRemote.loginUser("marques", "marques1");
					}
				};
				LoginUserIntegrator loginMarques = new LoginUserIntegrator(
						"marques", "marques1");
				loginMarques.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				new Expectations() {
					{
						idRemote.loginUser("almeida", "almeida2");
					}
				};
				LoginUserIntegrator loginAlmeida = new LoginUserIntegrator(
						"almeida", "almeida2");
				loginAlmeida.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				new Expectations() {
					{
						idRemote.loginUser("moncovio", "moncovio3");
					}
				};
				LoginUserIntegrator loginMoncovio = new LoginUserIntegrator(
						"moncovio", "moncovio3");
				loginMoncovio.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				new Expectations() {
					{
						idRemote.removeUser("moncovio");
					}
				};
				DeleteUserIntegrator deleteMoncovio = new DeleteUserIntegrator(
						userToken, "moncovio");
				deleteMoncovio.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			String marquesToken = null;
			String almeidaToken = null;
			String semiaoToken = null;
			try {
				marquesToken = sessions.getTokenByUsername("marques");
				almeidaToken = sessions.getTokenByUsername("almeida");
				semiaoToken = sessions.getTokenByUsername("semiao");
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			// Criação de folhas de cálculo

			CreateSpreadSheetIntegrator ssMarques = null;
			try {
				ssMarques = new CreateSpreadSheetIntegrator(marquesToken,
						"Notas", 40, 40);
				ssMarques.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			CreateSpreadSheetIntegrator ssMarques2 = null;
			try {
				ssMarques2 = new CreateSpreadSheetIntegrator(marquesToken,
						"Pautas", 55, 50);
				ssMarques.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			CreateSpreadSheetIntegrator ssMarques3 = null;
			try {
				ssMarques3 = new CreateSpreadSheetIntegrator(marquesToken,
						"Ambiguidades", 10, 25);
				ssMarques.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			CreateSpreadSheetIntegrator ssAlmeida;

			try {
				ssAlmeida = new CreateSpreadSheetIntegrator(almeidaToken,
						"Calculadora", -2, -2);
				ssAlmeida.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			CreateSpreadSheetIntegrator ssAlmeida2 = null;
			try {
				ssAlmeida2 = new CreateSpreadSheetIntegrator(almeidaToken,
						"Excel", 10, 10);
				ssAlmeida2.execute();
			} catch (BubbleException e2) {
				System.out.println(e2.getMessage());
			}

			try {
				CreateSpreadSheetIntegrator ssSemiao = new CreateSpreadSheetIntegrator(
						semiaoToken, "Gatos", 100, 100);
				ssSemiao.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			Integer ssMarquesId = ssMarques.getDocID();
			Integer ssMarques2Id = ssMarques2.getDocID();
			Integer ssMarques3Id = ssMarques3.getDocID();
			Integer ssAlmeida2Id = ssAlmeida2.getDocID();

			// Atribuição de conteúdos às folhas

			// Literais
			try {
				AssignLiteralCellIntegrator l1 = new AssignLiteralCellIntegrator(
						almeidaToken, ssAlmeida2Id, "1;1", "2");
				l1.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignLiteralCellIntegrator l2 = new AssignLiteralCellIntegrator(
						almeidaToken, ssAlmeida2Id, "2;2", "4");
				l2.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignLiteralCellIntegrator l3 = new AssignLiteralCellIntegrator(
						marquesToken, ssMarquesId, "4;4", "6");
				l3.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignLiteralCellIntegrator l4 = new AssignLiteralCellIntegrator(
						marquesToken, ssMarquesId, "3;1", "15");
				l4.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignLiteralCellIntegrator l5 = new AssignLiteralCellIntegrator(
						almeidaToken, ssAlmeida2Id, "20;10", "50");
				l5.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignLiteralCellIntegrator l6 = new AssignLiteralCellIntegrator(
						marquesToken, ssMarquesId, "3;10", "100");
				l6.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignLiteralCellIntegrator l7 = new AssignLiteralCellIntegrator(
						marquesToken, ssMarquesId, "4;5", "0");
				l7.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignLiteralCellIntegrator l8 = new AssignLiteralCellIntegrator(
						almeidaToken, ssAlmeida2Id, "13;16", "-40");
				l8.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignLiteralCellIntegrator l9 = new AssignLiteralCellIntegrator(
						marquesToken, ssMarquesId, "5;1", "-100");
				l9.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignLiteralCellIntegrator l10 = new AssignLiteralCellIntegrator(
						marquesToken, ssMarquesId, "9;50", "40");
				l10.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}
			
			try {
				AssignLiteralCellIntegrator l11 = new AssignLiteralCellIntegrator(
						almeidaToken, ssAlmeida2Id, "10;10", "50");
				l11.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			// Referencias
			try {
				AssignReferenceCellIntegrator r1 = new AssignReferenceCellIntegrator(
						almeidaToken, ssAlmeida2Id, "1;2", "1;1");
				r1.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignReferenceCellIntegrator r2 = new AssignReferenceCellIntegrator(
						almeidaToken, ssAlmeida2Id, "2;1", "2;2");
				r2.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignReferenceCellIntegrator r3 = new AssignReferenceCellIntegrator(
						marquesToken, ssMarques2Id, "4;6", "4;4");
				r3.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignReferenceCellIntegrator r4 = new AssignReferenceCellIntegrator(
						marquesToken, ssMarques2Id, "3;1", "3;10");
				r4.execute();
			} catch (BubbleException e1) {
				System.out.println(e1.getMessage());
			}

			try {
				AssignReferenceCellIntegrator r5 = new AssignReferenceCellIntegrator(
						almeidaToken, ssAlmeida2Id, "20;10", "1;1");
				r5.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignReferenceCellIntegrator r6 = new AssignReferenceCellIntegrator(
						marquesToken, ssAlmeida2Id, "11;11", "8;8");
				r6.execute();
			} catch (BubbleException e1) {
				System.out.println(e1.getMessage());
			}

			try {
				AssignReferenceCellIntegrator r7 = new AssignReferenceCellIntegrator(
						marquesToken, ssMarques2Id, "4;5", "5;1");
				r7.execute();
			} catch (BubbleException e1) {
				System.out.println(e1.getMessage());
			}

			try {
				AssignReferenceCellIntegrator r8 = new AssignReferenceCellIntegrator(
						marquesToken, ssMarques2Id, "13;16", "-40;3");
				r8.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignReferenceCellIntegrator r9 = new AssignReferenceCellIntegrator(
						marquesToken, ssMarques2Id, "5;1", "-100;40");
				r9.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignReferenceCellIntegrator r10 = new AssignReferenceCellIntegrator(
						marquesToken, ssMarques2Id, "9;10", "13;16");
				r10.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			// Function

			try {
				AssignBinaryFunctionCellIntegrator f1 = new AssignBinaryFunctionCellIntegrator(
						almeidaToken, ssAlmeida2Id, "6;1", "Div(4,0)");
				f1.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignBinaryFunctionCellIntegrator f2 = new AssignBinaryFunctionCellIntegrator(
						almeidaToken, ssAlmeida2Id, "3;2", "Div(5,10)");
				f2.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignBinaryFunctionCellIntegrator f3 = new AssignBinaryFunctionCellIntegrator(
						marquesToken, ssMarques3Id, "4;4", "Add(6,100)");
				f3.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignBinaryFunctionCellIntegrator f4 = new AssignBinaryFunctionCellIntegrator(
						marquesToken, ssMarques3Id, "3;1", "Mul(15,5)");
				f4.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignBinaryFunctionCellIntegrator f5 = new AssignBinaryFunctionCellIntegrator(
						almeidaToken, ssMarques2Id, "20;10", "Sub(50,10)");
				f5.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignBinaryFunctionCellIntegrator f6 = new AssignBinaryFunctionCellIntegrator(
						marquesToken, ssAlmeida2Id, "3;11", "Div(100,4)");
				f6.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignBinaryFunctionCellIntegrator f7 = new AssignBinaryFunctionCellIntegrator(
						marquesToken, ssAlmeida2Id, "4;5", "Sub(0,2)");
				f7.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignBinaryFunctionCellIntegrator f8 = new AssignBinaryFunctionCellIntegrator(
						almeidaToken, ssMarques3Id, "13;16", "Add(-40:3)");
				f8.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignBinaryFunctionCellIntegrator f9 = new AssignBinaryFunctionCellIntegrator(
						almeidaToken, ssMarquesId, "5;3", "Sub(-100:40)");
				f9.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				AssignBinaryFunctionCellIntegrator f10 = new AssignBinaryFunctionCellIntegrator(
						almeidaToken, ssMarques3Id, "10;25", "div(40:40)");
				f10.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				GetSpreadSheetContentIntegrator getSSContent = new GetSpreadSheetContentIntegrator(
						userToken, ssAlmeida2Id);
				getSSContent.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			byte[] byteAux = null;
			
			try {
				new Expectations() {{
						storeRemote.storeDocument("marques", "Notas",
								(byte[]) any);
					}
				};
				
				ExportDocumentIntegrator exportDocument = new ExportDocumentIntegrator(
						marquesToken, ssMarques3Id);
				exportDocument.execute();
				byteAux = exportDocument.getDoc();
				
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}
			
			final byte[] byteAuxFinal = byteAux;

			try {
				new Expectations() {
					{
						
						storeRemote.loadDocument("marques", "Notas");
						result = byteAuxFinal;
					}
				};
				
				ImportDocumentIntegrator importDocument = new ImportDocumentIntegrator(
						marquesToken, ssMarques3Id);
				importDocument.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				new Expectations() {
					{
						idRemote.renewPassword("almeida");
					}
				};
				RenewPasswordIntegrator renewPassAlmeida = new RenewPasswordIntegrator(
						almeidaToken);
				renewPassAlmeida.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				new Expectations() {
					{
						idRemote.removeUser("almeida");
					}
				};
				DeleteUserIntegrator deleteAlmeida = new DeleteUserIntegrator(
						userToken, "almeida");
				deleteAlmeida.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				new Expectations() {
					{
						idRemote.removeUser("marques");
					}
				};
				DeleteUserIntegrator deleteMarques = new DeleteUserIntegrator(
						userToken, "marques");
				deleteMarques.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}

			try {
				DeleteUserIntegrator deleteSemiao = new DeleteUserIntegrator(
						userToken, "semiao");
				deleteSemiao.execute();
			} catch (BubbleException e) {
				System.out.println(e.getMessage());
			}
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

}
