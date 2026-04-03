package javapass;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javapass.SQLite.UserValues;

public class Services {
    SQLite sqlite = new SQLite();
    User user;

    private static final String MINUSCULES = "abcdefghijklmnopqrstuvwxyz";
    private static final String MAJUSCULES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHIFFRES = "0123456789";
    private static final String SPECIAUX = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

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
            UserValues uservalue = sqlite.get_user(username);
            
            byte[] salt = uservalue.getSalt();
            byte[] hash = Argon2.derivePassword(password, salt, uservalue.getArgon2Type());
    	    
            SecretKey key = new SecretKeySpec(hash, "AES");
            byte[] iv = uservalue.getIv_verify();
    	    GCMParameterSpec gcmParameterSpec = AES.generateGCMParameterSpec(iv);
    	    String algorithm = "AES/GCM/NoPadding";
            String decryptedText = AES.decrypt(algorithm, uservalue.getEncrypted_textAndTag_verify(), key, gcmParameterSpec);

            if(decryptedText.equals(username)) {
                int userID = uservalue.getUser_id();
                ArrayList<SQLite.MdpValues> mdpValues = sqlite.get_mdp(uservalue.getUser_id());
                user = new User(userID, username, hash, uservalue.getLast_login(), mdpValues);
                
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
                    argon2Type = 0; // Argon2d
                } else {
                    argon2Type = 2; // Argon2id
                }
                byte[] salt = Argon2.generateSalt();
                byte[] hash = Argon2.derivePassword(password, salt, argon2Type);

                //byte[] byteArgon2Type = hashAndSalt[2];
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

    // Fonction qui renvoie la liste de tout les noms de sites 
    // desquels l'utilisateur a enregistré son mot de passe
    public ArrayList<String> returnWebsiteName() {
        return user.getWebsiteNameList();
    }

    // Fonction de recherche des noms de sites enregistré par l'utilisateur
    public ArrayList<String> researchWebsiteName(String input) {
        ArrayList<String> websiteNameList = returnWebsiteName();
        if(input == null || input.isBlank()) {
            return websiteNameList;
        }

        ArrayList<String> newWebsiteNameList = new ArrayList<>();
        for(int i = 0; i < websiteNameList.size(); i++) {
            if(websiteNameList.get(i).contains(input)) {
                newWebsiteNameList.add(websiteNameList.get(i));
            }
        }
        return newWebsiteNameList;
    }

    // Fonction qui retourne les informations dechiffrées du mots de passe choisi
    public String[] givePasswordInfos(String websiteName) {
        try {
            String[] passwordInfos = new String[4];
            int indPassword = user.getPasswordValues(websiteName);

            // Dechiffre le nom d'utilisateur et le mot de passe
            SecretKey key = new SecretKeySpec(user.getKey(), "AES");
            byte[] ivUsername = user.getIvUsername(indPassword);
            byte[] ivPassword = user.getIvPassword(indPassword);
    	    GCMParameterSpec gcmParameterSpecU = AES.generateGCMParameterSpec(ivUsername);
            GCMParameterSpec gcmParameterSpecP = AES.generateGCMParameterSpec(ivPassword);
    	    String algorithm = "AES/GCM/NoPadding";
            String decryptedUsername = AES.decrypt(algorithm, user.getEncryptedUsername(indPassword), key, gcmParameterSpecU);
            String decryptedPassword = AES.decrypt(algorithm, user.getEncryptedPassword(indPassword), key, gcmParameterSpecP);

            // Remplis le tableau avec le nom du site, l'url, le nom de l'utilisateur et mot de passe
            passwordInfos[0] = user.getWebsiteName(indPassword);
            passwordInfos[1] = user.getUrl(indPassword);
            passwordInfos[2] = decryptedUsername;
            passwordInfos[3] = decryptedPassword;

            return passwordInfos;
        } catch(Exception e) {
            String[] error = {e.getMessage()};
            return error;
        }
    }

