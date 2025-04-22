package com.konnect.controller.creator;

import com.konnect.dao.ApplicationDao;
import com.konnect.model.Application;
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
import java.util.stream.Collectors;


@WebServlet("/creator/applications")
public class CreatorApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ApplicationDao applicationDao = new ApplicationDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !"creator".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Fetch all applications and invitations submitted BY or sent TO this creator
        List<Application> allMyInteractions = applicationDao.findByCreatorId(currentUser.getUserId());

        // Separate them for the view
        List<Application> myApplications = allMyInteractions.stream()
                .filter(a -> !"invited".equals(a.getStatus())) // Exclude initial invitations
                .collect(Collectors.toList());

        List<Application> myInvitations = allMyInteractions.stream()
                .filter(a -> "invited".equals(a.getStatus())) // Only show pending invitations here
                .collect(Collectors.toList());


        request.setAttribute("applicationList", myApplications);
        request.setAttribute("invitationList", myInvitations);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/creator/applications.jsp");
        dispatcher.forward(request, response);
    }

     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         // Handles Creator accepting/rejecting an INVITATION
         HttpSession session = request.getSession(false);
         User currentUser = (User) session.getAttribute("user");

         if (currentUser == null || !"creator".equals(currentUser.getRole())) {
             response.sendRedirect(request.getContextPath() + "/login.jsp?error=Session expired or invalid role");
             return;
         }

         String action = request.getParameter("action"); // "acceptInvite" or "rejectInvite"
         String applicationIdStr = request.getParameter("applicationId");

         if (applicationIdStr != null && ( "acceptInvite".equals(action) || "rejectInvite".equals(action) ) ) {
             try {
                 int applicationId = Integer.parseInt(applicationIdStr);
                 String newStatus = "acceptInvite".equals(action) ? "accepted" : "rejected";

                  // Verify the application exists and belongs to this creator and is currently 'invited'
                 Application invitation = applicationDao.findById(applicationId);
                 if (invitation == null || invitation.getCreatorUserId() != currentUser.getUserId() || !"invited".equals(invitation.getStatus()) ) {
                      request.getSession().setAttribute("error", "Invitation not found or invalid action.");
                      response.sendRedirect(request.getContextPath() + "/creator/applications");
                      return;
                 }


                 boolean success = applicationDao.respondToInvitation(applicationId, newStatus, currentUser.getUserId());

                 if (success) {
                     request.getSession().setAttribute("message", "Invitation " + newStatus + " successfully.");
                      System.out.println("Creator " + currentUser.getUsername() + " " + newStatus + " invitation ID " + applicationId);
                 } else {
                     request.getSession().setAttribute("error", "Failed to update invitation status.");
                     System.err.println("Creator " + currentUser.getUsername() + " failed to update status for invitation ID " + applicationId);
                 }

             } catch (NumberFormatException e) {
                 request.getSession().setAttribute("error", "Invalid application ID format.");
                  System.err.println("CreatorApplicationServlet POST error: Invalid applicationId format");
             }
         } else {
             request.getSession().setAttribute("error", "Invalid action or missing application ID.");
             System.err.println("CreatorApplicationServlet POST error: Missing parameters (action=accept/reject, applicationId)");
         }

         // Redirect back to the applications/invitations list
         response.sendRedirect(request.getContextPath() + "/creator/applications");
     }
}
