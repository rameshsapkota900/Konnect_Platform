package com.konnect.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

// Basic password hashing utility (For real applications, use a stronger library like BCrypt)
public class PasswordUtil {

    // This is NOT production-ready security. 
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash); // Store hash as Base64 string
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage());
            throw new RuntimeException("Password hashing failed", e); // Critical failure
        }
    }

    public static boolean checkPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        String hashOfInput = hashPassword(plainPassword);
        return hashOfInput.equals(storedHash);
    }

     // --- Main method to generate a hash for the initial admin user ---
     public static void main(String[] args) {
         String passwordToHash = "adminpassword"; // Change this to your desired admin password
         String hashedPassword = hashPassword(passwordToHash);
         System.out.println("Password: " + passwordToHash);
         System.out.println("Hashed (SHA-256, Base64): " + hashedPassword);
         System.out.println("Use this hash in your SQL INSERT statement for the admin user.");

         // Example check:
         // System.out.println("Check 'adminpassword': " + checkPassword("adminpassword", hashedPassword)); // true
         // System.out.println("Check 'wrongpassword': " + checkPassword("wrongpassword", hashedPassword)); // false
     }
}
