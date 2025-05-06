package com.konnect.controller;

import java.io.IOException;

import com.konnect.dao.ApplicationDAO;
import com.konnect.dao.CampaignDAO;
import com.konnect.dao.MessageDAO;
import com.konnect.dao.ProfileDAO;
import com.konnect.dao.ReportDAO;
import com.konnect.dao.UserDAO;
import com.konnect.model.BusinessProfile;
import com.konnect.model.CreatorProfile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet for handling dashboard operations
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    private CampaignDAO campaignDAO;
    private ApplicationDAO applicationDAO;
    private ProfileDAO profileDAO;
    private MessageDAO messageDAO;
    private ReportDAO reportDAO;

    public void init() {
        userDAO = new UserDAO();
        campaignDAO = new CampaignDAO();
        applicationDAO = new ApplicationDAO();
        profileDAO = new ProfileDAO();
        messageDAO = new MessageDAO();
        reportDAO = new ReportDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get user role and ID
        String role = (String) session.getAttribute("role");
        int userId = (int) session.getAttribute("userId");

        if (role.equals("admin")) {
            // For admin users, redirect to the admin servlet with relative path
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else if (role.equals("creator")) {
            // Creator dashboard
            CreatorProfile profile = profileDAO.getCreatorProfileByUserId(userId);
            request.setAttribute("profile", profile);
            request.setAttribute("activeCampaigns", campaignDAO.countActiveCampaigns());
            request.setAttribute("applications", applicationDAO.getApplicationsByCreatorId(userId));
            request.setAttribute("unreadMessages", messageDAO.countUnreadMessages(userId));

            request.getRequestDispatcher("/creator/dashboard.jsp").forward(request, response);
        } else if (role.equals("business")) {
            // Business dashboard
            BusinessProfile profile = profileDAO.getBusinessProfileByUserId(userId);
            request.setAttribute("profile", profile);
            request.setAttribute("campaigns", campaignDAO.getCampaignsByBusinessId(userId));
            request.setAttribute("applications", applicationDAO.getApplicationsByBusinessId(userId));
            request.setAttribute("unreadMessages", messageDAO.countUnreadMessages(userId));

            request.getRequestDispatcher("/business/dashboard.jsp").forward(request, response);
        } else {
            // Invalid role
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
