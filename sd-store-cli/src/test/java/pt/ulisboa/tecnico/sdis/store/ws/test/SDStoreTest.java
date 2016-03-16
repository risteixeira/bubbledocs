package pt.ulisboa.tecnico.sdis.store.ws.test;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.Cryptography;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.FrontEnd;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;

public class SDStoreTest {
	
	private static DocUserPair _docpair1 = new DocUserPair();
	private static DocUserPair _docpair2 = new DocUserPair(); 
	private static DocUserPair _docpair3 = new DocUserPair();
	private static byte[] iv = { 1, 1, 2, 3};
	private static byte[] iv1 = { 1, 1, 2, 3, 5, 6, 7};
	static SDStore port;
	
	@BeforeClass
    public static void oneTimeSetUp() {  	
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
    
    //#######################  TESTES-SD-STORE-A  ##############################
    @Test
    public void sucessStore() throws Exception{  		
    	FrontEnd service = new FrontEnd(null,"SD-STORE","http://localhost:8081");
    	service.store(_docpair1, iv);
    	Integer i = 0;
    	byte[] newbyte = service.load(_docpair1);
       	assertEquals(iv.length,newbyte.length);
       	while(i<iv.length){
       		assertEquals(iv[i],newbyte[i]);
       		i++;
       	}
    }
    
    @Test(expected = Exception.class)
    public void UserDoesNotExist() throws Exception{
    	DocUserPair _docUserPair4 = new DocUserPair();
    	_docUserPair4.setDocumentId("folha");
    	_docUserPair4.setUserId("Casimiro");
    	FrontEnd service = new FrontEnd(null,"SD-STORE","http://localhost:8081");
    	service.load(_docUserPair4);
    }
    
    @Test(expected = Exception.class)
    public void DocDoesNotExists() throws Exception{
    	DocUserPair _docUserPair4 = new DocUserPair();
    	_docUserPair4.setDocumentId("folha");
    	_docUserPair4.setUserId("alice");
    	FrontEnd service = new FrontEnd(null,"SD-STORE","http://localhost:8081");
    	service.load(_docUserPair4);
    }
   
    @Test(expected = Exception.class)
    public void duplicateDoc() throws Exception{
    	FrontEnd service = new FrontEnd(null,"SD-STORE","http://localhost:8081");	
    	service.createDoc(_docpair1);   	
    	service.createDoc(_docpair1);
    }
    
    //###################   TESTES-SD-ID-B  ##########################
    @Test
    public void verifyMAC() throws Exception{
    	FrontEnd service = new FrontEnd(null,"SD-STORE","http://localhost:8081");	
    	service.store(_docpair2,iv);
    	byte[] test = null;
    	byte[] testMAC = null;
    	Cryptography contentEncrypt = new Cryptography();
    	test = contentEncrypt.Encrypt(iv, contentEncrypt.getKey());
    	byte[] test1 = contentEncrypt.Encrypt(iv1, contentEncrypt.getKey());
    	testMAC = contentEncrypt.makeMAC(test, service.getSecretKey());
		boolean cenas = Cryptography.verifyMAC(testMAC,test1 , service.getSecretKey());
		assertEquals(false, cenas);
    }
    
    @Test
    public void verifyEncrypt() throws Exception{
    	FrontEnd service = new FrontEnd(null,"SD-STORE","http://localhost:8081");	
    	service.store(_docpair2,iv);
    	byte[] newDoc = service.getcontentCifer();
    	boolean verify = Arrays.equals(newDoc, iv);
    	assertEquals(false,verify);
    	service.load(_docpair2);
    	byte[] newDocLoad = service.getcontentCiferLoad();
    	boolean verify1 = Arrays.equals(newDocLoad, newDoc);
    	assertEquals(true,verify1);
    	byte[] desenctyptdoc = service.load(_docpair2);
    	boolean verify2 = Arrays.equals(desenctyptdoc, iv);
    	assertEquals(true,verify2);  	
    }
}
