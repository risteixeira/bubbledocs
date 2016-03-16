package pt.ulisboa.tecnico.sdis.store.ws;

//import java.security.Key;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.store.ws.handler.RelayClientHandler;
import pt.ulisboa.tecnico.sdis.store.ws.uddi.UDDINaming;

public class FrontEnd implements SDStore{

	public static final String CLASS_NAME = StoreClient.class.getSimpleName();
	public static final String TOKEN = "client";
	//private Cipher cipher;
	//private Key key;
	private SDStore port;
	private boolean verbose = false;
	SDStore_Service service = null;
	Collection<String> listUddi = new ArrayList<String>();
	private static List<SDStore> listPorts =null;
	private static URL uddiUrl;
	
	private Key _key = null;
	private SecretKey _secretKey = null;
	private Cryptography contentEncrypt = new Cryptography();
	private byte[] newCont = null;
	private byte[] newContLoad = null;

	public FrontEnd(String wsURL, String name, String URLuddi) throws Exception {
		UDDINaming uddiNaming = new UDDINaming(URLuddi);
		listPorts=  new ArrayList<SDStore>();
		listUddi = new ArrayList<String>();
		System.out.println(uddiNaming.list("SD-STORE%"));
		listUddi=uddiNaming.list("SD-STORE%");

		
		_key = contentEncrypt.getKey();
		_secretKey = contentEncrypt.generate();

		for(String uddi : listUddi){
			try {
				uddiUrl = new URL(uddi);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (verbose)
				System.out.println("Creating stub ...");
			service = new SDStore_Service(uddiUrl);
			port = service.getSDStoreImplPort();
			listPorts.add(port);
		
			System.out.println("Setting endpoint address ...");
		}
	}

	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {
		// TODO Auto-generated method stub
		for(SDStore service : listPorts)
			service.createDoc(docUserPair);		
	}

	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		for(SDStore service : listPorts)
			return	service.listDocs(userId);
		return null;	
	}

	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		int i = 0;
		List<Tag> tagList = new ArrayList<Tag>();
		Tag maxTag = new Tag(0,0);
		Integer index = 0;
		
		byte[] _digest=null;
		ByteArrayOutputStream _final = new ByteArrayOutputStream();
		byte [] _send=null;
		byte[] splitter = new byte[] { 0x00, 0x01, 0x00,0x02,0x00,0x00,0x03,0x04,0x00 };
		
		for(SDStore service : listPorts){
			
			BindingProvider bindingProvider = (BindingProvider) service;
			
			Map<String, Object> requestContext = bindingProvider.getRequestContext();			
			requestContext.put(RelayClientHandler.REQUEST_PROPERTY, "null");
			requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, listUddi.toArray()[i]);
			
			service.store(docUserPair, contents);
			Map<String,Object> responseContext = bindingProvider.getResponseContext();
			String finalValue = (String) responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
			String[] parse = finalValue.split(";");
			Tag newTag = new Tag(Integer.parseInt(parse[0]),Integer.parseInt(parse[1]));
			tagList.add(newTag);
			
			i++;
		}
		for(Tag tag : tagList){		
			if(tag.getseq() >= maxTag.getseq()){
				maxTag = tag;
			}
			index++;
		}
		
		try {
			newCont = contentEncrypt.Encrypt(contents, _key);
		} catch (InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			_digest = contentEncrypt.makeMAC(newCont, _secretKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			_final.write(newCont);
			_final.write(splitter);
			_final.write(_digest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		i = 0;	
		
		for(SDStore service : listPorts){		
			BindingProvider bindingProvider = (BindingProvider) service ; //service;
			
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			String newString = maxTag.getseq() + ";" + maxTag.getCid();
			String initialValue = newString;
			requestContext.put(RelayClientHandler.REQUEST_PROPERTY, initialValue);
			requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, listUddi.toArray()[i]);
			
			_send = _final.toByteArray();
			service.store(docUserPair, _send);
			i++;		
		}
	}

	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		byte[] newCont = null;
		int i = 0;
		List<Tag> tagList = new ArrayList<Tag>();
		Tag maxTag = new Tag(0,0);
		SDStore maxServer = null;
		Integer index = 0;
		boolean mac = false;
		
		for(SDStore service : listPorts){
			BindingProvider bindingProvider = (BindingProvider) service;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();			
			String initialValue = docUserPair.getDocumentId();
			requestContext.put(RelayClientHandler.REQUEST_PROPERTY, initialValue);
			requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, listUddi.toArray()[i]);
			
			service.load(docUserPair);
			
			Map<String,Object> responseContext = bindingProvider.getResponseContext();
			String finalValue = (String) responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
			String[] parse = finalValue.split(";");
			Tag newTag = new Tag(Integer.parseInt(parse[0]),Integer.parseInt(parse[1]));
			tagList.add(newTag);
		}
		for(Tag tag : tagList){		
			if(tag.getseq() >= maxTag.getseq()){
				maxTag = tag;
				maxServer = listPorts.get(index);
			}
			index++;
		}
		byte[] content = maxServer.load(docUserPair);
		
		String aux = printHexBinary(content);
		String[] parse = aux.split("000100020000030400");
		String DocbytesString= parse[0];
		String MacBytesString = parse[1];
		
		byte[] Docbytes = DatatypeConverter.parseHexBinary(DocbytesString);
		byte[] MacBytes = DatatypeConverter.parseHexBinary(MacBytesString);
		newContLoad = Docbytes;
		//System.out.println(printHexBinary(Docbytes));
		//System.out.println(printHexBinary(MacBytes));
		try {
			mac = Cryptography.verifyMAC(MacBytes, Docbytes, _secretKey);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!mac){
			System.out.println("Mac returned False !");
			return null;
		}
		try {
			newCont = contentEncrypt.Decrypt(Docbytes, _key);
		} catch (InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newCont;
	}
	
	public SecretKey getSecretKey(){
		return this._secretKey;
	}
	
	public Key getKey(){
		return this._key;
	}
	
	public byte[] getcontentCifer(){
		return newCont;
	}
	public byte[] getcontentCiferLoad(){
		return newContLoad;
	}
	public boolean isVerbose() {
		return verbose;
	}
	public Collection<String> getServers(){
		return this.listUddi;
	}
	

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
