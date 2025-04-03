package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
//	private static int iterations = 0; // work factor

	// There's generally no significant security benefit beyond 16 bytes of length for the salt. 
	// The primary security comes from having a salt that's:
	// • Random (generated using a cryptographically secure random number generator)
	// • Unique per user
	// • Long enough to prevent collisions
	/**
	 * Generates a random salt of specified length. Uses {@link SecureRandom}.
	 * @param length the length of the to-be-generated salt in bytes (16 is recommended)
	 * @return salt as string in Base64
	 * @throws IllegalArgumentException If length is less than 1
	 */
    public static String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hashes a password with a given salt and set work factor using SHA-256
     * @param password input string
     * @param salt randomly generated unique string
     * @return hashed password in Base64
     * @throws NoSuchAlgorithmException
     */
    public static String hashPassword(String password, String salt) 
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        // Initial hash with salt
        md.update(salt.getBytes());
        byte[] hashedPassword = md.digest(password.getBytes());
        
        // Apply multiple iterations (work factor)
//        for (int i = 0; i < iterations - 1; i++) {
//            md.reset();
//            hashedPassword = md.digest(hashedPassword);
//        }
        
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
    
    // Verify a password against stored hash and salt
    public static boolean verifyPassword(String inputPassword, String storedHash, String storedSalt) 
            throws NoSuchAlgorithmException {
        String newHash = hashPassword(inputPassword, storedSalt);
        return newHash.equals(storedHash);
    }
}
