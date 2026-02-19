package javapass;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class Argon2 {

    private static byte[] generateSalt16Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        
        return salt;
    }

    public static byte[][] derivePassword(String password, String userType) {
        byte[] salt = generateSalt16Byte();

        int iterations;
        int memLimit;
        int hashLength;
        int parallelism;
        int argon2Type;

        if (userType.equals("PC")) {
            iterations = 1;
            memLimit = 2097152;
            hashLength = 32;
            parallelism = 4;
            argon2Type = Argon2Parameters.ARGON2_d;
        } else if (userType.equals("Server")) {
            iterations = 4;
            // 128mio pour ne pas surcharger le serveur en cas de connexions multiples (=134mo de RAM)
            memLimit = 131072;
            hashLength = 32;
            parallelism = 4;
            argon2Type = Argon2Parameters.ARGON2_id;
        } else {
            iterations = 4;
            // 128mio pour ne pas surcharger les appareils bas de gamme (=134mo de RAM)
            memLimit = 131072;
            hashLength = 32;
            parallelism = 4;
            argon2Type = Argon2Parameters.ARGON2_id;
        }
        
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(argon2Type)
        .withVersion(Argon2Parameters.ARGON2_VERSION_13)
        .withIterations(iterations)
        .withMemoryAsKB(memLimit)
        .withParallelism(parallelism)
        .withSalt(salt);
        
        Argon2BytesGenerator generate = new Argon2BytesGenerator();
        generate.init(builder.build());
        byte[] hashbytes = new byte[hashLength];
        generate.generateBytes(password.getBytes(StandardCharsets.UTF_8), hashbytes, 0, hashbytes.length);

        return new byte[][]{hashbytes, salt};
    }
}
