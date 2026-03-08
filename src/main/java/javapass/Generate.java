package javapass;
import java.security.SecureRandom;

public class Generate {
    
    // Listes des caractères disponibles
    private static final String MINUSCULES = "abcdefghijklmnopqrstuvwxyz";
    private static final String MAJUSCULES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHIFFRES = "0123456789";
    private static final String SPECIAUX = "!@#$%^&*()_+€£µ§?/|{}[]'\"";

    private static final int TAILLE_PAR_DEFAUT = 20;
    private static final int TAILLE_MINIMUM = 12;

    private static final SecureRandom RANDOM = new SecureRandom();

    // Un mot de passe ne peut pas être plus petit que TAILLE_MINIMUM si c'est le cas on prend la TAILLE_PAR_DEFAUT
    public static String generate(int taille, boolean minuscule, boolean majuscule, boolean chiffres, boolean speciaux) {
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

        //Au moins un caractère de chaque catégorie sélectionnée
        if (minuscule) motDePasse[position++] = MINUSCULES.charAt(RANDOM.nextInt(MINUSCULES.length()));
        if (majuscule) motDePasse[position++] = MAJUSCULES.charAt(RANDOM.nextInt(MAJUSCULES.length()));
        if (chiffres) motDePasse[position++] = CHIFFRES.charAt(RANDOM.nextInt(CHIFFRES.length()));
        if (speciaux) motDePasse[position++] = SPECIAUX.charAt(RANDOM.nextInt(SPECIAUX.length()));

        for (int i = position; i < taille; i++) {
            motDePasse[i] = choix.charAt(RANDOM.nextInt(choix.length()));
        }

        for (int i = taille - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = motDePasse[i];
            motDePasse[i] = motDePasse[j];
            motDePasse[j] = temp;
        }

        return new String(motDePasse);
    }
}
