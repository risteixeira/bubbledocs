package pt.ulisboa.tecnico.sdis.store.ws.test;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.FrontEnd;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;

public class SDStoreMultipleServersTest {
	private static DocUserPair _docpair1 = new DocUserPair();
	private static DocUserPair _docpair2 = new DocUserPair(); 
	private static DocUserPair _docpair3 = new DocUserPair();
	private static byte[] iv = { 1, 1, 2, 3};
	private static byte[] iv1 = { 1, 1, 2, 3, 5, 6, 7};
	static SDStore port;
	
	@BeforeClass
    public static void oneTimeSetUp() {
    	SDStore_Service service = new SDStore_Service();
    	port = service.getSDStoreImplPort();
    	
    	_docpair1.setUserId("alice");
    	_docpair1.setDocumentId("a1");
    	_docpair2.setUserId("alice");
    	_docpair2.setDocumentId("a2");
    	_docpair3.setUserId("bruno");
    	_docpair3.setDocumentId("b1");
    }

    @AfterClass
    public static void oneTimeTearDown() {
    }
    
    @Test   
    public void checknumberServers() throws Exception{
    	//correr 2 servidores diferentes e apagar a db do uddi!!!
    	//servidores com os portos: http://localhost:8082/sd-store/endpoint
    	// e http://localhost:8083/sd-store/endpoint e SD-STORE1
    	FrontEnd service = new FrontEnd(null,"SD-STORE","http://localhost:8081");
    	assertEquals(2,service.getServers().size());
    	assertEquals("http://localhost:8083/sd-store/endpoint",service.getServers().toArray()[0]);
    	assertEquals("http://localhost:8082/sd-store/endpoint",service.getServers().toArray()[1]);
    	System.out.println(service.getServers());
    }
}
