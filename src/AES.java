import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AES {
    static Cipher cipher;
 
	public static void main(String[] args) throws Exception
	{
		// génération d'une clé AES de 256bits
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);
		SecretKey secretKey = keyGenerator.generateKey();
 
		// instanciation d'un objet chiffreur implémentant le chiffrement AES
		cipher = Cipher.getInstance("AES");
 
		// le clair
		String plainText = "Bienvenue sur JavaPass";
		System.out.println("Message avant cryptage: " + plainText);
 
		// le chiffré
		String encryptedText = encrypt(plainText, secretKey);
		System.out.println("Message après cryptage: " + encryptedText);
 
		// de nouveau le clair
		String decryptedText = decrypt(encryptedText, secretKey);
		System.out.println("Message decrypté: " + decryptedText);
	}
 
	private static String decrypt(String encryptedText, SecretKey secretKey) throws Exception
	{
		// chargement du chiffré dans un tableau d'octets
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] encryptedTextByte = decoder.decode(encryptedText);
 
		// initialisation du chiffreur en mode déchiffrement avec clé secrete
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
 
		// déchiffrement du tableau d'octets
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
 
		// chaine de caractères représentant le clair
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}
 
	private static String encrypt(String plainText, SecretKey secretKey) throws Exception
	{
		// chargement du clair dans un tableau d'octets
		byte[] plainTextByte = plainText.getBytes();
 
		// initialisation du chiffreur en mode chiffrement avec clé secrete
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
 
		// chiffrement du tableau d'octets
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
 
		// chaine de caractères représentant le chiffré 
		Base64.Encoder encoder = Base64.getEncoder();
		String encryptedText = encoder.encodeToString(encryptedByte);
		return encryptedText;
	}
}
