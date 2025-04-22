package com.konnect.controller.business;

import com.konnect.dao.MessageDao;
import com.konnect.dao.UserDao;
import com.konnect.model.Message;
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

@WebServlet("/business/chat")
public class BusinessChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MessageDao messageDao = new MessageDao();
    private UserDao userDao = new UserDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !"business".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get list of creator users this business has chatted with
        List<User> chatPartners = messageDao.getConversationPartners(currentUser.getUserId());

        // Check if a specific chat partner is requested
        String partnerIdStr = request.getParameter("with");
        List<Message> conversation = null;
        User currentPartner = null;

        if (partnerIdStr != null) {
            try {
                int partnerId = Integer.parseInt(partnerIdStr);
                 // Verify the requested partner is valid and is a creator
                 currentPartner = userDao.findById(partnerId);
                 if(currentPartner != null && "creator".equals(currentPartner.getRole())) {
                    conversation = messageDao.getConversation(currentUser.getUserId(), partnerId);
                    // Mark messages from this partner as read
                    messageDao.markMessagesAsRead(currentUser.getUserId(), partnerId);
                    request.setAttribute("currentPartner", currentPartner);
                 } else {
                    request.setAttribute("error", "Invalid chat partner requested.");
                    currentPartner = null; // Reset
                 }

            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid partner ID format.");
            }
        }

        request.setAttribute("chatPartners", chatPartners);
        request.setAttribute("conversation", conversation);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/business/chat.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handles sending a new message
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null || !"business".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Session expired or invalid role");
            return;
        }

        String receiverIdStr = request.getParameter("receiverId");
        String content = request.getParameter("content");
        String campaignIdStr = request.getParameter("campaignId"); // Optional context

        if (receiverIdStr != null && content != null && !content.trim().isEmpty()) {
            try {
                int receiverId = Integer.parseInt(receiverIdStr);
                // Optional: Validate receiverId exists and is a creator user
                 User receiver = userDao.findById(receiverId);
                 if (receiver == null || !"creator".equals(receiver.getRole())) {
                     request.getSession().setAttribute("error", "Cannot send message to this user.");
                     response.sendRedirect(request.getContextPath() + "/business/chat"); // Redirect back
                     return;
                 }

                Message newMessage = new Message();
                newMessage.setSenderUserId(currentUser.getUserId());
                newMessage.setReceiverUserId(receiverId);
                newMessage.setContent(content.trim());

                 if (campaignIdStr != null && !campaignIdStr.isEmpty()) {
                    try {
                        newMessage.setCampaignId(Integer.parseInt(campaignIdStr));
                    } catch (NumberFormatException e) { /* Ignore invalid campaign ID */ }
                }

                boolean success = messageDao.sendMessage(newMessage);

                if (!success) {
                    request.getSession().setAttribute("error", "Failed to send message.");
                     System.err.println("Business " + currentUser.getUsername() + " failed to send message to user ID " + receiverId);
                } else {
                     System.out.println("Business " + currentUser.getUsername() + " sent message to user ID " + receiverId);
                }

                 // Redirect back to the chat with the same partner
                response.sendRedirect(request.getContextPath() + "/business/chat?with=" + receiverId);

            } catch (NumberFormatException e) {
                 request.getSession().setAttribute("error", "Invalid receiver ID.");
                 response.sendRedirect(request.getContextPath() + "/business/chat"); // Redirect back to general chat
                 System.err.println("BusinessChatServlet POST error: Invalid receiverId format");
            }
        } else {
            request.getSession().setAttribute("error", "Receiver ID and message content are required.");
            String redirectUrl = request.getContextPath() + "/business/chat";
             if (receiverIdStr != null && !receiverIdStr.isEmpty()) {
                 redirectUrl += "?with=" + receiverIdStr;
             }
            response.sendRedirect(redirectUrl);
             System.err.println("BusinessChatServlet POST error: Missing parameters (receiverId, content)");
        }
    }
}
