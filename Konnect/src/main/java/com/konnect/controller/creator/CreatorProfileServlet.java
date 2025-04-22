package com.konnect.controller.creator;

import com.konnect.dao.ProfileDao;
import com.konnect.model.Profile;
import com.konnect.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

// Important: Add @MultipartConfig for file uploads
@WebServlet("/creator/profile")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
    // location = "/tmp" // Optional: Set temporary storage location
)
public class CreatorProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProfileDao profileDao = new ProfileDao();
    // --- Configure Upload Directory ---
    // Relative path within your web application structure
    private static final String UPLOAD_DIR_MEDIA_KITS = "uploads" + File.separator + "media_kits";


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Profile profile = profileDao.findByUserId(currentUser.getUserId());
         if (profile == null) {
             // Should not happen if profile is created on registration, but handle defensively
             profile = new Profile();
             profile.setUserId(currentUser.getUserId());
             // Maybe redirect to a "complete profile" page or show the form with empty values
         }

        request.setAttribute("profile", profile);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/creator/profile_form.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Retrieve form data
        String fullName = request.getParameter("fullName");
        String bio = request.getParameter("bio");
        String socialMediaLinks = request.getParameter("socialMediaLinks");
        int followerCount = 0;
        try {
            followerCount = Integer.parseInt(request.getParameter("followerCount"));
        } catch (NumberFormatException e) { /* Ignore or set default */ }
        String niche = request.getParameter("niche");
        String pricingInfo = request.getParameter("pricingInfo");

        // Handle file upload (Media Kit)
        Part filePart = request.getPart("mediaKit"); // Matches the <input type="file" name="mediaKit">
        String fileName = null;
        String relativeFilePath = null; // Path to store in DB

         // Get the existing profile to check for old file path
        Profile existingProfile = profileDao.findByUserId(currentUser.getUserId());
        String oldRelativePath = (existingProfile != null) ? existingProfile.getMediaKitPath() : null;


        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Sanitize filename
            // Create a unique filename to avoid collisions
             String uniqueID = UUID.randomUUID().toString();
             String fileExtension = "";
             int dotIndex = fileName.lastIndexOf('.');
             if (dotIndex > 0) {
                 fileExtension = fileName.substring(dotIndex); // .pdf, .jpg etc.
             }
             String uniqueFileName = "user_" + currentUser.getUserId() + "_" + uniqueID + fileExtension;


            // Get absolute path to the uploads directory
            String applicationPath = request.getServletContext().getRealPath("");
            String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR_MEDIA_KITS;

            // Create the directory if it doesn't exist
            File uploadDir = new File(uploadFilePath);
            if (!uploadDir.exists()) {
                if (uploadDir.mkdirs()) {
                    System.out.println("Created upload directory: " + uploadFilePath);
                } else {
                    System.err.println("Failed to create upload directory: " + uploadFilePath);
                    // Handle error appropriately - maybe set error message and redirect
                    request.getSession().setAttribute("error", "Server error: Could not create upload directory.");
                    response.sendRedirect(request.getContextPath() + "/creator/profile");
                    return;
                }
            }

            // Construct the full path for the new file
            String fullPath = uploadFilePath + File.separator + uniqueFileName;
            relativeFilePath = UPLOAD_DIR_MEDIA_KITS + File.separator + uniqueFileName; // Store this in DB


            System.out.println("Attempting to save media kit to: " + fullPath);
             System.out.println("Relative path for DB: " + relativeFilePath);


            // Save the file
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Media kit saved successfully: " + fullPath);

                 // Delete the old file IF a new one was successfully uploaded AND an old path exists
                if (oldRelativePath != null && !oldRelativePath.isEmpty()) {
                    String oldFullPath = applicationPath + File.separator + oldRelativePath;
                    File oldFile = new File(oldFullPath);
                    if (oldFile.exists()) {
                        if (oldFile.delete()) {
                             System.out.println("Deleted old media kit: " + oldFullPath);
                        } else {
                             System.err.println("Failed to delete old media kit: " + oldFullPath);
                        }
                    }
                }

            } catch (IOException e) {
                 System.err.println("Error saving uploaded media kit: " + e.getMessage());
                e.printStackTrace();
                request.getSession().setAttribute("error", "Error uploading media kit: " + e.getMessage());
                // Don't update the profile path if upload fails
                relativeFilePath = oldRelativePath; // Keep old path if upload failed
            }
        } else {
            // No new file uploaded, keep the existing path
             relativeFilePath = oldRelativePath;
             System.out.println("No new media kit uploaded, keeping old path: " + relativeFilePath);
        }

        // Create or update profile object
        Profile profile = (existingProfile != null) ? existingProfile : new Profile();
        profile.setUserId(currentUser.getUserId());
        profile.setFullName(fullName);
        profile.setBio(bio);
        profile.setSocialMediaLinks(socialMediaLinks);
        profile.setFollowerCount(followerCount);
        profile.setNiche(niche);
        profile.setPricingInfo(pricingInfo);
         profile.setMediaKitPath(relativeFilePath); // Set the new or existing relative path

        // Save to database
        boolean success = profileDao.saveOrUpdateProfile(profile);

        if (success) {
            request.getSession().setAttribute("message", "Profile updated successfully!");
             System.out.println("Creator profile updated for user ID: " + currentUser.getUserId());
        } else {
            request.getSession().setAttribute("error", "Failed to update profile.");
            System.err.println("Failed to update creator profile for user ID: " + currentUser.getUserId());
        }

        // Redirect back to the profile page (which will reload with updated data)
        response.sendRedirect(request.getContextPath() + "/creator/profile");
    }
}
