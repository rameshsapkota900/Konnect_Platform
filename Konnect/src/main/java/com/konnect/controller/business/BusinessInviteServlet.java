package com.konnect.controller.business;

import com.konnect.dao.ApplicationDao;
import com.konnect.dao.CampaignDao;
import com.konnect.dao.UserDao; // To validate creator exists
import com.konnect.model.Application;
import com.konnect.model.Campaign;
import com.konnect.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/business/invite")
public class BusinessInviteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ApplicationDao applicationDao = new ApplicationDao();
    private CampaignDao campaignDao = new CampaignDao();
    private UserDao userDao = new UserDao();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"business".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Session expired or invalid role");
            return;
        }

        String creatorIdStr = request.getParameter("creatorId");
        String campaignIdStr = request.getParameter("campaignId");
        String message = request.getParameter("inviteMessage"); // Optional custom message

        String redirectUrl = request.getHeader("Referer"); // Go back where they came from (search or profile)
        if (redirectUrl == null || redirectUrl.isEmpty()) {
            redirectUrl = request.getContextPath() + "/business/search"; // Default fallback
        }


        if (creatorIdStr != null && campaignIdStr != null) {
            try {
                int creatorId = Integer.parseInt(creatorIdStr);
                int campaignId = Integer.parseInt(campaignIdStr);

                // --- Validations ---
                // 1. Check if creator exists and is actually a creator
                User creator = userDao.findById(creatorId);
                if (creator == null || !"creator".equals(creator.getRole())) {
                    request.getSession().setAttribute("error", "Invalid creator selected.");
                    response.sendRedirect(redirectUrl);
                    return;
                }

                // 2. Check if campaign exists, is active, and belongs to the current business
                Campaign campaign = campaignDao.findById(campaignId);
                if (campaign == null || campaign.getBusinessUserId() != currentUser.getUserId() || !"active".equals(campaign.getStatus())) {
                     request.getSession().setAttribute("error", "Invalid or inactive campaign selected, or permission denied.");
                     response.sendRedirect(redirectUrl);
                    return;
                }
                // --- End Validations ---


                // Create an application object with 'invited' status
                Application invitation = new Application();
                invitation.setCampaignId(campaignId);
                invitation.setCreatorUserId(creatorId);
                invitation.setStatus("invited");
                invitation.setMessage(message != null ? message.trim() : "You are invited to apply for this campaign!"); // Default or custom


                boolean success = applicationDao.createInvitation(invitation);

                if (success) {
                    request.getSession().setAttribute("message", "Invitation sent successfully to " + creator.getUsername() + " for campaign '" + campaign.getTitle() + "'.");
                    System.out.println("Business " + currentUser.getUsername() + " invited creator ID " + creatorId + " to campaign ID " + campaignId);
                } else {
                    // DAO handles duplicate check
                     request.getSession().setAttribute("error", "Failed to send invitation. The creator might have already applied or been invited.");
                     System.err.println("Business " + currentUser.getUsername() + " failed to invite creator ID " + creatorId + " to campaign ID " + campaignId);
                }

            } catch (NumberFormatException e) {
                 request.getSession().setAttribute("error", "Invalid Creator or Campaign ID format.");
                 System.err.println("BusinessInviteServlet POST error: Invalid ID format");
            }
        } else {
             request.getSession().setAttribute("error", "Creator ID and Campaign ID are required to send an invitation.");
             System.err.println("BusinessInviteServlet POST error: Missing parameters (creatorId, campaignId)");
        }

        response.sendRedirect(redirectUrl); // Redirect back
    }

    // GET request not typically used for sending invites, redirect to search or dashboard
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/business/search");
    }
}
