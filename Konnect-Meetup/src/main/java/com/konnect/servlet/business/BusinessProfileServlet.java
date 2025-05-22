package com.konnect.servlet.business;

import com.konnect.dao.BusinessDAO;
import com.konnect.model.Business;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * BusinessProfileServlet
 * Handles business profile creation and editing
 */
@WebServlet("/business/profile")
public class BusinessProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BusinessDAO businessDAO;

    @Override
    public void init() {
        businessDAO = new BusinessDAO();
    }

    /**
     * Handle GET requests - display profile form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in and is a business
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"business".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get business profile if it exists
        Business business = businessDAO.getByUserId(user.getId());

        // If business profile doesn't exist, create a new one
        if (business == null) {
            business = new Business();
            business.setId(user.getId());
            business.setUsername(user.getUsername());
            business.setEmail(user.getEmail());
            business.setRole("business");
            business.setStatus(user.getStatus());
        }

        // Set business as request attribute
        request.setAttribute("business", business);

        // Forward to profile page
        request.getRequestDispatcher("/business/profile.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process profile form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in and is a business
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"business".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get form data
        String companyName = request.getParameter("companyName");
        String industry = request.getParameter("industry");
        String description = request.getParameter("description");
        String website = request.getParameter("website");
        String contactPhone = request.getParameter("contactPhone");

        // Validate input
        if (companyName == null || companyName.trim().isEmpty()) {
            request.setAttribute("error", "Company name is required");
            doGet(request, response);
            return;
        }

        if (industry == null || industry.trim().isEmpty()) {
            request.setAttribute("error", "Industry is required");
            doGet(request, response);
            return;
        }

        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("error", "Description is required");
            doGet(request, response);
            return;
        }

        // Get business profile or create a new one
        Business business = businessDAO.getByUserId(user.getId());
        boolean isNewProfile = false;

        if (business == null) {
            business = new Business();
            business.setId(user.getId());
            business.setUsername(user.getUsername());
            business.setEmail(user.getEmail());
            business.setPassword(user.getPassword());
            business.setRole("business");
            business.setStatus(user.getStatus());
            isNewProfile = true;
        }

        // Set business properties
        business.setCompanyName(companyName);
        business.setIndustry(industry);
        business.setDescription(description);
        business.setWebsite(website);
        business.setContactPhone(contactPhone);

        // Save business profile
        boolean success;
        if (isNewProfile) {
            success = businessDAO.insert(business);
        } else {
            success = businessDAO.update(business);
        }

        if (success) {
            // Don't update the session user, as we're only updating the business profile
            // The user object in the session should remain the same

            // Set success message
            request.setAttribute("success", "Profile updated successfully");

            // Redirect to dashboard
            response.sendRedirect(request.getContextPath() + "/business/dashboard");
        } else {
            // Set error message
            request.setAttribute("error", "Failed to update profile. Please try again.");

            // Forward back to profile page
            doGet(request, response);
        }
    }
}
