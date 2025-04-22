package com.konnect.controller.business;

import com.konnect.dao.CampaignDao;
import com.konnect.model.Campaign;
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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@WebServlet("/business/campaigns")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 15    // 15 MB
)
public class BusinessCampaignServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CampaignDao campaignDao = new CampaignDao();
    private static final String UPLOAD_DIR_PRODUCT_IMAGES = "uploads" + File.separator + "product_images";


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
         if (currentUser == null || !"business".equals(currentUser.getRole())) {
             response.sendRedirect(request.getContextPath() + "/login.jsp");
             return;
         }

        String action = request.getParameter("action");
        String campaignIdStr = request.getParameter("id");

        if ("edit".equals(action) && campaignIdStr != null) {
            // Show edit form
            try {
                int campaignId = Integer.parseInt(campaignIdStr);
                Campaign campaign = campaignDao.findById(campaignId);
                // Security Check: Ensure the campaign belongs to the current business user
                if (campaign != null && campaign.getBusinessUserId() == currentUser.getUserId()) {
                    request.setAttribute("campaign", campaign);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/business/campaign_form.jsp");
                    dispatcher.forward(request, response);
                } else {
                    request.getSession().setAttribute("error", "Campaign not found or you do not have permission to edit it.");
                    response.sendRedirect(request.getContextPath() + "/business/campaigns");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid campaign ID format.");
                response.sendRedirect(request.getContextPath() + "/business/campaigns");
            }
        } else if ("create".equals(action)) {
            // Show create form (empty form)
             request.setAttribute("campaign", new Campaign()); // Send empty object for the form
            RequestDispatcher dispatcher = request.getRequestDispatcher("/business/campaign_form.jsp");
            dispatcher.forward(request, response);
        }
        else {
            // Default action: List campaigns
            List<Campaign> campaignList = campaignDao.findByBusinessId(currentUser.getUserId());
            request.setAttribute("campaignList", campaignList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/business/campaigns.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
         if (currentUser == null || !"business".equals(currentUser.getRole())) {
             response.sendRedirect(request.getContextPath() + "/login.jsp?error=Session expired or invalid role");
             return;
         }

         String action = request.getParameter("action"); // create, update, delete

         if ("delete".equals(action)) {
             handleDelete(request, response, currentUser);
             return;
         }

         // Common fields for create/update
        String campaignIdStr = request.getParameter("campaignId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String requirements = request.getParameter("requirements");
        String budgetStr = request.getParameter("budget");
        String status = request.getParameter("status"); // active, completed, archived

         BigDecimal budget = null;
        if (budgetStr != null && !budgetStr.trim().isEmpty()) {
            try {
                budget = new BigDecimal(budgetStr.trim());
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid budget format.");
                // Redirect back to form - need to know if it was create or edit
                String redirectTarget = (campaignIdStr != null && !campaignIdStr.isEmpty())
                                        ? "/business/campaigns?action=edit&id=" + campaignIdStr
                                        : "/business/campaigns?action=create";
                response.sendRedirect(request.getContextPath() + redirectTarget);
                return;
            }
        }

         // Handle file upload (Product Image)
        Part filePart = request.getPart("productImage"); // Matches <input type="file" name="productImage">
        String relativeFilePath = null; // Path to store in DB
         String oldRelativePath = null; // To delete old file on update


        // --- Determine if it's an update and get existing campaign data ---
        Campaign campaign = new Campaign();
         boolean isUpdate = (campaignIdStr != null && !campaignIdStr.trim().isEmpty());
         if (isUpdate) {
             try {
                 campaign.setCampaignId(Integer.parseInt(campaignIdStr));
                 // Fetch existing campaign to get old image path and verify ownership
                 Campaign existingCampaign = campaignDao.findById(campaign.getCampaignId());
                 if (existingCampaign == null || existingCampaign.getBusinessUserId() != currentUser.getUserId()) {
                     request.getSession().setAttribute("error", "Campaign not found or permission denied.");
                     response.sendRedirect(request.getContextPath() + "/business/campaigns");
                     return;
                 }
                 oldRelativePath = existingCampaign.getProductImagePath();
                 campaign.setBusinessUserId(currentUser.getUserId()); // Set owner for update check
             } catch (NumberFormatException e) {
                 request.getSession().setAttribute("error", "Invalid campaign ID for update.");
                 response.sendRedirect(request.getContextPath() + "/business/campaigns");
                 return;
             }
         } else {
             // For create, set the owner ID
              campaign.setBusinessUserId(currentUser.getUserId());
         }
        // --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---


         if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uniqueID = UUID.randomUUID().toString();
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) fileExtension = fileName.substring(dotIndex);
            String uniqueFileName = "campaign_" + (isUpdate ? campaign.getCampaignId() : "new") + "_" + uniqueID + fileExtension;

            String applicationPath = request.getServletContext().getRealPath("");
            String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR_PRODUCT_IMAGES;

            File uploadDir = new File(uploadFilePath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fullPath = uploadFilePath + File.separator + uniqueFileName;
            relativeFilePath = UPLOAD_DIR_PRODUCT_IMAGES + File.separator + uniqueFileName;

            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Product image saved: " + fullPath);

                // Delete old image if update was successful and old path exists
                 if (isUpdate && oldRelativePath != null && !oldRelativePath.isEmpty()) {
                     String oldFullPath = applicationPath + File.separator + oldRelativePath;
                     File oldFile = new File(oldFullPath);
                     if (oldFile.exists() && !oldFile.delete()) {
                         System.err.println("Failed to delete old product image: " + oldFullPath);
                     } else if (oldFile.exists()) {
                         System.out.println("Deleted old product image: " + oldFullPath);
                     }
                 }

            } catch (IOException e) {
                System.err.println("Error saving product image: " + e.getMessage());
                request.getSession().setAttribute("error", "Error uploading product image: " + e.getMessage());
                relativeFilePath = oldRelativePath; // Keep old path if upload failed during update
            }
        } else {
             // No new file uploaded, keep existing path if updating
             relativeFilePath = oldRelativePath;
             System.out.println("No new product image, keeping old path: " + relativeFilePath);
        }


         // Populate campaign object
         campaign.setTitle(title);
         campaign.setDescription(description);
         campaign.setRequirements(requirements);
         campaign.setBudget(budget);
         campaign.setStatus(status);
         campaign.setProductImagePath(relativeFilePath); // Set new or existing path

         // --- Save to database ---
         boolean success = false;
         if (isUpdate) {
             success = campaignDao.updateCampaign(campaign);
             System.out.println("Attempting to update campaign ID: " + campaign.getCampaignId());
         } else {
             success = campaignDao.createCampaign(campaign);
             System.out.println("Attempting to create new campaign for user ID: " + currentUser.getUserId());
         }

         if (success) {
            request.getSession().setAttribute("message", "Campaign " + (isUpdate ? "updated" : "created") + " successfully!");
         } else {
            request.getSession().setAttribute("error", "Failed to " + (isUpdate ? "update" : "create") + " campaign.");
         }

         // Redirect to the campaign list
         response.sendRedirect(request.getContextPath() + "/business/campaigns");
    }


     private void handleDelete(HttpServletRequest request, HttpServletResponse response, User currentUser) throws IOException {
         String campaignIdStr = request.getParameter("campaignId");
         if (campaignIdStr != null) {
             try {
                 int campaignId = Integer.parseInt(campaignIdStr);

                 // Optional: Get campaign details BEFORE deleting to find the image path
                 Campaign campaignToDelete = campaignDao.findById(campaignId);

                 // Security Check: Ensure the campaign belongs to the current business user before deleting
                 if (campaignToDelete != null && campaignToDelete.getBusinessUserId() == currentUser.getUserId()) {
                     boolean success = campaignDao.deleteCampaign(campaignId, currentUser.getUserId());

                     if (success) {
                         request.getSession().setAttribute("message", "Campaign deleted successfully.");
                         System.out.println("Business user " + currentUser.getUsername() + " deleted campaign ID " + campaignId);

                         // Delete associated product image file if it exists
                         if (campaignToDelete.getProductImagePath() != null && !campaignToDelete.getProductImagePath().isEmpty()) {
                             String applicationPath = request.getServletContext().getRealPath("");
                             String fullPath = applicationPath + File.separator + campaignToDelete.getProductImagePath();
                             File imageFile = new File(fullPath);
                             if (imageFile.exists()) {
                                 if (imageFile.delete()) {
                                     System.out.println("Deleted product image file: " + fullPath);
                                 } else {
                                     System.err.println("Failed to delete product image file: " + fullPath);
                                 }
                             }
                         }
                     } else {
                         request.getSession().setAttribute("error", "Failed to delete campaign.");
                         System.err.println("Business user " + currentUser.getUsername() + " failed to delete campaign ID " + campaignId);
                     }
                 } else {
                     // Campaign not found or doesn't belong to the user
                     request.getSession().setAttribute("error", "Campaign not found or permission denied for deletion.");
                     System.err.println("Business user " + currentUser.getUsername() + " permission denied to delete campaign ID " + campaignId);
                 }
             } catch (NumberFormatException e) {
                 request.getSession().setAttribute("error", "Invalid campaign ID format for deletion.");
                  System.err.println("BusinessCampaignServlet DELETE error: Invalid campaignId format");
             }
         } else {
             request.getSession().setAttribute("error", "Campaign ID is required for deletion.");
             System.err.println("BusinessCampaignServlet DELETE error: Missing campaignId");
         }
         response.sendRedirect(request.getContextPath() + "/business/campaigns");
     }

}
