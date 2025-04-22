package com.konnect.controller.admin;

import com.konnect.dao.ReportDao;
import com.konnect.model.Report;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/reports")
public class AdminReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportDao reportDao = new ReportDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Report> reportList = reportDao.getAllReports();
        request.setAttribute("reportList", reportList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/reports.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String reportIdStr = request.getParameter("reportId");

        if ("resolve".equals(action) && reportIdStr != null) {
            try {
                int reportId = Integer.parseInt(reportIdStr);
                boolean success = reportDao.updateReportStatus(reportId, "resolved");
                if (success) {
                    request.getSession().setAttribute("message", "Report marked as resolved.");
                     System.out.println("Admin resolved report ID: " + reportId);
                } else {
                    request.getSession().setAttribute("error", "Failed to resolve report.");
                     System.err.println("Admin failed to resolve report ID: " + reportId);
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid report ID.");
                 System.err.println("Admin ReportServlet POST error: Invalid reportId format");
            }
        } else {
            request.getSession().setAttribute("error", "Invalid action or missing report ID.");
             System.err.println("Admin ReportServlet POST error: Missing parameters (action=resolve, reportId)");
        }

        response.sendRedirect(request.getContextPath() + "/admin/reports");
    }
}
