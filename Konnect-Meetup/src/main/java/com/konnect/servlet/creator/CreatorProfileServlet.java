package com.konnect.servlet.creator;

import com.konnect.dao.CreatorDAO;
import com.konnect.model.Creator;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CreatorProfileServlet
 * Handles creator profile creation and editing
 */
@WebServlet("/creator/profile")
public class CreatorProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CreatorDAO creatorDAO;

    @Override
    public void init() {
        creatorDAO = new CreatorDAO();
    }

    /**
     * Handle GET requests - display profile form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in and is a creator
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"creator".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get creator profile if it exists
        Creator creator = creatorDAO.getByUserId(user.getId());

        // If creator profile doesn't exist, create a new one
        if (creator == null) {
            creator = new Creator();
            // Set the ID to the user ID since Creator extends User
            creator.setId(user.getId());
            creator.setUsername(user.getUsername());
            creator.setEmail(user.getEmail());
            creator.setRole("creator");
            creator.setStatus(user.getStatus());
        }

        // Set creator as request attribute
        request.setAttribute("creator", creator);

        // Forward to profile page
        request.getRequestDispatcher("/creator/profile.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process profile form submission
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in and is a creator
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"creator".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get form data
        String bio = request.getParameter("bio");
        String followerCountStr = request.getParameter("followerCount");
        String instagramLink = request.getParameter("instagramLink");
        String tiktokLink = request.getParameter("tiktokLink");
        String youtubeLink = request.getParameter("youtubeLink");
        String pricingPerPostStr = request.getParameter("pricingPerPost");
        String[] interestsArray = request.getParameterValues("interests");

        // Validate input
        if (bio == null || bio.trim().isEmpty()) {
            request.setAttribute("error", "Bio is required");
            doGet(request, response);
            return;
        }

        if (followerCountStr == null || followerCountStr.trim().isEmpty()) {
            request.setAttribute("error", "Follower count is required");
            doGet(request, response);
            return;
        }

        if (pricingPerPostStr == null || pricingPerPostStr.trim().isEmpty()) {
            request.setAttribute("error", "Pricing per post is required");
            doGet(request, response);
            return;
        }

        // Parse numeric values
        int followerCount;
        double pricingPerPost;

        try {
            followerCount = Integer.parseInt(followerCountStr);
            if (followerCount < 0) {
                request.setAttribute("error", "Follower count must be a positive number");
                doGet(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Follower count must be a valid number");
            doGet(request, response);
            return;
        }

        try {
            pricingPerPost = Double.parseDouble(pricingPerPostStr);
            if (pricingPerPost < 0) {
                request.setAttribute("error", "Pricing per post must be a positive number");
                doGet(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Pricing per post must be a valid number");
            doGet(request, response);
            return;
        }

        // Get creator profile or create a new one
        Creator creator = creatorDAO.getByUserId(user.getId());
        boolean isNewProfile = false;

        if (creator == null) {
            creator = new Creator();
            // Set the ID to the user ID since Creator extends User
            creator.setId(user.getId());
            creator.setUsername(user.getUsername());
            creator.setEmail(user.getEmail());
            creator.setPassword(user.getPassword());
            creator.setRole("creator");
            creator.setStatus(user.getStatus());
            isNewProfile = true;
        }

        // Set creator properties
        creator.setBio(bio);
        creator.setFollowerCount(followerCount);
        creator.setInstagramLink(instagramLink);
        creator.setTiktokLink(tiktokLink);
        creator.setYoutubeLink(youtubeLink);
        creator.setPricingPerPost(pricingPerPost);

        // Set interests
        List<String> interests = new ArrayList<>();
        if (interestsArray != null) {
            interests = Arrays.asList(interestsArray);
        }
        creator.setInterests(interests);

        // Save creator profile
        boolean success;
        if (isNewProfile) {
            // For a new profile, we're using the existing user account
            // and just creating a creator profile for it
            success = creatorDAO.insert(creator);
        } else {
            success = creatorDAO.update(creator);
        }

        if (success) {
            // Don't update the session user, as we're only updating the creator profile
            // The user object in the session should remain the same

            // Set success message
            request.setAttribute("success", "Profile updated successfully");

            // Redirect to dashboard
            response.sendRedirect(request.getContextPath() + "/creator/dashboard");
        } else {
            // Set error message
            request.setAttribute("error", "Failed to update profile. Please try again.");

            // Forward back to profile page
            doGet(request, response);
        }
    }
}
