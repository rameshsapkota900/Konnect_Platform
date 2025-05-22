package com.konnect.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * Utility class for password hashing and verification
 */
public class PasswordUtil {
    
    /**
     * Hash a password using SHA-256
     * 
     * @param password The password to hash
     * @return The hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verify if a password matches a hash
     * 
     * @param password The password to verify
     * @param hash The hash to verify against
     * @return True if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }
    
    /**
     * Generate a random verification code
     * 
     * @param length The length of the code
     * @return A random verification code
     */
    public static String generateVerificationCode(int length) {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Digits 0-9
        }
        
        return sb.toString();
    }
    
    /**
     * Generate a random token
     * 
     * @param length The length of the token in bytes (actual string will be longer due to Base64 encoding)
     * @return A random token
     */
    public static String generateToken(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
