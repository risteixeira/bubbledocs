package pt.ulisboa.tecnico.sdis.id.test;

import java.net.MalformedURLException;
import java.net.URL;

import javax.jws.WebService;
import javax.xml.ws.WebServiceException;

import pt.ulisboa.tecnico.sdis.id.ws.*;
import pt.ulisboa.tecnico.sdis.id.SDIdClient;
import pt.ulisboa.tecnico.sdis.id.SDIdClientException;

import org.junit.*;

import static org.junit.Assert.*;
import mockit.*;

public class SdIdTest {
	private static SDId port;
	private static SDIdClient client;
	private static URL url;
	
	@BeforeClass
	public static void oneTimeSetUp(){
		SDId_Service service = new SDId_Service();
		port = service.getSDIdImplPort();
		

		try {
			url = new URL("http://localhost:8080/sd-id/endpoint");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 try {
			client = new SDIdClient(url);
		} catch (SDIdClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void oneTimeTearDown(){
		port = null;
		client = null;
		url = null;
	}
	
	@Before
	public void setUp(){
	}
	
	@After
	public void tearDown(){
	}
	
	/**
     *  In this test the server is mocked to
     *  simulate a communication exception.
     */
    @Test(expected=WebServiceException.class)
    public void testMockServerException(
        @Mocked final SDId_Service service,
        @Mocked final SDId port)
        throws Exception {

        // an "expectation block"
        // One or more invocations to mocked types, causing expectations to be recorded.
        new Expectations() {{
            new SDId_Service();
            service.getSDIdImplPort(); result = port;
            port.createUser(anyString, anyString);
            result = new WebServiceException("fabricated");
        }};

        
        
		// Unit under test is exercised.
//        SDIdClient client = new SDIdClient();
        // call to mocked server
        client.createUser("pedro", "pedro@fialho");
    }
    
    /**
     *  In this test the server is mocked to
     *  simulate a communication exception on a second call.
     */
    @Test
    public void testMockServerExceptionOnSecondCall(
        @Mocked final SDId_Service service,
        @Mocked final SDId port)
        throws Exception {

        // an "expectation block"
        // One or more invocations to mocked types, causing expectations to be recorded.
        new Expectations() {{
            new SDId_Service();
            service.getSDIdImplPort(); result = port;
            port.createUser("pedro", "pedro@fialho");
            // first call to sum returns the result
            result = null;
            // second call throws an exception
            result = new WebServiceException("fabricated");
        }};


        // Unit under test is exercised.
//        SDIdClient client = new SDIdClient();

        // first call to mocked server
        try {
            client.createUser("pedro", "pedro@fialho");
        } catch(WebServiceException e) {
            // exception is not expected
            fail();
        }

        // second call to mocked server
        try {
            client.createUser("pedro", "pedro@fialho");
            fail();
        } catch(WebServiceException e) {
            // exception is expected
            assertEquals("fabricated", e.getMessage());
        }
    }
    
    @Test
    public void createUserSuccess() throws Exception {
    	port.createUser("dharu", "dharu@queshil");
    }
    
    @Test(expected = EmailAlreadyExists_Exception.class)
    public void createUserDuplicateEmail() throws Exception {
    	port.createUser("dharita", "dharu@queshil");
    	port.createUser("dharita2", "dharu@queshil");
    }
    
    @Test(expected = InvalidEmail_Exception.class)
    public void createUserInvalidEmail() throws Exception {
    	port.createUser("dharita", "dharu@");
    }
    
    @Test(expected = InvalidUser_Exception.class)
    public void createUserUsernameNull() throws Exception {
    	port.createUser(null, "dharu4@almeida");
    }
    
    @Test(expected = InvalidUser_Exception.class)
    public void createUserUsernameEmpty() throws Exception {
    	port.createUser("", "dharu40@almeida");
    }
    
    @Test(expected = InvalidEmail_Exception.class)
    public void createUserEmailEmpty() throws Exception {
    	port.createUser("dharita5", "");
    }
    
    @Test(expected = UserAlreadyExists_Exception.class)
    public void createUserDuplicateUsername() throws Exception {
    	port.createUser("dharu1", "dharu@almeida");
    	port.createUser("dharu1", "dharu1@almeida");
    }
    
    @Test(expected = InvalidUser_Exception.class)
    public void createUserInvalidUser() throws Exception {
    	port.createUser("dh", "dharu2@almeida");
    }
    
    @Test(expected = InvalidEmail_Exception.class)
    public void createUserInvalidEmail2() throws Exception {
    	port.createUser("dh1", "@almeida");
    }
    
    @Test(expected = InvalidEmail_Exception.class)
    public void createUserInvalidEmail3() throws Exception {
    	port.createUser("dh2", null);
    }
    
    @Test(expected = InvalidEmail_Exception.class)
    public void createUserInvalidEmail4() throws Exception {
    	port.createUser("dh3", "dharu");
    }
    
    @Test
    public void renewPasswordSuccess() throws Exception {
    	port.createUser("jose", "jose@ist");
    	port.renewPassword("jose");
    }
    
    @Test(expected = UserDoesNotExist_Exception.class)
    public void renewPasswordInvalidUser() throws Exception {
    	port.renewPassword("joao");
    }
    
    @Test(expected = UserDoesNotExist_Exception.class)
    public void renewPasswordNull() throws Exception {
    	port.renewPassword(null);
    }
    
    @Test(expected = UserDoesNotExist_Exception.class)
    public void renewPasswordEmpty() throws Exception {
    	port.renewPassword("");
    }
    
    @Test
    public void removeUserSuccess() throws Exception {
    	port.createUser("joana", "joana@vicente");
    	port.removeUser("joana");
    }
    
    @Test(expected = UserDoesNotExist_Exception.class)
    public void removeUserInvalidUser() throws Exception {
    	port.removeUser("joao");
    }
    
    @Test(expected = UserDoesNotExist_Exception.class)
    public void removeUserTwice() throws Exception {
    	port.createUser("joao", "joao@almeida");
    	port.removeUser("joao");
    	port.removeUser("joao");
    }
    
    @Test(expected = UserDoesNotExist_Exception.class)
    public void removeUserNull() throws Exception {
    	port.removeUser(null);
    }
    
    @Test(expected = UserDoesNotExist_Exception.class)
    public void removeUserEmpty() throws Exception {
    	port.removeUser("");
    }
    
//    @Test
//    public void requestAuthenticationSuccess() throws Exception {
//    	port.requestAuthentication("bruno", "Bbb2".getBytes());
//    }
//    
//    @Test(expected = AuthReqFailed_Exception.class)
//    public void requestAuthenticationInvalidUser() throws Exception {
//    	port.requestAuthentication("rita", "<3".getBytes());
//    }
//    
//    @Test(expected = AuthReqFailed_Exception.class)
//    public void requestAuthenticationNullUser() throws Exception {
//    	port.requestAuthentication(null, "Bbb2".getBytes());
//    }
//    
//    @Test(expected = AuthReqFailed_Exception.class)
//    public void requestAuthenticationEmptyUser() throws Exception {
//    	port.requestAuthentication("", "Bbb2".getBytes());
//    }
//    
//    @Test(expected = AuthReqFailed_Exception.class)
//    public void requestAuthenticationNullPassword() throws Exception {
//    	port.requestAuthentication("bruno", null);
//    }
//    
//    @Test(expected = AuthReqFailed_Exception.class)
//    public void requestAuthenticationEmptyPassword() throws Exception {
//    	port.requestAuthentication("bruno", "".getBytes());
//    }
//    
//    @Test(expected = AuthReqFailed_Exception.class)
//    public void requestAuthenticationWrongUser() throws Exception {
//    	port.requestAuthentication("bruno2", "Bbb2".getBytes());
//    }
//    
//    @Test(expected = AuthReqFailed_Exception.class)
//    public void requestAuthenticationNull() throws Exception {
//    	port.requestAuthentication(null, null);
//    }
//    
//    @Test(expected = AuthReqFailed_Exception.class)
//    public void requestAuthenticationEmpty() throws Exception {
//    	port.requestAuthentication("", "".getBytes());
//    }
}
