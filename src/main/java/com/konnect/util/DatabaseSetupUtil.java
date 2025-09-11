package com.konnect.util;

import com.konnect.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Database Setup Utility
 * Creates database schema and populates initial data
 */
public class DatabaseSetupUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSetupUtil.class);
    
    public static void main(String[] args) {
        try {
            setupDatabase();
        } catch (Exception e) {
            logger.error("Failed to setup database", e);
            System.exit(1);
        }
    }
    
    public static void setupDatabase() throws Exception {
        logger.info("Starting database setup...");
        
        // Read SQL script
        String sqlScript = readSqlScript();
        if (sqlScript == null || sqlScript.trim().isEmpty()) {
            throw new RuntimeException("SQL script is empty or could not be read");
        }
        
        // Execute SQL script
        try (Connection connection = DatabaseConfig.getConnection()) {
            
            // Check if tables already exist
            if (tablesExist(connection)) {
                logger.info("Database tables already exist. Skipping schema creation.");
                return;
            }
            
            // Split script into individual statements
            String[] statements = sqlScript.split(";");
            
            try (Statement stmt = connection.createStatement()) {
                int executedCount = 0;
                
                for (String sql : statements) {
                    String trimmedSql = sql.trim();
                    // Skip empty statements, comments, and whitespace-only statements
                    if (!trimmedSql.isEmpty() && 
                        !trimmedSql.startsWith("--") && 
                        !trimmedSql.startsWith("/*") &&
                        trimmedSql.length() > 5) {  // Minimum meaningful SQL length
                        try {
                            stmt.execute(trimmedSql);
                            executedCount++;
                            logger.debug("Executed SQL statement: {}", 
                                       trimmedSql.length() > 50 ? trimmedSql.substring(0, 50) + "..." : trimmedSql);
                        } catch (Exception e) {
                            logger.warn("Failed to execute SQL: {} - Error: {}", 
                                      trimmedSql.length() > 100 ? trimmedSql.substring(0, 100) + "..." : trimmedSql, 
                                      e.getMessage());
                            // Continue with other statements
                        }
                    }
                }
                
                logger.info("Database setup completed successfully. Executed {} SQL statements.", executedCount);
                
                // Verify setup
                verifySetup(connection);
                
            }
        }
    }
    
    private static String readSqlScript() {
        try (InputStream inputStream = DatabaseSetupUtil.class.getClassLoader()
                .getResourceAsStream("sql/konnect_simple.sql")) {
            
            if (inputStream == null) {
                logger.error("SQL script file not found: sql/konnect_simple.sql");
                return null;
            }
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String content = reader.lines().collect(Collectors.joining("\n"));
                logger.info("Successfully read SQL script ({} characters)", content.length());
                return content;
            }
            
        } catch (Exception e) {
            logger.error("Error reading SQL script", e);
            return null;
        }
    }
    
    private static boolean tablesExist(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeQuery("SELECT 1 FROM users LIMIT 1");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static void verifySetup(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            // Check if users table has data
            var rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (rs.next()) {
                int userCount = rs.getInt("count");
                logger.info("Database verification: Found {} users in the database", userCount);
                
                if (userCount > 0) {
                    // Check for admin user
                    var adminRs = stmt.executeQuery("SELECT email, role FROM users WHERE role = 'admin' LIMIT 1");
                    if (adminRs.next()) {
                        String adminEmail = adminRs.getString("email");
                        logger.info("Admin user found: {}", adminEmail);
                    } else {
                        logger.warn("No admin user found in the database");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Database verification failed", e);
        }
    }
}