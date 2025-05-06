package com.konnect.controller;

import java.io.IOException;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.CampaignDAO;
import com.konnect.dao.ProfileDAO;
import com.konnect.dao.ReportDAO;
import com.konnect.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet for handling admin operations
 */
@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    private CampaignDAO campaignDAO;
    private ApplicationDAO applicationDAO;
    private ReportDAO reportDAO;
    private ProfileDAO profileDAO;

    public void init() {
        userDAO = new UserDAO();
        campaignDAO = new CampaignDAO();
        applicationDAO = new ApplicationDAO();
        reportDAO = new ReportDAO();
        profileDAO = new ProfileDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();

        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // No session or user, redirect to login
            response.sendRedirect(contextPath + "/login");
            return;
        } else if (session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")) {
            // User is logged in but not admin, redirect to appropriate dashboard
            String role = (String) session.getAttribute("role");
            if (role == null) {
                // Invalid session state, clear it and redirect to login
                session.invalidate();
                response.sendRedirect(contextPath + "/login");
            } else if ("business".equals(role)) {
                response.sendRedirect(contextPath + "/business/dashboard");
            } else if ("creator".equals(role)) {
                response.sendRedirect(contextPath + "/creator/dashboard");
            } else {
                response.sendRedirect(contextPath + "/dashboard");
            }
            return;
        }

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/dashboard")) {
            // Admin dashboard with statistics
            request.setAttribute("totalUsers", userDAO.countTotalUsers());
            request.setAttribute("activeUsers", userDAO.countUsersByStatus("active"));
            request.setAttribute("bannedUsers", userDAO.countUsersByStatus("banned"));
            request.setAttribute("totalCreators", profileDAO.countCreatorProfiles());
            request.setAttribute("totalBusinesses", profileDAO.countBusinessProfiles());
            request.setAttribute("totalCampaigns", campaignDAO.countTotalCampaigns());
            request.setAttribute("activeCampaigns", campaignDAO.countActiveCampaigns());
            request.setAttribute("totalApplications", applicationDAO.countTotalApplications());
            request.setAttribute("pendingApplications", applicationDAO.countApplicationsByStatus("pending"));
            request.setAttribute("totalReports", reportDAO.countTotalReports());
            request.setAttribute("pendingReports", reportDAO.countReportsByStatus("pending"));

            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } else if (pathInfo.equals("/users")) {
            // List all users
            request.setAttribute("users", userDAO.getAllUsers());
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/user/")) {
            // View user details
            try {
                int userId = Integer.parseInt(pathInfo.substring(6));
                request.setAttribute("user", userDAO.getUserById(userId));
                request.getRequestDispatcher("/admin/user-details.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect(contextPath + "/admin/users");
            }
        } else {
            response.sendRedirect(contextPath + "/admin/dashboard");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null ||
            session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")) {
            // Use relative path with context path
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (pathInfo.startsWith("/user/status/")) {
            // Update user status (ban/unban)
            try {
                int userId = Integer.parseInt(pathInfo.substring(13));
                String status = request.getParameter("status");

                // Validate status
                if (status == null || (!status.equals("active") && !status.equals("banned"))) {
                    response.sendRedirect(request.getContextPath() + "/admin/user/" + userId);
                    return;
                }

                // Update user status
                boolean updated = userDAO.updateUserStatus(userId, status);

                if (updated) {
                    // Status updated successfully
                    request.setAttribute("success", "User status updated to " + status);
                } else {
                    // Status update failed
                    request.setAttribute("error", "Failed to update user status");
                }

                response.sendRedirect(request.getContextPath() + "/admin/user/" + userId);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            }
        } else if (pathInfo.equals("/users/filter")) {
            // Filter users by role or status
            String role = request.getParameter("role");
            String status = request.getParameter("status");

            if ((role == null || role.trim().isEmpty()) && (status == null || status.trim().isEmpty())) {
                // No filter applied
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            if (role != null && !role.trim().isEmpty() && status != null && !status.trim().isEmpty()) {
                // Filter by both role and status
                request.setAttribute("users", userDAO.getUsersByRoleAndStatus(role, status));
            } else if (role != null && !role.trim().isEmpty()) {
                // Filter by role only
                request.setAttribute("users", userDAO.getUsersByRole(role));
            } else if (status != null && !status.trim().isEmpty()) {
                // Filter by status only
                request.setAttribute("users", userDAO.getUsersByStatus(status));
            }

            request.setAttribute("filterRole", role);
            request.setAttribute("filterStatus", status);
            request.getRequestDispatcher("/admin/users.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
    }
}
