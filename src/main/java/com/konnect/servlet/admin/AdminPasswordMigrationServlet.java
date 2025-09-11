package com.konnect.servlet.admin;

import com.konnect.util.PasswordMigrationService;
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

/**
 * AdminPasswordMigrationServlet
 * Provides password migration statistics for administrators
 */
@WebServlet(name = "AdminPasswordMigrationServlet", urlPatterns = {"/admin/password-migration"})
public class AdminPasswordMigrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AdminPasswordMigrationServlet.class);
    
    private PasswordMigrationService migrationService;
    
    @Override
    public void init() throws ServletException {
        migrationService = new PasswordMigrationService();
        logger.info("AdminPasswordMigrationServlet initialized");
    }
    
    /**
     * Handle GET requests - display migration statistics
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
            // Get migration statistics
            PasswordMigrationService.MigrationStats stats = migrationService.getMigrationStats();
            
            // Set response content type
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            // Generate HTML response
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Password Migration Status - Konnect Platform</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
            out.println(".container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            out.println(".header { text-align: center; margin-bottom: 30px; }");
            out.println(".stats-card { background: #f8f9fa; padding: 20px; border-radius: 8px; margin: 15px 0; border-left: 4px solid #007bff; }");
            out.println(".progress-bar { background: #e9ecef; border-radius: 10px; height: 30px; overflow: hidden; margin: 10px 0; }");
            out.println(".progress-fill { background: linear-gradient(90deg, #28a745, #20c997); height: 100%; transition: width 0.3s ease; }");
            out.println(".stat-number { font-size: 2em; font-weight: bold; color: #007bff; }");
            out.println(".stat-label { color: #6c757d; font-size: 0.9em; }");
            out.println(".back-link { display: inline-block; margin-top: 20px; color: #007bff; text-decoration: none; }");
            out.println(".back-link:hover { text-decoration: underline; }");
            out.println(".warning { background: #fff3cd; border-left-color: #ffc107; }");
            out.println(".success { background: #d4edda; border-left-color: #28a745; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<div class='container'>");
            out.println("<div class='header'>");
            out.println("<h1>🔐 Password Migration Status</h1>");
            out.println("<p>Monitor the progress of password security upgrades across the platform</p>");
            out.println("</div>");
            
            // Migration progress
            double progressPercent = stats.getMigrationProgress();
            String cardClass = progressPercent == 100 ? "stats-card success" : "stats-card warning";
            
            out.println("<div class='" + cardClass + "'>");
            out.println("<h3>Migration Progress</h3>");
            out.println("<div class='progress-bar'>");
            out.println("<div class='progress-fill' style='width: " + progressPercent + "%'></div>");
            out.println("</div>");
            out.println("<p><strong>" + String.format("%.1f", progressPercent) + "% Complete</strong></p>");
            out.println("</div>");
            
            // Total users
            out.println("<div class='stats-card'>");
            out.println("<div class='stat-number'>" + stats.getTotalUsers() + "</div>");
            out.println("<div class='stat-label'>Total Users</div>");
            out.println("</div>");
            
            // Migrated users
            out.println("<div class='stats-card'>");
            out.println("<div class='stat-number'>" + stats.getMigratedUsers() + "</div>");
            out.println("<div class='stat-label'>Users with Secure BCrypt Passwords</div>");
            out.println("</div>");
            
            // Legacy users
            out.println("<div class='stats-card " + (stats.getLegacyUsers() > 0 ? "warning" : "success") + "'>");
            out.println("<div class='stat-number'>" + stats.getLegacyUsers() + "</div>");
            out.println("<div class='stat-label'>Users with Legacy SHA-256 Passwords</div>");
            if (stats.getLegacyUsers() > 0) {
                out.println("<p><small>⚠️ These passwords will be automatically migrated when users log in</small></p>");
            } else {
                out.println("<p><small>✅ All passwords are now using secure BCrypt hashing</small></p>");
            }
            out.println("</div>");
            
            // Additional information
            out.println("<div class='stats-card'>");
            out.println("<h3>Security Information</h3>");
            out.println("<ul>");
            out.println("<li><strong>BCrypt:</strong> Industry-standard password hashing with adaptive cost</li>");
            out.println("<li><strong>Salt:</strong> Each password uses a unique random salt</li>");
            out.println("<li><strong>Auto-Migration:</strong> Legacy passwords are upgraded automatically during login</li>");
            out.println("<li><strong>Strength Requirements:</strong> " + com.konnect.util.PasswordUtil.getPasswordRequirements() + "</li>");
            out.println("</ul>");
            out.println("</div>");
            
            out.println("<a href='" + request.getContextPath() + "/admin/dashboard' class='back-link'>");
            out.println("← Back to Admin Dashboard");
            out.println("</a>");
            
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            
            logger.info("Password migration statistics requested by admin: {}", stats);
            
        } catch (Exception e) {
            logger.error("Error generating password migration statistics", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error retrieving migration statistics");
        }
    }
}