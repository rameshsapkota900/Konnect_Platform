package com.konnect.servlet.monitoring;

import com.konnect.config.DatabaseConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * Health Check Servlet
 * Provides system health status including database connectivity
 */
@WebServlet("/health")
public class HealthCheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        try {
            // Check database connectivity
            boolean dbHealthy = checkDatabaseHealth();
            
            // Get pool statistics
            String poolStats = DatabaseConfig.getPoolStats();
            
            // Determine overall health status
            String status = dbHealthy ? "UP" : "DOWN";
            int httpStatus = dbHealthy ? 200 : 503;
            
            response.setStatus(httpStatus);
            
            // Build JSON response
            String jsonResponse = String.format(
                "{\n" +
                "  \"status\": \"%s\",\n" +
                "  \"timestamp\": \"%s\",\n" +
                "  \"components\": {\n" +
                "    \"database\": {\n" +
                "      \"status\": \"%s\",\n" +
                "      \"details\": {\n" +
                "        \"poolStats\": \"%s\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}",
                status,
                java.time.Instant.now().toString(),
                dbHealthy ? "UP" : "DOWN",
                poolStats
            );
            
            out.write(jsonResponse);
            
            logger.debug("Health check completed. Status: {}", status);
            
        } catch (Exception e) {
            logger.error("Health check failed: {}", e.getMessage(), e);
            response.setStatus(503);
            out.write("{\"status\":\"DOWN\",\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private boolean checkDatabaseHealth() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            // Simple connectivity test
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            logger.error("Database health check failed: {}", e.getMessage());
            return false;
        }
    }
}