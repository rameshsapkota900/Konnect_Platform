package com.konnect.controller.common;

import com.konnect.dao.ReportDao;
import com.konnect.dao.UserDao; // To validate reported user exists
import com.konnect.model.Report;
import com.konnect.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/common/reportUser") // Accessible by logged-in creators and businesses
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportDao reportDao = new ReportDao();
    private UserDao userDao = new UserDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Session expired");
            return;
        }
         // Admins cannot report users through this form
         if ("admin".equals(currentUser.getRole())) {
             request.getSession().setAttribute("error", "Admins cannot report users.");
             response.sendRedirect(request.getContextPath() + "/admin/dashboard"); // Redirect admin away
             return;
         }


        String reportedUserIdStr = request.getParameter("reportedUserId");
        String reason = request.getParameter("reason");
        String details = request.getParameter("details");

        // Determine where to redirect back to
        String redirectUrl = request.getHeader("Referer");
        if (redirectUrl == null || redirectUrl.isEmpty()) {
             // Fallback based on user role
            redirectUrl = request.getContextPath() + ("creator".equals(currentUser.getRole()) ? "/creator/dashboard" : "/business/dashboard");
        }

        if (reportedUserIdStr != null && reason != null && !reason.trim().isEmpty()) {
            try {
                int reportedUserId = Integer.parseInt(reportedUserIdStr);

                // Validate reported user exists and is not the current user
                 if (reportedUserId == currentUser.getUserId()) {
                     request.getSession().setAttribute("error", "You cannot report yourself.");
                     response.sendRedirect(redirectUrl);
                     return;
                 }

                 User reportedUser = userDao.findById(reportedUserId);
                 if (reportedUser == null) {
                     request.getSession().setAttribute("error", "The user you are trying to report does not exist.");
                     response.sendRedirect(redirectUrl);
                     return;
                 }
                 // Optional: Prevent creators reporting creators, businesses reporting businesses?
                 // if (currentUser.getRole().equals(reportedUser.getRole())) {
                 //     request.getSession().setAttribute("error", "You cannot report users of the same type.");
                 //     response.sendRedirect(redirectUrl);
                 //     return;
                 // }


                Report report = new Report();
                report.setReporterUserId(currentUser.getUserId());
                report.setReportedUserId(reportedUserId);
                report.setReason(reason.trim());
                report.setDetails(details != null ? details.trim() : null);
                // Status defaults to 'new' in DAO

                boolean success = reportDao.createReport(report);

                if (success) {
                    request.getSession().setAttribute("message", "Report submitted successfully. An admin will review it.");
                    System.out.println("User " + currentUser.getUsername() + " reported user ID " + reportedUserId + " for reason: " + reason);
                } else {
                    request.getSession().setAttribute("error", "Failed to submit report. Please try again later.");
                     System.err.println("User " + currentUser.getUsername() + " failed to report user ID " + reportedUserId);
                }

            } catch (NumberFormatException e) {
                 request.getSession().setAttribute("error", "Invalid user ID for report.");
                 System.err.println("ReportServlet POST error: Invalid reportedUserId format");
            }
        } else {
             request.getSession().setAttribute("error", "Reported user ID and reason are required.");
             System.err.println("ReportServlet POST error: Missing parameters (reportedUserId, reason)");
        }

        response.sendRedirect(redirectUrl); // Redirect back
    }

     // GET request should perhaps show a form, but typically report is a POST action from another page.
     // For simplicity, redirect away if accessed via GET.
     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         HttpSession session = request.getSession(false);
         User currentUser = (User) session.getAttribute("user");
         String redirectUrl = request.getContextPath() + "/login.jsp"; // Default
         if (currentUser != null) {
             redirectUrl = request.getContextPath() + "/" + currentUser.getRole() + "/dashboard";
         }
         response.sendRedirect(redirectUrl);
     }
}
