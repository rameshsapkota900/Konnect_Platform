package com.konnect.servlet.admin;

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

/**
 * AdminSecurityMonitoringServlet
 * Provides security monitoring dashboard for administrators
 */
@WebServlet(name = "AdminSecurityMonitoringServlet", urlPatterns = {"/admin/security-monitor"})
public class AdminSecurityMonitoringServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AdminSecurityMonitoringServlet.class);
    
    // Security event counters (in production, these would be stored in database)
    private static final AtomicInteger totalRequests = new AtomicInteger(0);
    private static final AtomicInteger blockedRequests = new AtomicInteger(0);
    private static final AtomicInteger xssAttempts = new AtomicInteger(0);
    private static final AtomicInteger sqlInjectionAttempts = new AtomicInteger(0);
    
    @Override
    public void init() throws ServletException {
        logger.info("AdminSecurityMonitoringServlet initialized");
    }
    
    /**
     * Handle GET requests - display security monitoring dashboard
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
            
            // Generate HTML response
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Security Monitoring Dashboard - Konnect Platform</title>");
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
            out.println(".security-features { margin: 30px 0; }");
            out.println(".feature-list { list-style: none; padding: 0; }");
            out.println(".feature-list li { padding: 10px; margin: 5px 0; background: #e9ecef; border-radius: 5px; }");
            out.println(".feature-list li::before { content: '✓'; color: #28a745; font-weight: bold; margin-right: 10px; }");
            out.println(".back-link { display: inline-block; margin-top: 20px; color: #007bff; text-decoration: none; }");
            out.println(".back-link:hover { text-decoration: underline; }");
            out.println(".refresh-btn { background: #007bff; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; margin: 10px 0; }");
            out.println(".refresh-btn:hover { background: #0056b3; }");
            out.println("</style>");
            out.println("<script>");
            out.println("function refreshStats() { window.location.reload(); }");
            out.println("</script>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<div class='container'>");
            out.println("<div class='header'>");
            out.println("<h1>🛡️ Security Monitoring Dashboard</h1>");
            out.println("<p>Real-time monitoring of security threats and protection systems</p>");
            out.println("<button class='refresh-btn' onclick='refreshStats()'>🔄 Refresh Statistics</button>");
            out.println("</div>");
            
            // Security statistics
            out.println("<div class='stats-grid'>");
            
            // Total requests
            out.println("<div class='stat-card'>");
            out.println("<div class='stat-number'>" + totalRequests.get() + "</div>");
            out.println("<div class='stat-label'>Total Requests Processed</div>");
            out.println("</div>");
            
            // Blocked requests
            String blockedClass = blockedRequests.get() > 0 ? "danger" : "success";
            out.println("<div class='stat-card " + blockedClass + "'>");
            out.println("<div class='stat-number'>" + blockedRequests.get() + "</div>");
            out.println("<div class='stat-label'>Malicious Requests Blocked</div>");
            out.println("</div>");
            
            // XSS attempts
            String xssClass = xssAttempts.get() > 0 ? "warning" : "success";
            out.println("<div class='stat-card " + xssClass + "'>");
            out.println("<div class='stat-number'>" + xssAttempts.get() + "</div>");
            out.println("<div class='stat-label'>XSS Attempts Detected</div>");
            out.println("</div>");
            
            // SQL injection attempts
            String sqlClass = sqlInjectionAttempts.get() > 0 ? "danger" : "success";
            out.println("<div class='stat-card " + sqlClass + "'>");
            out.println("<div class='stat-number'>" + sqlInjectionAttempts.get() + "</div>");
            out.println("<div class='stat-label'>SQL Injection Attempts Blocked</div>");
            out.println("</div>");
            
            out.println("</div>");
            
            // Security protection ratio
            double protectionRatio = totalRequests.get() > 0 ? 
                ((double)(totalRequests.get() - blockedRequests.get()) / totalRequests.get()) * 100 : 100;
            
            out.println("<div class='stat-card success' style='margin: 20px 0;'>");
            out.println("<div class='stat-number'>" + String.format("%.1f%%", protectionRatio) + "</div>");
            out.println("<div class='stat-label'>Clean Traffic Percentage</div>");
            out.println("</div>");
            
            // Active security features
            out.println("<div class='security-features'>");
            out.println("<h3>🔒 Active Security Features</h3>");
            out.println("<ul class='feature-list'>");
            out.println("<li><strong>Input Validation Filter:</strong> Real-time validation of all user inputs</li>");
            out.println("<li><strong>XSS Protection:</strong> Automatic sanitization of cross-site scripting attempts</li>");
            out.println("<li><strong>SQL Injection Prevention:</strong> Pattern-based detection and blocking</li>");
            out.println("<li><strong>Password Security:</strong> BCrypt hashing with automatic migration</li>");
            out.println("<li><strong>Session Security:</strong> Secure session management and validation</li>");
            out.println("<li><strong>Input Sanitization:</strong> Comprehensive data cleaning and validation</li>");
            out.println("<li><strong>Logging & Monitoring:</strong> Detailed security event logging</li>");
            out.println("<li><strong>Error Handling:</strong> Secure error responses without information leakage</li>");
            out.println("</ul>");
            out.println("</div>");
            
            // Security recommendations
            out.println("<div class='security-features'>");
            out.println("<h3>📋 Security Recommendations</h3>");
            out.println("<ul class='feature-list'>");
            out.println("<li>Monitor this dashboard regularly for unusual activity patterns</li>");
            out.println("<li>Review application logs for detailed security event information</li>");
            out.println("<li>Ensure all users migrate to BCrypt passwords through normal login</li>");
            out.println("<li>Consider implementing rate limiting for sensitive endpoints</li>");
            out.println("<li>Regular security audits and penetration testing recommended</li>");
            out.println("</ul>");
            out.println("</div>");
            
            out.println("<a href='" + request.getContextPath() + "/admin/dashboard' class='back-link'>");
            out.println("← Back to Admin Dashboard");
            out.println("</a>");
            
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
            logger.info("Security monitoring dashboard accessed by admin");
            
        } catch (Exception e) {
            logger.error("Error generating security monitoring dashboard", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error retrieving security monitoring data");
        }
    }
    
    // Static methods to increment counters (would be called by security filter)
    public static void incrementTotalRequests() {
        totalRequests.incrementAndGet();
    }
    
    public static void incrementBlockedRequests() {
        blockedRequests.incrementAndGet();
    }
    
    public static void incrementXSSAttempts() {
        xssAttempts.incrementAndGet();
    }
    
    public static void incrementSQLInjectionAttempts() {
        sqlInjectionAttempts.incrementAndGet();
    }
}