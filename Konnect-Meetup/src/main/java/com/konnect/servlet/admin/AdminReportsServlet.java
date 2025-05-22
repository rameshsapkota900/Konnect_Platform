package com.konnect.servlet.admin;

import com.konnect.dao.ReportDAO;
import com.konnect.dao.UserDAO;
import com.konnect.model.Report;
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
 * AdminReportsServlet
 * Handles report management for admins
 */
@WebServlet("/admin/reports")
public class AdminReportsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportDAO reportDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() {
        reportDAO = new ReportDAO();
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - display reports list
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
        
        // Get filter parameter
        String statusFilter = request.getParameter("status");
        
        // Get reports based on filter
        List<Report> reports;
        if (statusFilter != null && !statusFilter.isEmpty()) {
            reports = reportDAO.getByStatus(statusFilter);
        } else {
            reports = reportDAO.getAll();
        }
        
        // Get user details for each report
        Map<Integer, User> reporterMap = new HashMap<>();
        Map<Integer, User> reportedUserMap = new HashMap<>();
        
        for (Report report : reports) {
            User reporter = userDAO.getById(report.getReporterId());
            User reportedUser = userDAO.getById(report.getReportedUserId());
            
            if (reporter != null) {
                reporterMap.put(report.getId(), reporter);
            }
            
            if (reportedUser != null) {
                reportedUserMap.put(report.getId(), reportedUser);
            }
        }
        
        // Set attributes for the view
        request.setAttribute("reports", reports);
        request.setAttribute("reporterMap", reporterMap);
        request.setAttribute("reportedUserMap", reportedUserMap);
        request.setAttribute("statusFilter", statusFilter);
        
        // Forward to reports page
        request.getRequestDispatcher("/admin/reports.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - update report status
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
        
        // Get form data
        String reportIdStr = request.getParameter("reportId");
        String action = request.getParameter("action");
        String banUserStr = request.getParameter("banUser");
        
        if (reportIdStr == null || reportIdStr.trim().isEmpty() || action == null || action.trim().isEmpty()) {
            request.setAttribute("error", "Invalid request");
            doGet(request, response);
            return;
        }
        
        // Parse report ID
        int reportId;
        try {
            reportId = Integer.parseInt(reportIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid report ID");
            doGet(request, response);
            return;
        }
        
        // Get report
        Report report = reportDAO.getById(reportId);
        if (report == null) {
            request.setAttribute("error", "Report not found");
            doGet(request, response);
            return;
        }
        
        // Update report status based on action
        boolean success = false;
        String successMessage = "";
        
        if ("resolve".equals(action)) {
            report.setStatus("resolved");
            success = reportDAO.updateStatus(reportId, "resolved");
            successMessage = "Report resolved successfully";
            
            // Check if admin wants to ban the reported user
            boolean banUser = "on".equals(banUserStr);
            if (banUser) {
                User reportedUser = userDAO.getById(report.getReportedUserId());
                if (reportedUser != null) {
                    reportedUser.setStatus("banned");
                    boolean banSuccess = userDAO.update(reportedUser);
                    if (banSuccess) {
                        successMessage += " and user has been banned";
                    }
                }
            }
        } else if ("dismiss".equals(action)) {
            report.setStatus("dismissed");
            success = reportDAO.updateStatus(reportId, "dismissed");
            successMessage = "Report dismissed successfully";
        } else {
            request.setAttribute("error", "Invalid action");
            doGet(request, response);
            return;
        }
        
        if (success) {
            request.setAttribute("success", successMessage);
        } else {
            request.setAttribute("error", "Failed to update report status");
        }
        
        // Redirect back to reports page
        doGet(request, response);
    }
}
