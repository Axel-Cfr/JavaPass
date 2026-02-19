package javapass;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class AES {
	
	public static GCMParameterSpec generateIv() {
		byte[] iv = new byte[12];
    	new SecureRandom().nextBytes(iv);
    	return new GCMParameterSpec(128, iv);
	}

	public static String encrypt(String algorithm, String input, SecretKey key, GCMParameterSpec iv) throws Exception {
    	Cipher cipher = Cipher.getInstance(algorithm);
    	cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    	byte[] cipherText = cipher.doFinal(input.getBytes());
    	return Base64.getEncoder().encodeToString(cipherText);
	}

	public static String decrypt(String algorithm, String cipherText, SecretKey key, GCMParameterSpec iv) throws Exception {
    	Cipher cipher = Cipher.getInstance(algorithm);
    	cipher.init(Cipher.DECRYPT_MODE, key, iv);
    	byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
    	return new String(plainText);
	}
}
