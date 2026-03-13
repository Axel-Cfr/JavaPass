package javapass;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javapass.SQLite.ReturningUserValues;

public class Services {
    SQLite sqlite = new SQLite();

    public String connectionDB() {
        try {
            sqlite.initialisationDB();
            return "Done";
        } catch(SQLException e) {
            return e.getMessage();
        }
    }

    public String authentification(String username, String password) {
        try {
            ReturningUserValues uservalue = sqlite.get_user(username);

            byte[][] hashAndSalt = Argon2.derivePassword(password, uservalue.argon2Type);
		    byte[] hash = hashAndSalt[0];
		    byte[] salt = hashAndSalt[1];
    	    SecretKey key = new SecretKeySpec(hash, "AES");
            byte[] iv = uservalue.iv_verify;
    	    GCMParameterSpec gcmParameterSpec = AES.generateGCMParameterSpec(iv);
    	    String algorithm = "AES/GCM/NoPadding";
            String decryptedText = AES.decrypt(algorithm, username, key, gcmParameterSpec);
            if(decryptedText.equals(username)) {
                return "Done";
            } else {
                return "Wrong";
            }
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    public String inscription(String username, String password, String passwordverif, String option) {
        try {
            if(password.equals(passwordverif)) {
                int argon2Type;
                if(option.equals("1")) {
                    argon2Type = 0;
                } else {
                    argon2Type = 2;
                }
                byte[][] hashAndSalt = Argon2.derivePassword(password, argon2Type);
		        byte[] hash = hashAndSalt[0];
		        byte[] salt = hashAndSalt[1];
                byte[] byteArgon2Type = hashAndSalt[2];
    	        SecretKey key = new SecretKeySpec(hash, "AES");
                byte[] iv = AES.generateIv();
    	        GCMParameterSpec gcmParameterSpec = AES.generateGCMParameterSpec(iv);
    	        String algorithm = "AES/GCM/NoPadding";
    	        String cipherText = AES.encrypt(algorithm, username, key, gcmParameterSpec);

                LocalDate localDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedString = localDate.format(formatter);
                sqlite.ajout_utilisateur(username, cipherText, iv, salt, argon2Type, formattedString);

                return "Done";
            } else {
                return  "Different";
            }
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    public SecureRandom generateSecureRandom() {
        return new SecureRandom();
    }

    public String generate(int taille, boolean minuscule, boolean majuscule, boolean chiffres, boolean speciaux) {
        
        // Listes des caractères disponibles
        String MINUSCULES = "abcdefghijklmnopqrstuvwxyz";
        String MAJUSCULES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String CHIFFRES = "0123456789";
        String SPECIAUX = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

        // Un mot de passe ne peut pas être plus petit que TAILLE_MINIMUM si c'est le cas on prend la TAILLE_PAR_DEFAUT
        int TAILLE_PAR_DEFAUT = 20;
        int TAILLE_MINIMUM = 12;
        
        if (taille < TAILLE_MINIMUM) {
            taille = TAILLE_PAR_DEFAUT;
        }

        StringBuilder caracteresDisponibles = new StringBuilder();
        if (minuscule) {
            caracteresDisponibles.append(MINUSCULES);
        }
        if (majuscule) {
            caracteresDisponibles.append(MAJUSCULES);
        }
        if (chiffres) {
            caracteresDisponibles.append(CHIFFRES);
        }
        if (speciaux) {
            caracteresDisponibles.append(SPECIAUX);
        }
        // Si aucune catégorie séléctionnée alors on les prend toutes
        if (caracteresDisponibles.length() == 0) {
            caracteresDisponibles.append(MINUSCULES).append(MAJUSCULES).append(CHIFFRES).append(SPECIAUX);
        }

        String choix = caracteresDisponibles.toString();
        char[] motDePasse = new char[taille];
        int position = 0;

        // Au moins 3 caractères de chaque catégorie sélectionnée
        for (int k = 0; k < 3; k++) {
            if (minuscule) motDePasse[position++] = MINUSCULES.charAt(generateSecureRandom().nextInt(MINUSCULES.length()));
            if (majuscule) motDePasse[position++] = MAJUSCULES.charAt(generateSecureRandom().nextInt(MAJUSCULES.length()));
            if (chiffres) motDePasse[position++] = CHIFFRES.charAt(generateSecureRandom().nextInt(CHIFFRES.length()));
            if (speciaux) motDePasse[position++] = SPECIAUX.charAt(generateSecureRandom().nextInt(SPECIAUX.length()));
        }
        
        for (int i = position; i < taille; i++) {
            motDePasse[i] = choix.charAt(generateSecureRandom().nextInt(choix.length()));
        }

        for (int i = taille - 1; i > 0; i--) {
            int j = generateSecureRandom().nextInt(i + 1);
            char temp = motDePasse[i];
            motDePasse[i] = motDePasse[j];
            motDePasse[j] = temp;
        }

        return new String(motDePasse);
    }

    public String wait(int millisecond) {
        try {
            Thread.sleep(3000);
            return "Done";
        } catch(InterruptedException e) {
            return e.getMessage();
        }
    }
}
