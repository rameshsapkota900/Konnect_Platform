package com.konnect.servlet.admin;

import com.konnect.dao.CampaignDAO;
import com.konnect.dao.ReportDAO;
import com.konnect.dao.UserDAO;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdminDashboardServlet
 * Handles admin dashboard requests
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private CampaignDAO campaignDAO;
    private ReportDAO reportDAO;
    
    @Override
    public void init() {
        userDAO = new UserDAO();
        campaignDAO = new CampaignDAO();
        reportDAO = new ReportDAO();
    }
    
    /**
     * Handle GET requests - display admin dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if user is logged in and is an admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get statistics for the dashboard
        Map<String, Integer> statistics = new HashMap<>();
        
        // Get user counts by role
        List<User> allUsers = userDAO.getAll();
        int totalUsers = allUsers.size();
        int creatorCount = 0;
        int businessCount = 0;
        int activeUsers = 0;
        int bannedUsers = 0;
        
        for (User u : allUsers) {
            if ("creator".equals(u.getRole())) {
                creatorCount++;
            } else if ("business".equals(u.getRole())) {
                businessCount++;
            }
            
            if ("active".equals(u.getStatus())) {
                activeUsers++;
            } else if ("banned".equals(u.getStatus())) {
                bannedUsers++;
            }
        }
        
        // Get campaign count
        List<com.konnect.model.Campaign> campaigns = campaignDAO.getAll();
        int campaignCount = campaigns.size();
        
        // Get report counts by status
        int pendingReports = reportDAO.countByStatus("pending");
        int resolvedReports = reportDAO.countByStatus("resolved");
        int dismissedReports = reportDAO.countByStatus("dismissed");
        int totalReports = pendingReports + resolvedReports + dismissedReports;
        
        // Add statistics to the map
        statistics.put("totalUsers", totalUsers);
        statistics.put("creatorCount", creatorCount);
        statistics.put("businessCount", businessCount);
        statistics.put("activeUsers", activeUsers);
        statistics.put("bannedUsers", bannedUsers);
        statistics.put("campaignCount", campaignCount);
        statistics.put("pendingReports", pendingReports);
        statistics.put("resolvedReports", resolvedReports);
        statistics.put("dismissedReports", dismissedReports);
        statistics.put("totalReports", totalReports);
        
        // Set attributes for the dashboard
        request.setAttribute("statistics", statistics);
        request.setAttribute("recentUsers", getRecentUsers(allUsers, 5));
        request.setAttribute("recentCampaigns", getRecentCampaigns(campaigns, 5));
        
        // Forward to dashboard page
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
    
    /**
     * Get recent users
     * @param users List of all users
     * @param limit Maximum number of users to return
     * @return List of recent users
     */
    private List<User> getRecentUsers(List<User> users, int limit) {
        // Sort users by creation date (newest first)
        users.sort((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()));
        
        // Return the first 'limit' users or all users if there are fewer than 'limit'
        return users.subList(0, Math.min(users.size(), limit));
    }
    
    /**
     * Get recent campaigns
     * @param campaigns List of all campaigns
     * @param limit Maximum number of campaigns to return
     * @return List of recent campaigns
     */
    private List<com.konnect.model.Campaign> getRecentCampaigns(List<com.konnect.model.Campaign> campaigns, int limit) {
        // Sort campaigns by creation date (newest first)
        campaigns.sort((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()));
        
        // Return the first 'limit' campaigns or all campaigns if there are fewer than 'limit'
        return campaigns.subList(0, Math.min(campaigns.size(), limit));
    }
}
