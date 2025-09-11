package com.konnect.util;

import com.konnect.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database Connection Utility Class
 * Provides methods to establish and manage database connections
 * @deprecated Use DatabaseConfig directly for new code
 */
@Deprecated
public class DBConnection {
    
    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
    
    /**
     * Get a connection to the database using connection pool
     * @return Connection object from the pool
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        logger.debug("Obtaining database connection through legacy DBConnection class");
        return DatabaseConfig.getConnection();
    }
    
    /**
     * Get the DataSource for advanced usage
     * @return DataSource object
     */
    public static DataSource getDataSource() {
        return DatabaseConfig.getDataSource();
    }
    
    /**
     * Close the database connection (returns it to the pool)
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        DatabaseConfig.closeConnection(connection);
    }
    
    /**
     * Get connection pool statistics for monitoring
     * @return Connection pool statistics
     */
    public static String getPoolStats() {
        return DatabaseConfig.getPoolStats();
    }
}