    // Fonction qui ajoute un mot de passe
    public String addNewPassword(String[] PasswordInfos) {
        try {
            int userID = user.getUserID();
            String websiteName = PasswordInfos[0];
            String url = PasswordInfos[1];
            String username = PasswordInfos[2];
            String password = PasswordInfos[3];
            byte[] hash = user.getKey();
            SecretKey key = new SecretKeySpec(hash, "AES");

            // Chiffrement du nom d'utilisateur et du mot de passe
            String algorithm = "AES/GCM/NoPadding";
            byte[] ivUsername = AES.generateIv();
    	    GCMParameterSpec gcmParameterSpecU = AES.generateGCMParameterSpec(ivUsername);
    	    String encryptedUsername = AES.encrypt(algorithm, username, key, gcmParameterSpecU);
            byte[] ivPassword = AES.generateIv();
    	    GCMParameterSpec gcmParameterSpecP = AES.generateGCMParameterSpec(ivPassword);
    	    String encryptedPassword = AES.encrypt(algorithm, password, key, gcmParameterSpecP);

            // Ajout du mot de passe dans la base de données
            sqlite.ajout_mdp(userID, websiteName, url, encryptedUsername, encryptedPassword, ivUsername, ivPassword);
            
            // Recréation du user actualisé
            UserValues uservalue = sqlite.get_user(user.getUsername());
            String usernameAccount = user.getUsername();
            String last_login = user.getLast_login();
            ArrayList<SQLite.MdpValues> mdpValues = sqlite.get_mdp(userID);
            user = new User(userID, usernameAccount, hash, last_login, mdpValues);
            
            return "Done";
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    // Fonction qui génère un nombre flottant réellement aléatoire
    public SecureRandom generateSecureRandom() {
        return new SecureRandom();
    }

    // Fonction qui génère un mot de passe sécurisé en fonction des paramètres passés
    public String generatePassword(int taille, boolean minuscule, boolean majuscule, boolean chiffres, boolean speciaux) {

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

    // Fonction qui vérifie la présence de minuscule, majuscule, chiffre et caractère spécial dans un mot de passe
    private boolean[] check(String password) {
        boolean hasMinuscule = false;
        boolean hasMajuscule = false;
        boolean hasChiffre = false;
        boolean hasSpecial = false;

        if (password != null) {
            for (int i = 0; i < password.length(); i++) {
                String lettre = String.valueOf(password.charAt(i));

                if (MINUSCULES.contains(lettre)) hasMinuscule = true;
                else if (MAJUSCULES.contains(lettre)) hasMajuscule = true;
                else if (CHIFFRES.contains(lettre)) hasChiffre = true;
                else if (SPECIAUX.contains(lettre)) hasSpecial = true;
            }
        }
        return new boolean[] {hasMinuscule, hasMajuscule, hasChiffre, hasSpecial};
    }

    // Fonction qui vérifie si un mot de passe est valide et retourne les erreurs
    public String verifyPassword(String password) {
        if (password == null || password.length() < 12) {
            return "Le mot de passe doit faire au moins 12 caractères";
        }

        // Vérifie la présence de minuscule, majuscule, chiffre et caractère spécial avec la méthode check
        boolean[] results = check(password);
        boolean hasMinuscule = results[0];
        boolean hasMajuscule = results[1];
        boolean hasChiffre = results[2];
        boolean hasSpecial = results[3];

        // Stockage des erreurs grâce à la méthode check
        StringBuilder errors = new StringBuilder();
        if (!hasMinuscule) errors.append("- Il manque une minuscule.\n");
        if (!hasMajuscule) errors.append("- Il manque une majuscule.\n");
        if (!hasChiffre) errors.append("- Il manque un chiffre.\n");
        if (!hasSpecial) errors.append("- Il manque un caractère spécial.\n");

        // retourne les erreurs en transformant le StringBuilder en String
        if (errors.length() > 0) {
            return errors.toString();
        }

        // Si aucune erreur alors le mot de passe est valide
        return "Le mot de passe est valide.";
    }

    // Fonction qui améliore un mot de passe en lui ajoutant les éléments manquants
    public String enhancePassword(String password) {
        if (password == null) password = "";

        // Vérifie la présence de minuscule, majuscule, chiffre et caractère spécial avec la méthode check
        boolean[] results = check(password);
        boolean hasMinuscule = results[0];
        boolean hasMajuscule = results[1];
        boolean hasChiffre = results[2];
        boolean hasSpecial = results[3];

        StringBuilder newPassword = new StringBuilder(password);
        SecureRandom random = generateSecureRandom();

        // Ajout des types manquants
        if (!hasMinuscule) newPassword.append(MINUSCULES.charAt(random.nextInt(MINUSCULES.length())));
        if (!hasMajuscule) newPassword.append(MAJUSCULES.charAt(random.nextInt(MAJUSCULES.length())));
        if (!hasChiffre) newPassword.append(CHIFFRES.charAt(random.nextInt(CHIFFRES.length())));
        if (!hasSpecial) newPassword.append(SPECIAUX.charAt(random.nextInt(SPECIAUX.length())));

        String caracteresDisponibles = MINUSCULES + MAJUSCULES + CHIFFRES + SPECIAUX;
        while (newPassword.length() < 12) {
            newPassword.append(caracteresDisponibles.charAt(random.nextInt(caracteresDisponibles.length())));
        }

        return newPassword.toString();
    }

    public String wait(int millisecond) {
        try {
            Thread.sleep(millisecond);
            return "Done";
        } catch(InterruptedException e) {
            return e.getMessage();
        }
    }
}
