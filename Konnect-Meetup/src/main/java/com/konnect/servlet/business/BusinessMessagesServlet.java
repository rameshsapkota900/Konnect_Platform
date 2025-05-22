package com.konnect.servlet.business;

import com.konnect.dao.MessageDAO;
import com.konnect.dao.UserDAO;
import com.konnect.model.Message;
import com.konnect.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * BusinessMessagesServlet
 * Handles messaging for businesses
 */
@WebServlet("/business/messages")
public class BusinessMessagesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MessageDAO messageDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() {
        messageDAO = new MessageDAO();
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - display messages
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
        
        User currentUser = (User) session.getAttribute("user");
        if (!"business".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get conversation partner ID if provided
        String partnerIdStr = request.getParameter("userId");
        User partner = null;
        
        if (partnerIdStr != null && !partnerIdStr.trim().isEmpty()) {
            try {
                int partnerId = Integer.parseInt(partnerIdStr);
                partner = userDAO.getById(partnerId);
                
                if (partner != null) {
                    // Mark messages from this user as read
                    messageDAO.markAsRead(partner.getId(), currentUser.getId());
                }
            } catch (NumberFormatException e) {
                // Invalid user ID, ignore
            }
        }
        
        // Get all conversation partners
        List<Integer> conversationPartnerIds = messageDAO.getUserConversations(currentUser.getId());
        List<User> conversationPartners = userDAO.getByIds(conversationPartnerIds);
        
        // Get messages for the selected conversation
        List<Message> messages = null;
        if (partner != null) {
            messages = messageDAO.getConversation(currentUser.getId(), partner.getId());
        }
        
        // Get unread message counts
        int totalUnreadCount = messageDAO.getUnreadCount(currentUser.getId());
        
        // Set attributes for the view
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("partner", partner);
        request.setAttribute("conversationPartners", conversationPartners);
        request.setAttribute("messages", messages);
        request.setAttribute("totalUnreadCount", totalUnreadCount);
        
        // Forward to messages page
        request.getRequestDispatcher("/business/messages.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - send message
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
        
        User currentUser = (User) session.getAttribute("user");
        if (!"business".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get form data
        String receiverIdStr = request.getParameter("receiverId");
        String content = request.getParameter("content");
        
        // Validate input
        if (receiverIdStr == null || receiverIdStr.trim().isEmpty() || 
            content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "Receiver ID and message content are required");
            doGet(request, response);
            return;
        }
        
        // Parse receiver ID
        int receiverId;
        try {
            receiverId = Integer.parseInt(receiverIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid receiver ID");
            doGet(request, response);
            return;
        }
        
        // Check if receiver exists
        User receiver = userDAO.getById(receiverId);
        if (receiver == null) {
            request.setAttribute("error", "Receiver not found");
            doGet(request, response);
            return;
        }
        
        // Create message
        Message message = new Message();
        message.setSenderId(currentUser.getId());
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setRead(false);
        
        // Save message
        int messageId = messageDAO.insert(message);
        
        if (messageId > 0) {
            // Redirect back to conversation
            response.sendRedirect(request.getContextPath() + "/business/messages?userId=" + receiverId);
        } else {
            // Set error message
            request.setAttribute("error", "Failed to send message. Please try again.");
            doGet(request, response);
        }
    }
}
