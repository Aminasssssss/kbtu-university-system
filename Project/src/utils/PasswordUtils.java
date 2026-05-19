package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password hashing using SHA-256.
 * Passwords are NEVER stored in plain text.
 *
 * Usage:
 *   String hashed = PasswordUtils.hash("myPassword");
 *   boolean ok     = PasswordUtils.verify("myPassword", hashed);
 */
public class PasswordUtils {

    private PasswordUtils() {} // no instances, just static methods

    /**
     * Hashes a raw password using SHA-256.
     * @param rawPassword plain text password
     * @return hex-encoded SHA-256 hash
     */
    public static String hash(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available on this JVM. This should never happen.", e);
        }
    }

    /**
     * Verifies a raw password against a stored hash.
     * @param rawPassword  the password entered by the user
     * @param storedHash   the hash stored in the database
     * @return true if they match
     */
    public static boolean verify(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }
        return hash(rawPassword).equals(storedHash);
    }
}
