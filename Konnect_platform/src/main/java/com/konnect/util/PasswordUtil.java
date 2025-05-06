package com.konnect.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Utility class for password encryption and validation
 */
public class PasswordUtil {
    
    /**
     * Encrypt password using SHA-256 algorithm
     * @param password Password to encrypt
     * @param salt Salt for encryption (can be null for no salt)
     * @return Encrypted password
     */
    public static String encryptPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Add salt if provided
            if (salt != null && !salt.isEmpty()) {
                md.update(salt.getBytes());
            }
            
            // Add password bytes to digest
            md.update(password.getBytes());
            
            // Get the hashed bytes
            byte[] hashedBytes = md.digest();
            
            // Convert to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }
    
    /**
     * Generate a random salt
     * @return Random salt string
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();
    }
    
    /**
     * Verify if a password matches the encrypted password
     * @param password Plain text password
     * @param encryptedPassword Encrypted password
     * @param salt Salt used for encryption
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String encryptedPassword, String salt) {
        String encryptedInput = encryptPassword(password, salt);
        return encryptedInput.equals(encryptedPassword);
    }
}
