package com.konnect.util;

import com.konnect.dao.UserDAO;
import com.konnect.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Service for migrating user passwords from SHA-256 to BCrypt
 * This service handles the gradual migration of legacy password hashes
 */
public class PasswordMigrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordMigrationService.class);
    private final UserDAO userDAO;
    
    public PasswordMigrationService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Attempt to migrate a user's password during login
     * This method should be called when a user successfully logs in with a legacy hash
     * 
     * @param userId The user's ID
     * @param plainTextPassword The user's plain text password
     * @return True if migration was successful, false otherwise
     */
    public boolean migrateUserPassword(int userId, String plainTextPassword) {
        try {
            // Get the current user
            User user = userDAO.getById(userId);
            if (user == null) {
                logger.warn("Cannot migrate password: User with ID {} not found", userId);
                return false;
            }
            
            // Check if password is already using BCrypt
            if (!PasswordUtil.isLegacyHash(user.getPassword())) {
                logger.debug("User {} already has BCrypt password", userId);
                return true;
            }
            
            // Generate new BCrypt hash
            String newHash = PasswordUtil.migrateLegacyPassword(plainTextPassword);
            
            // Update user's password in database
            boolean success = userDAO.updateUserPassword(userId, newHash);
            
            if (success) {
                logger.info("Successfully migrated password for user ID: {}", userId);
                return true;
            } else {
                logger.error("Failed to update password in database for user ID: {}", userId);
                return false;
            }
            
        } catch (SQLException e) {
            logger.error("Database error during password migration for user ID: {}", userId, e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during password migration for user ID: {}", userId, e);
            return false;
        }
    }
    
    /**
     * Check if a user needs password migration
     * 
     * @param userId The user's ID
     * @return True if user needs migration, false otherwise
     */
    public boolean needsMigration(int userId) {
        try {
            User user = userDAO.getById(userId);
            return user != null && PasswordUtil.isLegacyHash(user.getPassword());
        } catch (Exception e) {
            logger.error("Error checking migration status for user ID: {}", userId, e);
            return false;
        }
    }
    
    /**
     * Get statistics about password migration
     * 
     * @return Migration statistics
     */
    public MigrationStats getMigrationStats() {
        try {
            int totalUsers = userDAO.getTotalUserCount();
            int legacyUsers = userDAO.getLegacyPasswordCount();
            int migratedUsers = totalUsers - legacyUsers;
            
            return new MigrationStats(totalUsers, migratedUsers, legacyUsers);
        } catch (SQLException e) {
            logger.error("Error getting migration statistics", e);
            return new MigrationStats(0, 0, 0);
        }
    }
    
    /**
     * Statistics class for password migration
     */
    public static class MigrationStats {
        private final int totalUsers;
        private final int migratedUsers;
        private final int legacyUsers;
        
        public MigrationStats(int totalUsers, int migratedUsers, int legacyUsers) {
            this.totalUsers = totalUsers;
            this.migratedUsers = migratedUsers;
            this.legacyUsers = legacyUsers;
        }
        
        public int getTotalUsers() { return totalUsers; }
        public int getMigratedUsers() { return migratedUsers; }
        public int getLegacyUsers() { return legacyUsers; }
        
        public double getMigrationProgress() {
            return totalUsers > 0 ? (double) migratedUsers / totalUsers * 100 : 0;
        }
        
        @Override
        public String toString() {
            return String.format("Migration Stats: %d/%d users migrated (%.1f%%), %d legacy users remaining",
                    migratedUsers, totalUsers, getMigrationProgress(), legacyUsers);
        }
    }
}