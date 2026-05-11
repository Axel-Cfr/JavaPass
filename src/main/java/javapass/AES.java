package javapass;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class AES {
	
	// Fonction qui crée le vecteur d'initialisation (12 octets)
	public static byte[] generateIv() {
		// Crée un tableau de 12 octets
		byte[] iv = new byte[12];
		// Remplit ce tableau de 12 octets aléatoires et complétement imprévisibles
		SecureRandom secureRandom = new SecureRandom();
    	secureRandom.nextBytes(iv);
    	return iv;
	}

	// Fonction qui crée un objet GCMParameterSpec (128bits = 16 octets) avec un iv (12 octets) 
	// et le tag d'authentification propre à GCM (4 octets)
	public static GCMParameterSpec generateGCMParameterSpec(byte[] iv) {
    	return new GCMParameterSpec(128, iv);
	}

	// Fonction qui chiffre une chaîne de caractère et la retourne une fois chiffrée
	public static String encrypt(String algorithm, String input, SecretKey key, GCMParameterSpec iv) throws Exception {
    	// Crée un objet chiffreur avec l'algorithme AES-256-GCM
		Cipher cipher = Cipher.getInstance(algorithm);
		// Initialise l'objet chiffreur en mode chiffrement
    	cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		// Chiffre le message à chiffrer converti en tableau d'octets
    	byte[] cipherText = cipher.doFinal(input.getBytes());
		// Retourne le message chiffré en chaîne de caractères
    	return Base64.getEncoder().encodeToString(cipherText);
	}

	// Fonction qui dechiffre une chaîne de caractère chiffrée et la retourne une fois dechiffrée
	public static String decrypt(String algorithm, String cipherText, SecretKey key, GCMParameterSpec iv) throws Exception {
    	// Crée un objet chiffreur avec l'algorithme AES-256-GCM
		Cipher cipher = Cipher.getInstance(algorithm);
		// Initialise l'objet chiffreur en mode déchiffrement
    	cipher.init(Cipher.DECRYPT_MODE, key, iv);
		// Déchiffre le message à déchiffrer converti en tableau d'octets
    	byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		// Retourne le message déchiffré en chaîne de caractères
    	return new String(plainText);
	}
}
