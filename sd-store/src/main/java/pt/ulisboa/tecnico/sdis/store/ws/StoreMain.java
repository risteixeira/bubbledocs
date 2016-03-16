package pt.ulisboa.tecnico.sdis.store.ws;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.store.ws.uddi.UDDINaming;

//Main baseada no exemplo de webservices do laboratorio hello-ws_juddi

public class StoreMain {

	public static void main(String[] args) {

		if (args.length < 3) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s url%n", StoreMain.class.getName());
			return;
		}

		String uddiURL = args[0];
		String name = args[1];
		String url = args[2];
		
		DocUserPair _docpair1 = new DocUserPair();
		_docpair1.setUserId("alice");
		_docpair1.setDocumentId("a1");
		DocUserPair _docpair2 = new DocUserPair();
		_docpair2.setUserId("alice");
		_docpair2.setDocumentId("a2");
		DocUserPair _docpair3 = new DocUserPair();
		_docpair3.setUserId("bruno");
		_docpair3.setDocumentId("b1");
		
		System.out.println(name);
		System.out.println(uddiURL);
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
		
		StoreImpl storeimpl = new StoreImpl();
		try {
			storeimpl.createDoc(_docpair1);
			storeimpl.createDoc(_docpair2);
			storeimpl.createDoc(_docpair3);

		} catch (DocAlreadyExists_Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] iv = { 1, 1, 2, 3};
		try {
			storeimpl.changeTest(true);
			storeimpl.store(_docpair1, "AAAAAAAAAA".getBytes());
			storeimpl.store(_docpair2, "aaaaaaaaaa".getBytes());
			storeimpl.store(_docpair3,iv);
			storeimpl.changeTest(false);
			
		} catch (CapacityExceeded_Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DocDoesNotExist_Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UserDoesNotExist_Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
		try {
			endpoint = Endpoint.create(storeimpl);
			

			// publish endpoint
			System.out.printf("Starting %s%n", url);
			endpoint.publish(url);

			// publish to UDDI
			System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
			uddiNaming = new UDDINaming(uddiURL);
			uddiNaming.rebind(name, url);

			// wait
			System.out.println("Awaiting connections");
			System.out.println("Press enter to shutdown");
			System.in.read();

		} catch(Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} 

		finally {
			try {
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch(Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind(name);
					System.out.printf("Deleted '%s' from UDDI%n", name);
				}
			} catch(Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}
	}
}