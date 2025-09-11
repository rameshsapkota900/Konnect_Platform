package com.konnect.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * Utility class for secure password hashing and verification
 * Uses BCrypt for new passwords while maintaining backward compatibility with SHA-256
 */
public class PasswordUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private static final String BCRYPT_PREFIX = "$2a$";
    
    /**
     * Hash a password using BCrypt (recommended for new passwords)
     * 
     * @param password The password to hash
     * @return The BCrypt hashed password
     */
    public static String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        try {
            String hashedPassword = passwordEncoder.encode(password);
            logger.debug("Password hashed successfully using BCrypt");
            return hashedPassword;
        } catch (Exception e) {
            logger.error("Error hashing password with BCrypt", e);
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verify if a password matches a hash (supports both BCrypt and legacy SHA-256)
     * 
     * @param password The password to verify
     * @param hash The hash to verify against
     * @return True if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String password, String hash) {
        if (password == null || hash == null) {
            return false;
        }
        
        try {
            // Check if it's a BCrypt hash
            if (hash.startsWith(BCRYPT_PREFIX)) {
                boolean matches = passwordEncoder.matches(password, hash);
                logger.debug("Password verification using BCrypt: {}", matches ? "SUCCESS" : "FAILED");
                return matches;
            } else {
                // Legacy SHA-256 verification (for backward compatibility)
                String sha256Hash = hashPasswordSHA256(password);
                boolean matches = sha256Hash.equals(hash);
                logger.debug("Password verification using legacy SHA-256: {}", matches ? "SUCCESS" : "FAILED");
                
                if (matches) {
                    logger.warn("User authenticated with legacy SHA-256 hash. Consider migrating to BCrypt.");
                }
                
                return matches;
            }
        } catch (Exception e) {
            logger.error("Error verifying password", e);
            return false;
        }
    }
    
    /**
     * Check if a hash is using the legacy SHA-256 format
     * 
     * @param hash The hash to check
     * @return True if it's a legacy SHA-256 hash, false if BCrypt
     */
    public static boolean isLegacyHash(String hash) {
        return hash != null && !hash.startsWith(BCRYPT_PREFIX);
    }
    
    /**
     * Migrate a password from SHA-256 to BCrypt
     * This should be called during login when a user with legacy hash successfully authenticates
     * 
     * @param password The plain text password
     * @return The new BCrypt hash
     */
    public static String migrateLegacyPassword(String password) {
        logger.info("Migrating legacy password to BCrypt");
        return hashPassword(password);
    }
    
    /**
     * Legacy SHA-256 hashing (for backward compatibility only)
     * 
     * @param password The password to hash
     * @return The SHA-256 hashed password
     * @deprecated Use hashPassword() instead for new passwords
     */
    @Deprecated
    private static String hashPasswordSHA256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("SHA-256 algorithm not available", e);
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Validate password strength
     * 
     * @param password The password to validate
     * @return True if password meets security requirements
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
    /**
     * Get password strength requirements message
     * 
     * @return String describing password requirements
     */
    public static String getPasswordRequirements() {
        return "Password must be at least 8 characters long and contain: " +
               "uppercase letter, lowercase letter, digit, and special character";
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
