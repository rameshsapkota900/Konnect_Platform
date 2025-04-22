package com.konnect.controller.admin;

import com.konnect.dao.CampaignDao;
import com.konnect.dao.ReportDao;
import com.konnect.dao.UserDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDao userDao = new UserDao();
        CampaignDao campaignDao = new CampaignDao();
        ReportDao reportDao = new ReportDao(); // To show report count

        long totalUsers = userDao.countUsers();
        long totalCampaigns = campaignDao.countCampaigns();
        long newReports = reportDao.getAllReports().stream().filter(r -> "new".equals(r.getStatus())).count();


        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalCampaigns", totalCampaigns);
        request.setAttribute("newReports", newReports);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/dashboard.jsp");
        dispatcher.forward(request, response);
    }
}
