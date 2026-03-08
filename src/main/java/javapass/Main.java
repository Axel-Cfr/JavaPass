package javapass;

/*import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;*/

public class Main {
    public static void main(String[] args) throws Exception {
		SQLite.initialisationDB();
        Interface.afficherBienvenue();

    	//String mdp = Generate.generate(18, true, true, true, true);
        //System.out.println("Mot de passe généré : " + mdp);

        // Thread.sleep(10000);

        /*String input = "JavaPass";
		String password = "JavaPass";
        String userType = "PC";
		
        byte[][] hashAndSalt = Argon2.derivePassword(password, userType);
		byte[] hash = hashAndSalt[0];
		byte[] salt = hashAndSalt[1];
    	SecretKey key = new SecretKeySpec(hash, "AES");
    	GCMParameterSpec gcmParameterSpec = AES.generateIv();
    	String algorithm = "AES/GCM/NoPadding";
    	String cipherText = AES.encrypt(algorithm, input, key, gcmParameterSpec);
    	String plainText = AES.decrypt(algorithm, cipherText, key, gcmParameterSpec);
    	
        System.out.println(input.equals(plainText) + " " + plainText);*/
    }
}