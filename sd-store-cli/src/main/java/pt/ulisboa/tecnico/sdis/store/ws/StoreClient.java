package pt.ulisboa.tecnico.sdis.store.ws;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class StoreClient implements SDStore{
	
    public static final String CLASS_NAME = StoreClient.class.getSimpleName();
    public static final String TOKEN = "client";
    private static String uddiURL;
    private static String wsURL;
    private static String name;
    static FrontEnd frontend;
	Key _key = null;
	SecretKey _secretKey = null;
	
	
	//Constructors:
	public StoreClient(String wsURL) throws Exception {
		//frontend = new FrontEnd(wsURL,name,uddiURL);
		StoreClient.wsURL = wsURL;
		Cryptography contentEncrypt = new Cryptography();
		_key = contentEncrypt.getKey();
		_secretKey = contentEncrypt.generate();
	}

	public StoreClient() throws Exception {
		//System.out.println("OLAAAA");
		frontend = new FrontEnd(null,"SD-STORE%","http://localhost:8081");
		//System.out.println("oi");
		StoreClient.wsURL=null;
		Cryptography contentEncrypt = new Cryptography();
		_key = contentEncrypt.getKey();
		_secretKey = contentEncrypt.generate();
	}
	
	//Main:
	public static void main(String[] args) throws SDStoreClientException, Exception {
        if (args.length < 2) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s uddiURL name%n", SDStore_Service.class.getName());
            return;
        }    
        uddiURL = args[0];
        name = args[1];
        
        //System.out.printf("Contacting UDDI at %s%n", uddiURL);
        //UDDINaming uddiNaming = new UDDINaming(uddiURL);
        
        
        //System.out.printf("Looking for '%s'%n", name);
        //String endpointAddress = uddiNaming.lookup( name);
        
        /*if (endpointAddress == null) {
            System.out.println("Not found!");
            return;
        } else {
            System.out.printf("Found %s%n", endpointAddress);
        }*/
        
        System.out.println(wsURL);
        System.out.println(name);
        System.out.println(uddiURL);
        frontend = new FrontEnd(wsURL,name,uddiURL);              
	}
	
	public boolean isVerbose() {
		return frontend.isVerbose();
	}

	public void setVerbose(boolean verbose) {
		frontend.setVerbose(verbose);
	}

	
	//SD-Store functions:
	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {
		// TODO Auto-generated method stub
		frontend.createDoc(docUserPair);
	}

	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		return frontend.listDocs(userId);
	}

	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		frontend.store(docUserPair, contents);
		
	}

	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		byte[] content = frontend.load(docUserPair);
		return content;

	}	
}
