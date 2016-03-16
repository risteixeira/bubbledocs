package pt.ulisboa.tecnico.sdis.id;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AuthenticationKeys {
	
	private static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private static IvParameterSpec ivspec = new IvParameterSpec(iv);

	public static byte[] hashPassword(String password) throws NoSuchAlgorithmException {

		final byte[] passBytes = password.getBytes();
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(passBytes);
		byte[] digest = messageDigest.digest();
		
		return digest;	
	}
	
	public static Key makeKey() throws Exception {
		
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		Key key = keyGen.generateKey();
		return key;
	}

	public static Key makeKeySpec() throws Exception {
		
		byte[] keyBytes = new byte[] { 
                0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		return key;
	}
	
	public static Key makeKeySpec(byte[] hashPass) throws Exception {
		
		byte[] keyBytes = hashPass;
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		return key;
	}
	
	public static byte[] secureRandomNumber() throws Exception{
		
		 	SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	        final byte array[] = new byte[16];
	        random.nextBytes(array);
	        return array;
	}
	
	public static byte[] createMAC(byte[] bytes, Key key) throws Exception{
		
		Mac cipher = Mac.getInstance("HmacMD5");
    	cipher.init(key);
        byte[] cipherDigest = cipher.doFinal(bytes);
		return cipherDigest;
	}
	
	public static boolean verifyMAC(byte[] cipherDigest, byte[] bytes, Key key) throws Exception {

		Mac cipher = Mac.getInstance("HmacMD5");
		cipher.init(key);
		byte[] cipheredBytes = cipher.doFinal(bytes);
		return Arrays.equals(cipherDigest, cipheredBytes);
	}
	
	
	public static byte[] encrypt(Key key, byte[] input) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
        byte[] cipherBytes = cipher.doFinal(input);

        return cipherBytes;
	}
	
	public static byte[] decrypt(Key key, byte[] input) throws Exception{
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
        byte[] cipherBytes = cipher.doFinal(input);

        return cipherBytes;
	}
	
	public static byte[] generateTicket(String userId, Key serverClientKey) throws Exception{
		
		long strt = System.currentTimeMillis();
		long expre = System.currentTimeMillis() + 5000;
		String idSrver = "SD-STORE";
		
		byte[] idClient = userId.getBytes();
		byte[] idServer = idSrver.getBytes();
		byte[] serverclientkey = serverClientKey.getEncoded();
		byte[] splitter = new byte[] { 0x00, 0x00 };
		
		ByteBuffer bufferStart = ByteBuffer.allocate(Long.SIZE);
	    bufferStart.putLong(strt);
	    byte[] start = bufferStart.array();
	    
	    ByteBuffer bufferExpire = ByteBuffer.allocate(Long.SIZE);
	    bufferExpire.putLong(expre);
	    byte[] expire = bufferExpire.array();
	    
		
		ByteArrayOutputStream ticket = new ByteArrayOutputStream();
		//TMNT
		ticket.write(idClient);
		ticket.write(splitter);
		ticket.write(idServer);
		ticket.write(splitter);
		ticket.write(start);
		ticket.write(splitter);
		ticket.write(expire);
		ticket.write(splitter);
		ticket.write(serverclientkey);
		
		byte[] encriptedTicket = encrypt(makeKeySpec(), ticket.toByteArray());
		
		return encriptedTicket;
	}
	
//	public static byte[] encryptHash(byte[] hashPass) throws Exception{
//		return encrypt(makeKeySpec(), hashPass);
//	}
//	
//	public static byte[] encryptTicket(byte[] ticket) throws Exception{
//		return encrypt(makeKey(),generateTicket());
//	}
//	
//	public static byte[] decryptHash(byte[] hashPass) throws Exception{
//		return encrypt(makeKeySpec(), hashPass);
//	}
//	
//	public static byte[] decryptTicket(byte[] ticket) throws Exception{
//		return encrypt(makeKey(),generateTicket());
//	}

}
