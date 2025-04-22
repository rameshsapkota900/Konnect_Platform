package com.konnect.controller.admin;

import com.konnect.dao.UserDao;
import com.konnect.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao = new UserDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = userDao.getAllUsers();
        request.setAttribute("userList", userList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/users.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");
         String currentStatusStr = request.getParameter("currentStatus"); // To toggle

        if (action != null && userIdStr != null && currentStatusStr != null) {
            try {
                int userId = Integer.parseInt(userIdStr);
                 boolean currentStatus = Boolean.parseBoolean(currentStatusStr);

                if ("toggleStatus".equals(action)) {
                    boolean newStatus = !currentStatus; // Toggle the status
                    boolean success = userDao.updateUserStatus(userId, newStatus);
                    if (success) {
                        request.getSession().setAttribute("message", "User status updated successfully.");
                         System.out.println("Admin toggled user " + userId + " status to " + newStatus);
                    } else {
                        request.getSession().setAttribute("error", "Failed to update user status.");
                         System.err.println("Admin failed to toggle status for user " + userId);
                    }
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid user ID.");
                 System.err.println("Admin UserServlet POST error: Invalid userId format");
            }
        } else {
             request.getSession().setAttribute("error", "Missing required parameters for user action.");
             System.err.println("Admin UserServlet POST error: Missing parameters (action, userId, currentStatus)");
        }

        // Redirect back to the user list page after action
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
  }
