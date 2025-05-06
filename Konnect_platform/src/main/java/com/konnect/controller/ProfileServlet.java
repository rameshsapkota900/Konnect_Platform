package com.konnect.controller;

import java.io.IOException;

import com.konnect.dao.ProfileDAO;
import com.konnect.dao.UserDAO;
import com.konnect.model.BusinessProfile;
import com.konnect.model.CreatorProfile;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet for handling profile operations
 */
@WebServlet("/profile/*")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProfileDAO profileDAO;

    public void init() {
        profileDAO = new ProfileDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get user role and ID
        String role = (String) session.getAttribute("role");
        int userId = (int) session.getAttribute("userId");

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/edit")) {
            // Show profile edit form
            if (role.equals("creator")) {
                // Get creator profile
                CreatorProfile profile = profileDAO.getCreatorProfileByUserId(userId);

                if (profile == null) {
                    // Create empty profile if not exists
                    profile = new CreatorProfile();
                    profile.setUserId(userId);
                }

                request.setAttribute("profile", profile);
                request.getRequestDispatcher("/creator/profile.jsp").forward(request, response);
            } else if (role.equals("business")) {
                // Get business profile
                BusinessProfile profile = profileDAO.getBusinessProfileByUserId(userId);

                if (profile == null) {
                    // Create empty profile if not exists
                    profile = new BusinessProfile();
                    profile.setUserId(userId);
                }

                request.setAttribute("profile", profile);
                request.getRequestDispatcher("/business/profile.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else if (pathInfo.startsWith("/view/")) {
            // View other user's profile
            try {
                int profileUserId = Integer.parseInt(pathInfo.substring(6));

                // Get user details first to ensure the user exists
                com.konnect.model.User profileUser = new com.konnect.dao.UserDAO().getUserById(profileUserId);
                if (profileUser == null) {
                    // User not found, redirect to dashboard
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                }

                // Check user role to determine which profile to show
                String profileUserRole = profileUser.getRole();

                if ("creator".equals(profileUserRole)) {
                    // Viewing creator profile
                    CreatorProfile creatorProfile = profileDAO.getCreatorProfileByUserId(profileUserId);

                    // If profile doesn't exist yet, create a placeholder
                    if (creatorProfile == null) {
                        creatorProfile = new CreatorProfile();
                        creatorProfile.setUserId(profileUserId);
                    }

                    request.setAttribute("profile", creatorProfile);
                    request.setAttribute("profileUser", profileUser);
                    request.getRequestDispatcher("/creator-profile.jsp").forward(request, response);
                    return;
                } else if ("business".equals(profileUserRole)) {
                    // Viewing business profile
                    BusinessProfile businessProfile = profileDAO.getBusinessProfileByUserId(profileUserId);

                    // If profile doesn't exist yet, create a placeholder
                    if (businessProfile == null) {
                        businessProfile = new BusinessProfile();
                        businessProfile.setUserId(profileUserId);
                    }

                    request.setAttribute("profile", businessProfile);
                    request.setAttribute("profileUser", profileUser);
                    request.getRequestDispatcher("/business-profile.jsp").forward(request, response);
                    return;
                }

                // Profile not found
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else if (pathInfo.equals("/creators") && (role.equals("business") || role.equals("admin"))) {
            // List all creators
            request.setAttribute("creators", profileDAO.getAllCreatorProfiles());
            request.getRequestDispatcher("/" + role + "/creators.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get user role and ID
        String role = (String) session.getAttribute("role");
        int userId = (int) session.getAttribute("userId");

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/update")) {
            // Update profile
            if (role.equals("creator")) {
                // Update creator profile
                String fullName = request.getParameter("fullName");
                String bio = request.getParameter("bio");
                String instagramLink = request.getParameter("instagramLink");
                String youtubeLink = request.getParameter("youtubeLink");
                String tiktokLink = request.getParameter("tiktokLink");
                String instagramFollowersStr = request.getParameter("instagramFollowers");
                String youtubeFollowersStr = request.getParameter("youtubeFollowers");
                String tiktokFollowersStr = request.getParameter("tiktokFollowers");
                String postPriceStr = request.getParameter("postPrice");
                String storyPriceStr = request.getParameter("storyPrice");
                String videoPriceStr = request.getParameter("videoPrice");
                String interests = request.getParameter("interests");

                // Validate required fields
                if (fullName == null || fullName.trim().isEmpty() ||
                    bio == null || bio.trim().isEmpty()) {

                    request.setAttribute("error", "Name and bio are required");

                    // Get existing profile
                    CreatorProfile profile = profileDAO.getCreatorProfileByUserId(userId);
                    if (profile == null) {
                        profile = new CreatorProfile();
                        profile.setUserId(userId);
                    }

                    request.setAttribute("profile", profile);
                    request.getRequestDispatcher("/creator/profile.jsp").forward(request, response);
                    return;
                }

                // Parse numeric values
                int instagramFollowers = 0;
                int youtubeFollowers = 0;
                int tiktokFollowers = 0;
                double postPrice = 0.0;
                double storyPrice = 0.0;
                double videoPrice = 0.0;

                try {
                    if (instagramFollowersStr != null && !instagramFollowersStr.trim().isEmpty()) {
                        instagramFollowers = Integer.parseInt(instagramFollowersStr);
                    }

                    if (youtubeFollowersStr != null && !youtubeFollowersStr.trim().isEmpty()) {
                        youtubeFollowers = Integer.parseInt(youtubeFollowersStr);
                    }

                    if (tiktokFollowersStr != null && !tiktokFollowersStr.trim().isEmpty()) {
                        tiktokFollowers = Integer.parseInt(tiktokFollowersStr);
                    }

                    if (postPriceStr != null && !postPriceStr.trim().isEmpty()) {
                        postPrice = Double.parseDouble(postPriceStr);
                    }

                    if (storyPriceStr != null && !storyPriceStr.trim().isEmpty()) {
                        storyPrice = Double.parseDouble(storyPriceStr);
                    }

                    if (videoPriceStr != null && !videoPriceStr.trim().isEmpty()) {
                        videoPrice = Double.parseDouble(videoPriceStr);
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid numeric values");

                    // Get existing profile
                    CreatorProfile profile = profileDAO.getCreatorProfileByUserId(userId);
                    if (profile == null) {
                        profile = new CreatorProfile();
                        profile.setUserId(userId);
                    }

                    request.setAttribute("profile", profile);
                    request.getRequestDispatcher("/creator/profile.jsp").forward(request, response);
                    return;
                }

                // Create or update profile
                CreatorProfile profile = profileDAO.getCreatorProfileByUserId(userId);
                boolean isNewProfile = false;

                if (profile == null) {
                    profile = new CreatorProfile();
                    profile.setUserId(userId);
                    isNewProfile = true;
                }

                profile.setFullName(fullName);
                profile.setBio(bio);
                profile.setInstagramLink(instagramLink);
                profile.setYoutubeLink(youtubeLink);
                profile.setTiktokLink(tiktokLink);
                profile.setInstagramFollowers(instagramFollowers);
                profile.setYoutubeFollowers(youtubeFollowers);
                profile.setTiktokFollowers(tiktokFollowers);
                profile.setPostPrice(postPrice);
                profile.setStoryPrice(storyPrice);
                profile.setVideoPrice(videoPrice);
                profile.setInterests(interests);

                boolean success;
                if (isNewProfile) {
                    int profileId = profileDAO.createCreatorProfile(profile);
                    success = profileId > 0;
                } else {
                    success = profileDAO.updateCreatorProfile(profile);
                }

                if (success) {
                    request.setAttribute("success", "Profile updated successfully");
                } else {
                    request.setAttribute("error", "Failed to update profile");
                }

                // Refresh profile data
                profile = profileDAO.getCreatorProfileByUserId(userId);
                request.setAttribute("profile", profile);
                request.getRequestDispatcher("/creator/profile.jsp").forward(request, response);

            } else if (role.equals("business")) {
                // Update business profile
                String businessName = request.getParameter("businessName");
                String businessDescription = request.getParameter("businessDescription");
                String website = request.getParameter("website");
                String industry = request.getParameter("industry");

                // Validate required fields
                if (businessName == null || businessName.trim().isEmpty() ||
                    businessDescription == null || businessDescription.trim().isEmpty() ||
                    industry == null || industry.trim().isEmpty()) {

                    request.setAttribute("error", "Business name, description, and industry are required");

                    // Get existing profile
                    BusinessProfile profile = profileDAO.getBusinessProfileByUserId(userId);
                    if (profile == null) {
                        profile = new BusinessProfile();
                        profile.setUserId(userId);
                    }

                    request.setAttribute("profile", profile);
                    request.getRequestDispatcher("/business/profile.jsp").forward(request, response);
                    return;
                }

                // Create or update profile
                BusinessProfile profile = profileDAO.getBusinessProfileByUserId(userId);
                boolean isNewProfile = false;

                if (profile == null) {
                    profile = new BusinessProfile();
                    profile.setUserId(userId);
                    isNewProfile = true;
                }

                profile.setBusinessName(businessName);
                profile.setBusinessDescription(businessDescription);
                profile.setWebsite(website);
                profile.setIndustry(industry);

                boolean success;
                if (isNewProfile) {
                    int profileId = profileDAO.createBusinessProfile(profile);
                    success = profileId > 0;
                } else {
                    success = profileDAO.updateBusinessProfile(profile);
                }

                if (success) {
                    request.setAttribute("success", "Profile updated successfully");
                } else {
                    request.setAttribute("error", "Failed to update profile");
                }

                // Refresh profile data
                profile = profileDAO.getBusinessProfileByUserId(userId);
                request.setAttribute("profile", profile);
                request.getRequestDispatcher("/business/profile.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else if (pathInfo.equals("/search-creators") && (role.equals("business") || role.equals("admin"))) {
            // Search creators by interests
            String interests = request.getParameter("interests");

            if (interests != null && !interests.trim().isEmpty()) {
                request.setAttribute("creators", profileDAO.getCreatorProfilesByInterests(interests));
                request.setAttribute("searchTerm", interests);
            } else {
                request.setAttribute("creators", profileDAO.getAllCreatorProfiles());
            }

            request.getRequestDispatcher("/" + role + "/creators.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}
