package javapass;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class Argon2 {

    // Fonction qui génère et retourne un sel de 16bytes (= 128bits)
    public static byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        
        return salt;
    }

    public static byte[] derivePassword(String password, byte[] salt, int userType) {

        int iterations; // Nombre de fois qu'Argon2 modifie tout les blocs mémoire
        int memAsk; // Quantité de mémoire allouée
        int hashLength; // Longueur du hash demandé
        int parallelism; // Nombre de coeurs alloués
        int argon2Type; // Variante d'Argon2 utilisée (d, i ou id)

        if (userType == 0) {
            // 1 => PC puissant strictement personnel
            // Paramètres de hash de la norme RFC_9106_HIGH_MEMORY pour Argon2d
            iterations = 1;
            memAsk = 2097152; // 2Gio = 2.14Go
            hashLength = 32; // 32 bytes = 256 bits
            parallelism = 4;
            argon2Type = Argon2Parameters.ARGON2_d;
        } else if (userType == 2) {
            // 2 => Serveur
            // Paramètres de hash supérieur à la norme RFC_9106_LOW_MEMORY
            iterations = 4; // 4 itérations au lieu de 3
            // 128mio au lieu de 64mio afin d'augmenter la sécurité sans toutefois
            // surcharger le serveur en cas de connexions multiples (=134mo de RAM)
            memAsk = 131072; // 128mio au lieu de 64 (128mio = 131072kio)
            hashLength = 32;
            // Nombre coeurs inférieurs afin de ne pas monopoliser les coeurs du processeur 
            // si plusieurs personnes utilisent JavaPass
            parallelism = 2;
            argon2Type = Argon2Parameters.ARGON2_id;
        } else {
            // 3 => Autres configurations
            // Paramètres de hash supérieur à la norme RFC_9106_LOW_MEMORY
            iterations = 4; // 4 itérations au lieu de 3
            // 128mio au lieu de 64mio afin d'augmenter la sécurité sans toutefois monopoliser
            // trop de ressources sur une machine potentiellement ancienne (=134mo de RAM)
            memAsk = 131072;
            hashLength = 32;
            parallelism = 4;
            argon2Type = Argon2Parameters.ARGON2_id;
        }
        
        // Création du constructeur du hash Argon2
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(argon2Type)
        .withVersion(Argon2Parameters.ARGON2_VERSION_13)
        .withIterations(iterations)
        .withMemoryAsKB(memAsk)
        .withParallelism(parallelism)
        .withSalt(salt);
        
        // Création du hash
        Argon2BytesGenerator generate = new Argon2BytesGenerator();
        generate.init(builder.build());
        byte[] hashbytes = new byte[hashLength];
        generate.generateBytes(password.getBytes(StandardCharsets.UTF_8), hashbytes, 0, hashbytes.length);

        return hashbytes;
    }
}
