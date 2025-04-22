package com.konnect.controller.creator;

import com.konnect.dao.ApplicationDao;
import com.konnect.dao.ProfileDao;
import com.konnect.model.Application;
import com.konnect.model.Profile;
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

@WebServlet("/creator/dashboard")
public class CreatorDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !"creator".equals(currentUser.getRole())) { // Added role check for consistency
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        ProfileDao profileDao = new ProfileDao();
        ApplicationDao applicationDao = new ApplicationDao();

        Profile profile = profileDao.findByUserId(currentUser.getUserId());
        List<Application> myApplications = applicationDao.findByCreatorId(currentUser.getUserId());
        List<Application> myInvitations = applicationDao.findInvitationsByCreatorId(currentUser.getUserId());

        // Separate applications by status for easier display
        long pendingApps = myApplications.stream().filter(a -> "pending".equals(a.getStatus())).count();
        long acceptedApps = myApplications.stream().filter(a -> "accepted".equals(a.getStatus())).count();

        // --- FIX: Ensure invitationsCount is stored as long/Long ---
        long invitesCount = myInvitations.size(); // Get size as int, store in long (implicit cast)
        // Alternatively, explicit cast: long invitesCount = (long) myInvitations.size();

        request.setAttribute("profile", profile);
        request.setAttribute("pendingApplicationsCount", pendingApps);    // Sets a Long
        request.setAttribute("acceptedApplicationsCount", acceptedApps);  // Sets a Long
        request.setAttribute("invitationsCount", invitesCount);         // Now sets a Long

        RequestDispatcher dispatcher = request.getRequestDispatcher("/creator/dashboard.jsp");
        dispatcher.forward(request, response);
    }
}
