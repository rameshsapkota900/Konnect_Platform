package com.konnect.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

/**
 * Servlet for handling message operations
 */
@WebServlet("/message/*")
public class MessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private MessageDAO messageDAO;
    private UserDAO userDAO;
    
    public void init() {
        messageDAO = new MessageDAO();
        userDAO = new UserDAO();
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
        
        // Admin cannot use messaging
        if (role.equals("admin")) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // List conversations
            List<Integer> conversationUserIds = messageDAO.getUserConversations(userId);
            List<User> conversationUsers = new ArrayList<>();
            
            for (int conversationUserId : conversationUserIds) {
                User user = userDAO.getUserById(conversationUserId);
                if (user != null) {
                    conversationUsers.add(user);
                }
            }
            
            request.setAttribute("conversationUsers", conversationUsers);
            request.setAttribute("unreadCount", messageDAO.countUnreadMessages(userId));
            request.getRequestDispatcher("/" + role + "/messages.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/conversation/")) {
            // View conversation with specific user
            try {
                int otherUserId = Integer.parseInt(pathInfo.substring(14));
                User otherUser = userDAO.getUserById(otherUserId);
                
                if (otherUser != null) {
                    // Get conversation messages
                    List<Message> messages = messageDAO.getConversation(userId, otherUserId);
                    
                    request.setAttribute("messages", messages);
                    request.setAttribute("otherUser", otherUser);
                    request.getRequestDispatcher("/" + role + "/conversation.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/message");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/message");
            }
        } else if (pathInfo.equals("/new")) {
            // Show new message form
            String recipientRole = role.equals("creator") ? "business" : "creator";
            List<User> potentialRecipients = userDAO.getUsersByRole(recipientRole);
            
            request.setAttribute("recipients", potentialRecipients);
            request.getRequestDispatcher("/" + role + "/new-message.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/message");
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
        
        // Admin cannot use messaging
        if (role.equals("admin")) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        if (pathInfo.equals("/send")) {
            // Send new message
            String receiverIdStr = request.getParameter("receiverId");
            String content = request.getParameter("content");
            
            // Validate input
            if (receiverIdStr == null || receiverIdStr.trim().isEmpty() || 
                content == null || content.trim().isEmpty()) {
                
                request.setAttribute("error", "Recipient and message content are required");
                
                if (pathInfo.startsWith("/conversation/")) {
                    // Redirect back to conversation
                    response.sendRedirect(request.getContextPath() + pathInfo);
                } else {
                    // Redirect to new message form
                    response.sendRedirect(request.getContextPath() + "/message/new");
                }
                return;
            }
            
            try {
                int receiverId = Integer.parseInt(receiverIdStr);
                User receiver = userDAO.getUserById(receiverId);
                
                if (receiver != null) {
                    // Create message object
                    Message message = new Message(userId, receiverId, content);
                    
                    // Save message
                    int messageId = messageDAO.sendMessage(message);
                    
                    if (messageId > 0) {
                        // Message sent successfully
                        request.setAttribute("success", "Message sent successfully");
                    } else {
                        // Message sending failed
                        request.setAttribute("error", "Failed to send message");
                    }
                    
                    // Redirect to conversation
                    response.sendRedirect(request.getContextPath() + "/message/conversation/" + receiverId);
                } else {
                    response.sendRedirect(request.getContextPath() + "/message");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/message");
            }
        } else if (pathInfo.startsWith("/reply/")) {
            // Reply to conversation
            try {
                int receiverId = Integer.parseInt(pathInfo.substring(7));
                String content = request.getParameter("content");
                
                // Validate input
                if (content == null || content.trim().isEmpty()) {
                    request.setAttribute("error", "Message content is required");
                    response.sendRedirect(request.getContextPath() + "/message/conversation/" + receiverId);
                    return;
                }
                
                User receiver = userDAO.getUserById(receiverId);
                
                if (receiver != null) {
                    // Create message object
                    Message message = new Message(userId, receiverId, content);
                    
                    // Save message
                    int messageId = messageDAO.sendMessage(message);
                    
                    if (messageId > 0) {
                        // Message sent successfully
                        request.setAttribute("success", "Message sent successfully");
                    } else {
                        // Message sending failed
                        request.setAttribute("error", "Failed to send message");
                    }
                    
                    // Redirect to conversation
                    response.sendRedirect(request.getContextPath() + "/message/conversation/" + receiverId);
                } else {
                    response.sendRedirect(request.getContextPath() + "/message");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/message");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/message");
        }
    }
}
