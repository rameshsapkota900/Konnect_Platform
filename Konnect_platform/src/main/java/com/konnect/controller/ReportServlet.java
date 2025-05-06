package com.konnect.controller;

import java.io.IOException;

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

/**
 * Servlet for handling report operations
 */
@WebServlet("/report/*")
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ReportDAO reportDAO;
    private UserDAO userDAO;

    public void init() {
        reportDAO = new ReportDAO();
        userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get user role and ID
        String role = (String) session.getAttribute("role");
        int userId = (int) session.getAttribute("userId");

        if (pathInfo == null || pathInfo.equals("/")) {
            // List reports (admin only)
            if (role.equals("admin")) {
                request.setAttribute("reports", reportDAO.getAllReports());
                request.getRequestDispatcher("/admin/reports.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else if (pathInfo.startsWith("/view/") && role.equals("admin")) {
            // View report details (admin only)
            try {
                int reportId = Integer.parseInt(pathInfo.substring(6));
                Report report = reportDAO.getReportById(reportId);

                if (report != null) {
                    request.setAttribute("report", report);
                    request.getRequestDispatcher("/admin/report-details.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/report");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/report");
            }
        } else if (pathInfo.startsWith("/user/")) {
            // Show report user form
            try {
                int reportedUserId = Integer.parseInt(pathInfo.substring(6));
                User reportedUser = userDAO.getUserById(reportedUserId);

                if (reportedUser != null) {
                    // Check if reported user is not the current user
                    if (reportedUserId == userId) {
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }

                    // Check if reported user is not an admin
                    if (reportedUser.getRole().equals("admin")) {
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }

                    request.setAttribute("reportedUser", reportedUser);
                    request.getRequestDispatcher("/" + role + "/report-user.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else if (pathInfo.equals("/pending") && role.equals("admin")) {
            // List pending reports (admin only)
            request.setAttribute("reports", reportDAO.getReportsByStatus("pending"));
            request.getRequestDispatcher("/admin/reports.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get user role and ID
        String role = (String) session.getAttribute("role");
        int userId = (int) session.getAttribute("userId");

        if (pathInfo.startsWith("/user/")) {
            // Submit user report
            try {
                int reportedUserId = Integer.parseInt(pathInfo.substring(6));
                User reportedUser = userDAO.getUserById(reportedUserId);

                if (reportedUser != null) {
                    // Check if reported user is not the current user
                    if (reportedUserId == userId) {
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }

                    // Check if reported user is not an admin
                    if (reportedUser.getRole().equals("admin")) {
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                        return;
                    }

                    // Get form data
                    String reason = request.getParameter("reason");
                    String description = request.getParameter("description");

                    // Validate input
                    if (reason == null || reason.trim().isEmpty()) {
                        request.setAttribute("error", "Reason is required");
                        request.setAttribute("reportedUser", reportedUser);
                        request.getRequestDispatcher("/" + role + "/report-user.jsp").forward(request, response);
                        return;
                    }

                    if (description == null || description.trim().isEmpty()) {
                        request.setAttribute("error", "Description is required");
                        request.setAttribute("reportedUser", reportedUser);
                        request.getRequestDispatcher("/" + role + "/report-user.jsp").forward(request, response);
                        return;
                    }

                    // Create report object
                    Report report = new Report(userId, reportedUserId, reason, description);

                    // Save report
                    int reportId = reportDAO.createReport(report);

                    if (reportId > 0) {
                        // Report submitted successfully
                        request.setAttribute("success", "Report submitted successfully");
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                    } else {
                        // Report submission failed
                        request.setAttribute("error", "Failed to submit report");
                        request.setAttribute("reportedUser", reportedUser);
                        request.getRequestDispatcher("/" + role + "/report-user.jsp").forward(request, response);
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else if (pathInfo.startsWith("/update/") && role.equals("admin")) {
            // Update report status (admin only)
            try {
                int reportId = Integer.parseInt(pathInfo.substring(8));
                String status = request.getParameter("status");
                String action = request.getParameter("action");

                // Validate status
                if (status == null || (!status.equals("resolved") && !status.equals("dismissed"))) {
                    response.sendRedirect(request.getContextPath() + "/report/view/" + reportId);
                    return;
                }

                // Update report status
                boolean updated = reportDAO.updateReportStatus(reportId, status);

                if (updated) {
                    // Status updated successfully
                    request.setAttribute("success", "Report marked as " + status);

                    // If action is to ban user
                    if (action != null && action.equals("ban")) {
                        Report report = reportDAO.getReportById(reportId);
                        if (report != null) {
                            // Ban the reported user
                            boolean banned = userDAO.updateUserStatus(report.getReportedId(), "banned");
                            if (banned) {
                                request.setAttribute("success", "Report resolved and user banned");
                            }
                        }
                    }
                } else {
                    // Status update failed
                    request.setAttribute("error", "Failed to update report status");
                }

                response.sendRedirect(request.getContextPath() + "/report/view/" + reportId);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/report");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}
