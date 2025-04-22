package com.konnect.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/konnect_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = ""; 
    // --- --- --- --- --- --- --- --- --- --- --- --- ---

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            // Load the JDBC driver
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL JDBC driver.");
            e.printStackTrace();
            // Consider throwing a RuntimeException to halt initialization if driver is critical
            // throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void main(String[] args) {
        // Test connection
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Database connection successful!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed:");
            e.printStackTrace();
        }
    }
}
