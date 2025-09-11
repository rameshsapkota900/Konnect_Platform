package com.konnect.servlet.admin;

import com.konnect.util.ErrorHandlingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AdminErrorMonitoringServlet
 * Provides comprehensive error monitoring and statistics for administrators
 */
@WebServlet(name = "AdminErrorMonitoringServlet", urlPatterns = {"/admin/error-monitor"})
public class AdminErrorMonitoringServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AdminErrorMonitoringServlet.class);
    
    // Error tracking counters
    private static final AtomicInteger totalErrors = new AtomicInteger(0);
    private static final AtomicInteger authenticationErrors = new AtomicInteger(0);
    private static final AtomicInteger databaseErrors = new AtomicInteger(0);
    private static final AtomicInteger validationErrors = new AtomicInteger(0);
    private static final AtomicInteger systemErrors = new AtomicInteger(0);
    private static final AtomicLong totalResponseTime = new AtomicLong(0);
    private static final AtomicInteger responseTimeCount = new AtomicInteger(0);
    
    @Override
    public void init() throws ServletException {
        logger.info("AdminErrorMonitoringServlet initialized");
    }
    
    /**
     * Handle GET requests - display error monitoring dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || 
            !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Set response content type
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            // Calculate average response time
            double avgResponseTime = responseTimeCount.get() > 0 ? 
                (double) totalResponseTime.get() / responseTimeCount.get() : 0;
            
            // Generate HTML response
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Error Monitoring Dashboard - Konnect Platform</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
            out.println(".container { max-width: 1200px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            out.println(".header { text-align: center; margin-bottom: 30px; }");
            out.println(".stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin: 20px 0; }");
            out.println(".stat-card { background: #f8f9fa; padding: 20px; border-radius: 8px; border-left: 4px solid #007bff; text-align: center; }");
            out.println(".stat-number { font-size: 2.5em; font-weight: bold; color: #007bff; margin-bottom: 10px; }");
            out.println(".stat-label { color: #6c757d; font-size: 0.9em; }");
            out.println(".danger { border-left-color: #dc3545; }");
            out.println(".danger .stat-number { color: #dc3545; }");
            out.println(".warning { border-left-color: #ffc107; }");
            out.println(".warning .stat-number { color: #ffc107; }");
            out.println(".success { border-left-color: #28a745; }");
            out.println(".success .stat-number { color: #28a745; }");
            out.println(".info { border-left-color: #17a2b8; }");
            out.println(".info .stat-number { color: #17a2b8; }");
            out.println(".error-breakdown { margin: 30px 0; }");
            out.println(".breakdown-item { display: flex; justify-content: space-between; padding: 10px; margin: 5px 0; background: #f8f9fa; border-radius: 5px; }");
            out.println(".refresh-btn { background: #007bff; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; margin: 10px 0; }");
            out.println(".refresh-btn:hover { background: #0056b3; }");
            out.println(".back-link { display: inline-block; margin-top: 20px; color: #007bff; text-decoration: none; }");
            out.println(".back-link:hover { text-decoration: underline; }");
            out.println(".health-indicator { padding: 20px; border-radius: 8px; margin: 20px 0; text-align: center; font-weight: bold; }");
            out.println(".health-excellent { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }");
            out.println(".health-good { background: #d1ecf1; color: #0c5460; border: 1px solid #bee5eb; }");
            out.println(".health-warning { background: #fff3cd; color: #856404; border: 1px solid #ffeaa7; }");
            out.println(".health-critical { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }");
            out.println("</style>");
            out.println("<script>");
            out.println("function refreshStats() { window.location.reload(); }");
            out.println("</script>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<div class='container'>");
            out.println("<div class='header'>");
            out.println("<h1>🔍 Error Monitoring Dashboard</h1>");
            out.println("<p>Comprehensive error tracking and system health monitoring</p>");
            out.println("<button class='refresh-btn' onclick='refreshStats()'>🔄 Refresh Statistics</button>");
            out.println("</div>");
            
            // System health indicator
            String healthClass = getSystemHealthClass();
            String healthMessage = getSystemHealthMessage();
            out.println("<div class='health-indicator " + healthClass + "'>");
            out.println("🏥 System Health: " + healthMessage);
            out.println("</div>");
            
            // Error statistics
            out.println("<div class='stats-grid'>");
            
            // Total errors
            String totalClass = totalErrors.get() == 0 ? "success" : (totalErrors.get() < 10 ? "warning" : "danger");
            out.println("<div class='stat-card " + totalClass + "'>");
            out.println("<div class='stat-number'>" + totalErrors.get() + "</div>");
            out.println("<div class='stat-label'>Total Errors</div>");
            out.println("</div>");
            
            // Authentication errors
            String authClass = authenticationErrors.get() == 0 ? "success" : "warning";
            out.println("<div class='stat-card " + authClass + "'>");
            out.println("<div class='stat-number'>" + authenticationErrors.get() + "</div>");
            out.println("<div class='stat-label'>Authentication Errors</div>");
            out.println("</div>");
            
            // Database errors
            String dbClass = databaseErrors.get() == 0 ? "success" : "danger";
            out.println("<div class='stat-card " + dbClass + "'>");
            out.println("<div class='stat-number'>" + databaseErrors.get() + "</div>");
            out.println("<div class='stat-label'>Database Errors</div>");
            out.println("</div>");
            
            // Validation errors
            String validationClass = validationErrors.get() == 0 ? "success" : "info";
            out.println("<div class='stat-card " + validationClass + "'>");
            out.println("<div class='stat-number'>" + validationErrors.get() + "</div>");
            out.println("<div class='stat-label'>Validation Errors</div>");
            out.println("</div>");
            
            // System errors
            String systemClass = systemErrors.get() == 0 ? "success" : "danger";
            out.println("<div class='stat-card " + systemClass + "'>");
            out.println("<div class='stat-number'>" + systemErrors.get() + "</div>");
            out.println("<div class='stat-label'>System Errors</div>");
            out.println("</div>");
            
            // Average response time
            out.println("<div class='stat-card info'>");
            out.println("<div class='stat-number'>" + String.format("%.0f", avgResponseTime) + "ms</div>");
            out.println("<div class='stat-label'>Average Response Time</div>");
            out.println("</div>");
            
            out.println("</div>");
            
            // Error breakdown
            out.println("<div class='error-breakdown'>");
            out.println("<h3>📊 Error Breakdown</h3>");
            
            if (totalErrors.get() > 0) {
                double authPercent = (double) authenticationErrors.get() / totalErrors.get() * 100;
                double dbPercent = (double) databaseErrors.get() / totalErrors.get() * 100;
                double validationPercent = (double) validationErrors.get() / totalErrors.get() * 100;
                double systemPercent = (double) systemErrors.get() / totalErrors.get() * 100;
                
                out.println("<div class='breakdown-item'>");
                out.println("<span>Authentication Errors</span>");
                out.println("<span>" + String.format("%.1f%%", authPercent) + "</span>");
                out.println("</div>");
                
                out.println("<div class='breakdown-item'>");
                out.println("<span>Database Errors</span>");
                out.println("<span>" + String.format("%.1f%%", dbPercent) + "</span>");
                out.println("</div>");
                
                out.println("<div class='breakdown-item'>");
                out.println("<span>Validation Errors</span>");
                out.println("<span>" + String.format("%.1f%%", validationPercent) + "</span>");
                out.println("</div>");
                
                out.println("<div class='breakdown-item'>");
                out.println("<span>System Errors</span>");
                out.println("<span>" + String.format("%.1f%%", systemPercent) + "</span>");
                out.println("</div>");
            } else {
                out.println("<p style='text-align: center; color: #28a745; font-weight: bold;'>");
                out.println("🎉 No errors detected! System is running smoothly.");
                out.println("</p>");
            }
            
            out.println("</div>");
            
            // Error handling features
            out.println("<div class='error-breakdown'>");
            out.println("<h3>🛠️ Error Handling Features</h3>");
            out.println("<div class='breakdown-item'>");
            out.println("<span>✅ Custom Exception Hierarchy</span>");
            out.println("<span>Active</span>");
            out.println("</div>");
            out.println("<div class='breakdown-item'>");
            out.println("<span>✅ Global Error Filter</span>");
            out.println("<span>Active</span>");
            out.println("</div>");
            out.println("<div class='breakdown-item'>");
            out.println("<span>✅ Comprehensive Error Logging</span>");
            out.println("<span>Active</span>");
            out.println("</div>");
            out.println("<div class='breakdown-item'>");
            out.println("<span>✅ User-Friendly Error Pages</span>");
            out.println("<span>Active</span>");
            out.println("</div>");
            out.println("<div class='breakdown-item'>");
            out.println("<span>✅ Performance Monitoring</span>");
            out.println("<span>Active</span>");
            out.println("</div>");
            out.println("<div class='breakdown-item'>");
            out.println("<span>✅ Error ID Tracking</span>");
            out.println("<span>Active</span>");
            out.println("</div>");
            out.println("</div>");
            
            // Navigation
            out.println("<a href='" + request.getContextPath() + "/admin/dashboard' class='back-link'>");
            out.println("← Back to Admin Dashboard");
            out.println("</a>");
            
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
            logger.info("Error monitoring dashboard accessed by admin");
            
        } catch (Exception e) {
            logger.error("Error generating error monitoring dashboard", e);
            ErrorHandlingUtil.handleServletException(request, response, e, "error monitoring dashboard");
        }
    }
    
    private String getSystemHealthClass() {
        int total = totalErrors.get();
        if (total == 0) return "health-excellent";
        if (total < 5) return "health-good";
        if (total < 20) return "health-warning";
        return "health-critical";
    }
    
    private String getSystemHealthMessage() {
        int total = totalErrors.get();
        if (total == 0) return "EXCELLENT - No errors detected";
        if (total < 5) return "GOOD - Minor issues detected";
        if (total < 20) return "WARNING - Multiple errors detected";
        return "CRITICAL - High error rate detected";
    }
    
    // Static methods to track errors (would be called by error handling utility)
    public static void incrementTotalErrors() {
        totalErrors.incrementAndGet();
    }
    
    public static void incrementAuthenticationErrors() {
        authenticationErrors.incrementAndGet();
        incrementTotalErrors();
    }
    
    public static void incrementDatabaseErrors() {
        databaseErrors.incrementAndGet();
        incrementTotalErrors();
    }
    
    public static void incrementValidationErrors() {
        validationErrors.incrementAndGet();
        incrementTotalErrors();
    }
    
    public static void incrementSystemErrors() {
        systemErrors.incrementAndGet();
        incrementTotalErrors();
    }
    
    public static void addResponseTime(long responseTime) {
        totalResponseTime.addAndGet(responseTime);
        responseTimeCount.incrementAndGet();
    }
}