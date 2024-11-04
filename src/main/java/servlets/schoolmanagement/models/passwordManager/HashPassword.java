package servlets.schoolmanagement.models.passwordManager;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class HashPassword {

    // Paramètres de hachage
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;

    // Méthode pour générer le hachage du mot de passe
    public static String hashPassword(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing a password: " + e.getMessage(), e);
        }
    }

    // Méthode pour générer un sel aléatoire
    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new java.security.SecureRandom().nextBytes(salt);
        return salt;
    }
    public static boolean verifyPassword(String providedPassword, String storedHashedPassword, String storedSalt) {
        // Generate hash of provided password using stored salt
        String hashedProvidedPassword = HashPassword.hashPassword(providedPassword, Base64.getDecoder().decode(storedSalt));

        // Compare the two hashes
        return hashedProvidedPassword.equals(storedHashedPassword);
    }


}

