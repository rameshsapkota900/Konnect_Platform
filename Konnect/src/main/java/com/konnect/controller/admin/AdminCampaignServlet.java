package com.konnect.controller.admin;

import com.konnect.dao.CampaignDao;
import com.konnect.model.Campaign;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/campaigns")
public class AdminCampaignServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CampaignDao campaignDao = new CampaignDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Campaign> campaignList = campaignDao.getAllCampaigns();
        request.setAttribute("campaignList", campaignList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/campaigns.jsp");
        dispatcher.forward(request, response);
    }

    // Optional: Add POST handler if admin needs to modify campaigns (e.g., change status)
    // protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //    // Handle campaign status changes initiated by admin
    // }
}
