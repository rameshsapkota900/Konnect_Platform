package com.konnect.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Enhanced Database Configuration Class
 * Provides environment-based database configuration with connection pooling
 * and proper error handling
 */
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final String CONFIG_FILE = "/database.properties";
    private static Properties properties;
    private static HikariDataSource dataSource;
    
    static {
        loadProperties();
        initializeConnectionPool();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        
        try (InputStream input = DatabaseConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
                logger.info("Database properties loaded successfully from {}", CONFIG_FILE);
            } else {
                logger.warn("Database properties file not found: {}. Using default values.", CONFIG_FILE);
                setDefaultProperties();
            }
        } catch (IOException e) {
            logger.error("Error loading database properties from {}: {}", CONFIG_FILE, e.getMessage());
            setDefaultProperties();
        }
    }
    
    private static void setDefaultProperties() {
        properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/Konnect_Platform");
        properties.setProperty("db.username", "postgres");
        properties.setProperty("db.password", "12345");
        properties.setProperty("db.driver", "org.postgresql.Driver");
        
        // Connection pool settings
        properties.setProperty("db.pool.maximum-pool-size", "20");
        properties.setProperty("db.pool.minimum-idle", "5");
        properties.setProperty("db.pool.connection-timeout", "30000");
        properties.setProperty("db.pool.idle-timeout", "600000");
        properties.setProperty("db.pool.max-lifetime", "1800000");
        
        logger.info("Default database properties set");
    }
    
    /**
     * Initialize HikariCP connection pool
     */
    private static void initializeConnectionPool() {
        try {
            HikariConfig config = new HikariConfig();
            
            // Basic connection settings
            config.setJdbcUrl(getUrl());
            config.setUsername(getUsername());
            config.setPassword(getPassword());
            config.setDriverClassName(getDriver());
            
            // Connection pool settings
            config.setMaximumPoolSize(Integer.parseInt(
                properties.getProperty("db.pool.maximum-pool-size", "20")));
            config.setMinimumIdle(Integer.parseInt(
                properties.getProperty("db.pool.minimum-idle", "5")));
            config.setConnectionTimeout(Long.parseLong(
                properties.getProperty("db.pool.connection-timeout", "30000")));
            config.setIdleTimeout(Long.parseLong(
                properties.getProperty("db.pool.idle-timeout", "600000")));
            config.setMaxLifetime(Long.parseLong(
                properties.getProperty("db.pool.max-lifetime", "1800000")));
            
            // Performance settings
            config.setLeakDetectionThreshold(60000);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            
            // Pool name for monitoring
            config.setPoolName("KonnectPool");
            
            dataSource = new HikariDataSource(config);
            
            // Test the connection
            try (Connection testConnection = dataSource.getConnection()) {
                logger.info("Database connection pool initialized successfully. Pool name: {}", 
                    config.getPoolName());
            }
            
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool: {}", e.getMessage(), e);
            throw new RuntimeException("Database configuration failed", e);
        }
    }
    
    public static String getUrl() {
        return properties.getProperty("db.url");
    }
    
    public static String getUsername() {
        return properties.getProperty("db.username");
    }
    
    public static String getPassword() {
        return properties.getProperty("db.password");
    }
    
    public static String getDriver() {
        return properties.getProperty("db.driver");
    }
    
    /**
     * Get the HikariCP DataSource
     * @return DataSource object
     */
    public static DataSource getDataSource() {
        if (dataSource == null || dataSource.isClosed()) {
            logger.error("DataSource is not available or has been closed");
            throw new RuntimeException("Database connection pool is not available");
        }
        return dataSource;
    }
    
    /**
     * Get a connection from the connection pool
     * @return Connection object from the pool
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            logger.debug("Connection obtained from pool. Active connections: {}", 
                dataSource.getHikariPoolMXBean().getActiveConnections());
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to obtain database connection: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Close the database connection (returns it to the pool)
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close(); // This returns the connection to the pool
                logger.debug("Connection returned to pool");
            } catch (SQLException e) {
                logger.error("Error closing connection: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Get connection pool statistics for monitoring
     * @return Connection pool statistics
     */
    public static String getPoolStats() {
        if (dataSource != null && !dataSource.isClosed()) {
            return String.format(
                "Pool Stats - Active: %d, Idle: %d, Total: %d, Waiting: %d",
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getTotalConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()
            );
        }
        return "Pool not available";
    }
    
    /**
     * Shutdown the connection pool gracefully
     */
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Shutting down database connection pool...");
            dataSource.close();
            logger.info("Database connection pool shutdown completed");
        }
    }
}