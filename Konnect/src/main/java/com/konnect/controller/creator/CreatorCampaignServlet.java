package com.konnect.controller.creator;

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
import java.util.List;

@WebServlet("/creator/campaigns")
public class CreatorCampaignServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CampaignDao campaignDao = new CampaignDao();
    private ApplicationDao applicationDao = new ApplicationDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

         if (currentUser == null || !"creator".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }


        // Fetch active campaigns, excluding those the creator has already applied to or been invited for
        List<Campaign> availableCampaigns = campaignDao.getActiveCampaigns(currentUser.getUserId());

        request.setAttribute("campaignList", availableCampaigns);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/creator/campaigns.jsp");
        dispatcher.forward(request, response);
    }

     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         // Handles the "Apply" action from the campaign list
         HttpSession session = request.getSession(false);
         User currentUser = (User) session.getAttribute("user");

         if (currentUser == null || !"creator".equals(currentUser.getRole())) {
             response.sendRedirect(request.getContextPath() + "/login.jsp?error=Session expired or invalid role");
             return;
         }

         String action = request.getParameter("action");
         String campaignIdStr = request.getParameter("campaignId");
         String message = request.getParameter("applicationMessage"); // Optional message from a modal/form

         if ("apply".equals(action) && campaignIdStr != null) {
             try {
                 int campaignId = Integer.parseInt(campaignIdStr);

                 // Check if campaign exists and is active
                 Campaign campaign = campaignDao.findById(campaignId);
                 if (campaign == null || !"active".equals(campaign.getStatus())) {
                     request.getSession().setAttribute("error", "Campaign not found or is no longer active.");
                     response.sendRedirect(request.getContextPath() + "/creator/campaigns");
                     return;
                 }

                 // Create application object
                 Application application = new Application();
                 application.setCampaignId(campaignId);
                 application.setCreatorUserId(currentUser.getUserId());
                 application.setStatus("pending"); // Explicitly set status
                 application.setMessage(message != null ? message.trim() : "I am interested in this campaign."); // Default or provided message

                 // Attempt to create the application
                 boolean success = applicationDao.createApplication(application);

                 if (success) {
                     request.getSession().setAttribute("message", "Successfully applied to campaign: " + campaign.getTitle());
                     System.out.println("Creator " + currentUser.getUsername() + " applied to campaign ID " + campaignId);
                 } else {
                     // DAO handles duplicate check, provide generic error here
                     request.getSession().setAttribute("error", "Failed to apply. You might have already applied or been invited to this campaign.");
                     System.err.println("Creator " + currentUser.getUsername() + " failed to apply to campaign ID " + campaignId);
                 }

             } catch (NumberFormatException e) {
                 request.getSession().setAttribute("error", "Invalid campaign ID format.");
                 System.err.println("CreatorCampaignServlet POST error: Invalid campaignId format");
             }
         } else {
             request.getSession().setAttribute("error", "Invalid action or missing campaign ID.");
             System.err.println("CreatorCampaignServlet POST error: Missing parameters (action=apply, campaignId)");
         }

         // Redirect back to the available campaigns list
         response.sendRedirect(request.getContextPath() + "/creator/campaigns");
     }
}
