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
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/business/dashboard")
public class BusinessDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !"business".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        CampaignDao campaignDao = new CampaignDao();
        ApplicationDao applicationDao = new ApplicationDao();

        List<Campaign> myCampaigns = campaignDao.findByBusinessId(currentUser.getUserId());

        // --- FIX: Ensure myCampaignsCount is stored as long/Long ---
        long myCampaignsCount = myCampaigns.size(); // Get size as int, store in long (implicit cast)
        // Alternatively, explicit cast: long myCampaignsCount = (long) myCampaigns.size();

        long activeCampaigns = myCampaigns.stream().filter(c -> "active".equals(c.getStatus())).count();
        long totalApplications = 0;

        // Get applications for each campaign owned by the business
        for (Campaign campaign : myCampaigns) {
             // We need applications specifically for this campaign, respecting business ownership
            List<Application> appsForCampaign = applicationDao.findByCampaignId(campaign.getCampaignId(), currentUser.getUserId());
             totalApplications += appsForCampaign.stream()
                                     .filter(a -> "pending".equals(a.getStatus())) // Count only PENDING applications
                                     .count();
        }

        // --- Set attributes using the long variables (autoboxing creates Long objects) ---
        request.setAttribute("myCampaignsCount", myCampaignsCount); // Now setting a Long
        request.setAttribute("activeCampaignsCount", activeCampaigns); // Already setting a Long
        request.setAttribute("pendingApplicationsCount", totalApplications); // Already setting a Long


        RequestDispatcher dispatcher = request.getRequestDispatcher("/business/dashboard.jsp");
        dispatcher.forward(request, response);
    }
}
