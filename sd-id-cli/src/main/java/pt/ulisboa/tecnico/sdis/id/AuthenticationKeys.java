package pt.ulisboa.tecnico.sdis.id;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
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
	
	public static Key makeKeySpec(byte[] hashPass) throws Exception {
		
		byte[] keyBytes = hashPass;
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		return key;
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
