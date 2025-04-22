package com.konnect.controller.business;

import com.konnect.dao.ApplicationDao;
import com.konnect.dao.CampaignDao;
import com.konnect.model.Application;
import com.konnect.model.Campaign;
import com.konnect.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/business/applications")
public class BusinessApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ApplicationDao applicationDao = new ApplicationDao();
    private CampaignDao campaignDao = new CampaignDao();


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"business".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get all campaigns owned by this business
        List<Campaign> myCampaigns = campaignDao.findByBusinessId(currentUser.getUserId());
        List<Integer> myCampaignIds = myCampaigns.stream().map(Campaign::getCampaignId).collect(Collectors.toList());

        // Fetch all applications for these campaigns
        List<Application> allApplications = new ArrayList<>();
         if (!myCampaignIds.isEmpty()) {
            for(int campaignId : myCampaignIds) {
                // Pass businessUserId for security check within DAO
                allApplications.addAll(applicationDao.findByCampaignId(campaignId, currentUser.getUserId()));
            }
        }


        // Group applications by campaign for the view
        Map<String, List<Application>> applicationsByCampaign = allApplications.stream()
                .filter(app -> app.getCampaignTitle() != null) // Ensure title is available
                .collect(Collectors.groupingBy(Application::getCampaignTitle));


        request.setAttribute("applicationsByCampaign", applicationsByCampaign);
        request.setAttribute("myCampaigns", myCampaigns); // Also pass campaigns for potential filtering dropdown

        RequestDispatcher dispatcher = request.getRequestDispatcher("/business/applications.jsp");
        dispatcher.forward(request, response);
    }

     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handles Business accepting/rejecting an APPLICATION
         HttpSession session = request.getSession(false);
         User currentUser = (User) session.getAttribute("user");
         if (currentUser == null || !"business".equals(currentUser.getRole())) {
             response.sendRedirect(request.getContextPath() + "/login.jsp?error=Session expired or invalid role");
             return;
         }

        String action = request.getParameter("action"); // "acceptApp" or "rejectApp"
        String applicationIdStr = request.getParameter("applicationId");

        if (applicationIdStr != null && ("acceptApp".equals(action) || "rejectApp".equals(action)) ) {
             try {
                int applicationId = Integer.parseInt(applicationIdStr);
                String newStatus = "acceptApp".equals(action) ? "accepted" : "rejected";

                 // DAO method updateApplicationStatus includes security check (business owner)
                 boolean success = applicationDao.updateApplicationStatus(applicationId, newStatus, currentUser.getUserId());

                 if (success) {
                     request.getSession().setAttribute("message", "Application status updated successfully.");
                     System.out.println("Business " + currentUser.getUsername() + " updated application ID " + applicationId + " to " + newStatus);
                 } else {
                      request.getSession().setAttribute("error", "Failed to update application status. Check permissions or application status.");
                      System.err.println("Business " + currentUser.getUsername() + " failed to update status for application ID " + applicationId);
                 }

             } catch (NumberFormatException e) {
                  request.getSession().setAttribute("error", "Invalid application ID format.");
                  System.err.println("BusinessApplicationServlet POST error: Invalid applicationId format");
             }
         } else {
              request.getSession().setAttribute("error", "Invalid action or missing application ID.");
              System.err.println("BusinessApplicationServlet POST error: Missing parameters (action=accept/reject, applicationId)");
         }

        // Redirect back to the applications list
        response.sendRedirect(request.getContextPath() + "/business/applications");
     }
}
