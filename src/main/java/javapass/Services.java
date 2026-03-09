package javapass;

import java.security.SecureRandom;

public class Services {
    public static boolean authentification(String username, String password) {
        return true;
    }

    public static SecureRandom generateSecureRandom() {
        return new SecureRandom();
    }

    public static String generate(int taille, boolean minuscule, boolean majuscule, boolean chiffres, boolean speciaux) {
        
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
}
