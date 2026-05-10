package javapass;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javapass.SQLite.UserValues;

public class Services {
    // Instanciation d'objets des classes SQLite, InactivityCounter et User
    private final SQLite sqlite = new SQLite();
    private final InactivityCounter inactivityCounter = new InactivityCounter();
    public User user;

    // Variables de caractères disponibles
    private static final String MINUSCULES = "abcdefghijklmnopqrstuvwxyz";
    private static final String MAJUSCULES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHIFFRES = "0123456789";
    private static final String SPECIAUX = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    // Fonction qui crée la connection à la base de données SQLite
    public String connectionDB() {
        try {
            sqlite.initialisationDB();
            return "Done";
        } catch(SQLException e) {
            return e.getMessage();
        }
    }

    // Fonction qui ferme la connexion à la base de données
    public String deconnectionDB() {
        try {
            sqlite.deconnexion();
            return "Done";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Fonction qui vérifie que les identifiants entrés sont valides et appartiennent à un compte
    public String authentification(String username, String password) {
        try {
            UserValues uservalue = sqlite.get_user(username);

            // Si le nom d'utilisateur saisi ne correspond à aucun utilisateur
            // Renvoie que la saisie est invalide
            if(uservalue.getUsername() == null) {
                return "Wrong";
            }
            
            // Dérivation du mot de passe maître
            byte[] salt = uservalue.getSalt();
            int argon2Type = uservalue.getArgon2Type();
            byte[] hash = Argon2.derivePassword(password, salt, argon2Type);
    	    
            // Déchiffrement du nom d'utilisateur chiffré
            SecretKey key = new SecretKeySpec(hash, "AES");
            byte[] iv = uservalue.getIv_verify();
    	    GCMParameterSpec gcmParameterSpec = AES.generateGCMParameterSpec(iv);
    	    String algorithm = "AES/GCM/NoPadding";
            String decryptedText = AES.decrypt(algorithm, uservalue.getEncrypted_textAndTag_verify(), key, gcmParameterSpec);

            // Comparaison entre le nom d'utilisateur entré et le nom d'utilisateur déchiffré
            if(decryptedText.equals(username)) {
                int userID = uservalue.getUser_id();
                ArrayList<SQLite.MdpValues> mdpValues = sqlite.get_mdp(userID);
                user = new User(userID, username, salt, argon2Type, hash, uservalue.getLast_login(), mdpValues);

                updateLastLogin();
                return "Done";
            } else {
                return "Wrong";
            }
        } catch(Exception e) {
            // Si le mot de passe est erroné
            if(e.getMessage().equals("Tag mismatch")) {
                return "Wrong";
            }
            return e.getMessage();
        }
    }

    // Fonction permettant de créer un compte
    public String inscription(String username, String password, String option) {
        try {
            // Création d'un sel et dérivation du mot de passe maître
            int argon2Type;
            if(option.equals("1")) {
                argon2Type = 0; // Argon2d
            } else {
                argon2Type = 2; // Argon2id
            }
            byte[] salt = Argon2.generateSalt();
            byte[] hash = Argon2.derivePassword(password, salt, argon2Type);

            // Chiffrement du nom d'utilisateur pour authentification ultérieure
    	    SecretKey key = new SecretKeySpec(hash, "AES");
            byte[] iv = AES.generateIv();
            GCMParameterSpec gcmParameterSpec = AES.generateGCMParameterSpec(iv);
	        String algorithm = "AES/GCM/NoPadding";
    	    String cipherText = AES.encrypt(algorithm, username, key, gcmParameterSpec);

            // Défintion de la date et de l'heure de création du compte
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
            String formattedString = localDateTime.format(formatter);

            // Création de l'utilisateur
            sqlite.ajout_utilisateur(username, cipherText, iv, salt, argon2Type, formattedString);

            return "Done";
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    // Fonction qui vérifie si le nom d'utilisateur est déjà pris
    public String isUsernameAvailable(String username) {
        try {
            ArrayList<String> userNameList = sqlite.get_usernameList();
            String available = "Yes";
            for (int i = 0; i < userNameList.size(); i++) {
                if(username.equals(userNameList.get(i))) {
                    available = "No";
                }
            }
            return available;
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    // Fonction qui renvoie la liste de tout les noms de sites 
    // desquels l'utilisateur a enregistré son mot de passe
    public ArrayList<String> returnWebsitesNames() {
        return user.getWebsiteNameList();
    }

    // Fonction de recherche des noms de sites enregistré par l'utilisateur
    public ArrayList<String> researchWebsiteName(String input) {
        // Si la saisie est nulle, renvoie la liste complète des mots de passe
        ArrayList<String> websiteNameList = returnWebsitesNames();
        if(input == null || input.isBlank()) {
            return websiteNameList;
        }

        // Sinon, renvoie la liste où les noms des sites contiennent la saisie
        ArrayList<String> newWebsiteNameList = new ArrayList<>();
        for(int i = 0; i < websiteNameList.size(); i++) {
            // toLowerCase() sert à tout mettre en minuscule rendant la recherche résistante à la casse
            if(websiteNameList.get(i).toLowerCase().contains(input.toLowerCase())) {
                newWebsiteNameList.add(websiteNameList.get(i));
            }
        }
        return newWebsiteNameList;
    }

    // Fonction qui retourne le nom d'utilisateur en clair d'un site
    public String[] giveUsername(String websiteName) {
        String[] password = new String[2];
        try {
            int indPassword = user.getPasswordIndice(websiteName);

            // Dechiffre le mot de passe
            SecretKey key = new SecretKeySpec(user.getKey(), "AES");
            byte[] ivPassword = user.getIvUsername(indPassword);
            GCMParameterSpec gcmParameterSpecP = AES.generateGCMParameterSpec(ivPassword);
    	    String algorithm = "AES/GCM/NoPadding";
            String decryptedPassword = AES.decrypt(algorithm, user.getEncryptedUsername(indPassword), key, gcmParameterSpecP);

            password[0] = "Done";
            password[1] = decryptedPassword;
            return password;
        } catch(Exception e) {
            password[0] = "Error";
            password[1] = e.getMessage();
            return password;
        }
    }

    // Fonction qui retourne le mot de passe en clair d'un site
    public String[] givePassword(String websiteName) {
        String[] password = new String[2];
        try {
            int indPassword = user.getPasswordIndice(websiteName);

            // Dechiffre le mot de passe
            SecretKey key = new SecretKeySpec(user.getKey(), "AES");
            byte[] ivPassword = user.getIvPassword(indPassword);
            GCMParameterSpec gcmParameterSpecP = AES.generateGCMParameterSpec(ivPassword);
    	    String algorithm = "AES/GCM/NoPadding";
            String decryptedPassword = AES.decrypt(algorithm, user.getEncryptedPassword(indPassword), key, gcmParameterSpecP);

            password[0] = "Done";
            password[1] = decryptedPassword;
            return password;
        } catch(Exception e) {
            password[0] = "Error";
            password[1] = e.getMessage();
            return password;
        }
    }

    // Fonction qui retourne les informations dechiffrées du mots de passe choisi
    public String[] givePasswordInfos(String websiteName) {
        try {
            String[] passwordInfos = new String[4];
            int indPassword = user.getPasswordIndice(websiteName);

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

    public boolean isWebsiteNameUsed(String websiteName) {
        ArrayList<String> websiteNameList = user.getWebsiteNameList();
        if(websiteNameList.contains(websiteName)) {
            return true;
        }
        return false;
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
            String usernameAccount = user.getUsername();
            String last_login = user.getLast_login();
            byte[] salt = user.getSalt();
            int argon2Type = user.getArgon2Type();
            ArrayList<SQLite.MdpValues> mdpValues = sqlite.get_mdp(userID);
            user = new User(userID, usernameAccount, salt, argon2Type, hash, last_login, mdpValues);
            
            return "Done";
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    // Fonction qui modifie un mot de passe
    public String updatePassword(String websiteName, String password) {
        try {
            int passwordInd = user.getPasswordIndice(websiteName);
            int passwordId = user.getPasswordId(passwordInd);

            byte[] hash = user.getKey();
            SecretKey key = new SecretKeySpec(hash, "AES");

            // Chiffrement du mot de passe
            String algorithm = "AES/GCM/NoPadding";
            byte[] iv = AES.generateIv();
    	    GCMParameterSpec gcmParameterSpecP = AES.generateGCMParameterSpec(iv);
    	    String encryptedPassword = AES.encrypt(algorithm, password, key, gcmParameterSpecP);

            // Mise à jour du mot de passe dans la BDD
            sqlite.update_sitemdp(passwordId, encryptedPassword, iv);

            // Recréation du user actualisé
            int userId = user.getUserID();
            String usernameAccount = user.getUsername();
            String last_login = user.getLast_login();
            byte[] salt = user.getSalt();
            int argon2Type = user.getArgon2Type();
            ArrayList<SQLite.MdpValues> mdpValues = sqlite.get_mdp(userId);
            user = new User(userId, usernameAccount, salt, argon2Type, hash, last_login, mdpValues);
            
            return "Done";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Fonction qui supprime un mot de passe
    public String deletePassword(String websiteName) {
        try {
            int userID = user.getUserID();
            byte[] hash = user.getKey();
            String usernameAccount = user.getUsername();
            String last_login = user.getLast_login();

            // Suppression du mot de passe dans la base de données
            sqlite.suppr_mdp(userID, websiteName);

            // Recréation du user actualisé
            byte[] salt = user.getSalt();
            int argon2Type = user.getArgon2Type();
            ArrayList<SQLite.MdpValues> mdpValues = sqlite.get_mdp(userID);
            user = new User(userID, usernameAccount, salt, argon2Type, hash, last_login, mdpValues);

            return "Done";
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    // Fonction qui prend en charge la mise à jour du mot de passe maître et ses conséquences :
    // Mettre à jour toutes les données cryptées avec la bonne clé et changer tout les IVs
    public String updateMasterPassword(String oldMasterPassword, String newMasterPassword) {
        try {
            // Dérivation du mot de passe maître saisi pour vérification
            byte[] salt = user.getSalt();
            int argon2Type = user.getArgon2Type();
            byte[] verifKey = Argon2.derivePassword(oldMasterPassword, salt, argon2Type);
            if(!java.util.Arrays.equals(verifKey, user.getKey())) {
                return "Wrong";
            }

            // Mise à jour des éléments de vérification du mot de passe maître
            // Dérivation du mot de passe maître
            byte[] newSalt = Argon2.generateSalt();
            byte[] hash = Argon2.derivePassword(newMasterPassword, newSalt, argon2Type);
            
            // Chiffrement du nom d'utilisateur pour authentification ultérieure
            SecretKey key = new SecretKeySpec(hash, "AES");
            byte[] iv = AES.generateIv();
    	    GCMParameterSpec gcmParameterSpec = AES.generateGCMParameterSpec(iv);
    	    String algorithm = "AES/GCM/NoPadding";
            String username = user.getUsername();
    	    String cipherText = AES.encrypt(algorithm, username, key, gcmParameterSpec);

            // Mise à jour des de la table users dans la BDD
            int userID = user.getUserID();
            sqlite.updateUserMdpVerif(userID, cipherText, iv, newSalt);

            // Mise à jour des données chiffrées et des IVs en fonction du nouveau mot de passe maître avec une boucle
            ArrayList<String> websiteNameList = user.getWebsiteNameList();
            for(int i = 0; i < websiteNameList.size(); i++) {
                // Récupération du passwordID et du nom du site duquel les données sont à modifier
                int passwordID = user.getPasswordId(i);
                String websiteName = user.getWebsiteName(i);

                // Décrypte le nom d'utilisateur et le mot de passe du site
                String[] decryptedUsername = giveUsername(websiteName);
                String[] decryptedPassword = givePassword(websiteName);
                
                // Renvoie le message d'erreur produit si le déchiffrement échoue 
                if(!decryptedUsername[0].equals("Done")) {
                    return decryptedUsername[0];
                } else if(!decryptedPassword[0].equals("Done")) {
                    return decryptedPassword[0];
                }

                // Chiffrement du nom d'utilisateur
                // Avec la nouvelle clé issue du nouveau mot de passe maître
                byte[] ivUsername = AES.generateIv();
    	        gcmParameterSpec = AES.generateGCMParameterSpec(ivUsername);
    	        algorithm = "AES/GCM/NoPadding";
    	        String encryptedUsername = AES.encrypt(algorithm, decryptedUsername[1], key, gcmParameterSpec);

                // Chiffrement du mot de passe
                // Avec la nouvelle clé issue du nouveau mot de passe maître
                byte[] ivPassword = AES.generateIv();
    	        gcmParameterSpec = AES.generateGCMParameterSpec(ivPassword);
    	        algorithm = "AES/GCM/NoPadding";
    	        String encryptedPassword = AES.encrypt(algorithm, decryptedPassword[1], key, gcmParameterSpec);

                sqlite.updateMdpAndUsername(passwordID, encryptedPassword, ivPassword, encryptedUsername, ivUsername);
            }

            // Recréation du user actualisé
            String usernameAccount = user.getUsername();
            String last_login = user.getLast_login();
            ArrayList<SQLite.MdpValues> mdpValues = sqlite.get_mdp(userID);
            user = new User(userID, usernameAccount, newSalt, argon2Type, hash, last_login, mdpValues);

            return "Done";
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    // Fonction qui supprime le compte
    public String deleteAccount() {
        try {
            // Supression des informations dans la base de données
            // Les mots de passes affiliés à l'utilisateur sont aussi supprimés grâce au 'DELETE ON CASCADE'
            sqlite.suppr_utilisateur(user.getUsername());
            user = new User(0, null, null, -1, null, null, null);
            return "Done";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Fonction qui met à jour la varaible last_login dans la BDD
    public void updateLastLogin() throws Exception {
        // Défintion de la date et de l'heure de connexion
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
        String formattedString = localDateTime.format(formatter);

        sqlite.update_last_login(formattedString, user.getUserID());
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
                else hasSpecial = true;
            }
        }
        return new boolean[] {hasMinuscule, hasMajuscule, hasChiffre, hasSpecial};
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
        while (newPassword.length() < 20) {
            newPassword.append(caracteresDisponibles.charAt(random.nextInt(caracteresDisponibles.length())));
        }

        return newPassword.toString();
    }

    // Fonction d'analyse d'un mot de passe (Longueur, types, temps de bruteforce)
    public String analysePassword(String password) {
        StringBuilder resultat = new StringBuilder();
        resultat.append("=================================\n");
        resultat.append("    Analyse du mot de passe\n");
        resultat.append("=================================\n");
        resultat.append("Longueur : ").append(password.length()).append(" caractères\n");
        
        boolean[] typesPresents = check(password);
        
        if (typesPresents[0]) {
            resultat.append("Minuscules : Oui\n");
        } else {
            resultat.append("Minuscules : Non\n");
        }
        
        if (typesPresents[1]) {
            resultat.append("Majuscules : Oui\n");
        } else {
            resultat.append("Majuscules : Non\n");
        }
        
        if (typesPresents[2]) {
            resultat.append("Chiffres : Oui\n");
        } else {
            resultat.append("Chiffres : Non\n");
        }
        
        if (typesPresents[3]) {
            resultat.append("Spéciaux : Oui\n");
        } else {
            resultat.append("Spéciaux : Non\n");
        }

        int charsetSize = 0;
        if (typesPresents[0]) {
            charsetSize += MINUSCULES.length();
        }
        if (typesPresents[1]) {
            charsetSize += MAJUSCULES.length();
        }
        if (typesPresents[2]) {
            charsetSize += CHIFFRES.length();
        }
        if (typesPresents[3]) {
            charsetSize += SPECIAUX.length();
        }
        
        if (charsetSize == 0) {
            charsetSize = 1; // Eviter log(0) dans le calcul de l'entropie
        }
        
        // Entropie en bits
        double entropy = password.length() * (Math.log(charsetSize) / Math.log(2));
        long entropyArrondie = Math.round(entropy * 100) / 100;
        resultat.append("Entropie : ").append(entropyArrondie).append(" bits\n");

        // Estimation du temps de craquage par bruteforce à ~10 milliards de hashs/sec
        double combinations = Math.pow(charsetSize, password.length());
        double seconds = combinations / 10000000000.0; 

        resultat.append("Temps estimé de craquage (Bruteforce à 10 milliards d'essais/s) : ");
        resultat.append(convertirDuree(seconds));
        
        // Force du mot de passe
        String RED = "\033[0;31m";
        String YELLOW = "\033[0;33m";
        String GREEN = "\033[0;32m";
        
        int score;
        String color;
        String force;
        
        if (estFaible(password) || entropy < 50) {
            score = 3;  // 3/10
            color = RED;
            force = "Faible";
        } else if (entropy < 80) {
            score = 6;  // 6/10
            color = YELLOW;
            force = "Moyen";
        } else {
            score = 10; // 10/10
            color = GREEN;
            force = "Fort";
        }
        
        // Affichage de la barre en ASCII
        resultat.append("\nForce : ").append(color).append("[");
        for (int i = 0; i < score; i++) {
            resultat.append("█");
        }
        for (int i = 0; i < 10 - score; i++) {
            resultat.append("░");
        }
        resultat.append("] ").append(force).append(GREEN).append("\n");

        return resultat.toString();
    }

    /*
    Fonction qui rend l'affichage des durées plus lisible en les convertissant.
    String.format("%.0f", valeur) arrondi à l'entier
    */
    private String convertirDuree(double seconds) {
        double minutes = seconds / 60;
        double hours = minutes / 60;
        double days = hours / 24;
        double years = days / 365;

        if (seconds < 1) {
            return "Moins d'une seconde";
        } else if (seconds < 60) {
            return String.format("%.0f secondes", seconds);
        } else if (minutes < 60) {
            return String.format("%.0f minutes", minutes);
        } else if (hours < 24) {
            return String.format("%.0f heures", hours);
        } else if (days < 365) {
            return String.format("%.0f jours", days);
        } else if (years < 1000000) {
            return String.format("%.0f années", years);
        } else if (years < 1000000000) {
            return String.format("%.0f millions d'années", years / 1000000);
        } else {
            return String.format("%.0f milliards d'années", years / 1000000000);
        }
    }

    // Fonction qui vérifie si un mot de passe est considéré comme trop faible
    public boolean estFaible(String password) {
        boolean[] typesPresents = check(password);
        if (password == null || password.length() < 12) {
            return true;
        } else if (!typesPresents[0] || !typesPresents[1] || !typesPresents[2] || !typesPresents[3]) {
            return true;
        } else {
            return false;
        }
    }

    // Fonction qui récupère les noms des sites qui utilisent exactement le même mot de passe
    public ArrayList<String> samePassword(String password, String actualWebsite) {
        ArrayList<String> websites = new ArrayList<>();
        ArrayList<String> allWebsites = returnWebsitesNames();
        
        if (allWebsites != null) {
            for (int i = 0; i < allWebsites.size(); i++) {
                String currentWebsite = allWebsites.get(i);
                
                if (currentWebsite.equals(actualWebsite) == false) {
                    String[] infos = givePasswordInfos(currentWebsite);
                    
                    if (infos != null) {
                        if (infos.length == 4) {
                            String passwordOfCurrentWebsite = infos[3];
                            
                            if (password.equals(passwordOfCurrentWebsite)) {
                                websites.add(currentWebsite);
                            }
                        }
                    }
                }
            }
        }
        return websites;
    }
    
    // Fonction qui met le programme en pause pendant le nombre de millisecondes
    public String wait(int millisecond) {
        try {
            Thread.sleep(millisecond);
            return "Done";
        } catch(InterruptedException e) {
            return e.getMessage();
        }
    }

    // Fonction qui initilise le compteur d'inactivité
    public void initializeTimer(Services services) {
        inactivityCounter.start(services);
    }

    // Fonction qui reset le compteur d'inactivité
    public void resetTimer() {
        inactivityCounter.resetTimer();
    }
}