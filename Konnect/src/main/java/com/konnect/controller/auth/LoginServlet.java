package com.konnect.controller.auth;

import com.konnect.dao.UserDao;
import com.konnect.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String redirectUrl = request.getContextPath(); // Base path

        UserDao userDao = new UserDao();
        User user = userDao.authenticateUser(username, password);

        if (user != null) {
            // Authentication successful
            HttpSession session = request.getSession(); // Create session if not exists
            session.setAttribute("user", user); // Store user object in session
             session.setMaxInactiveInterval(30*60); // 30 minutes timeout

            // Redirect based on role
             switch (user.getRole()) {
                 case "admin":
                     redirectUrl += "/admin/dashboard";
                     break;
                 case "creator":
                     redirectUrl += "/creator/dashboard";
                     break;
                 case "business":
                     redirectUrl += "/business/dashboard";
                     break;
                 default:
                     redirectUrl += "/login.jsp?error=Invalid role"; // Should not happen
             }
             System.out.println("Login successful for " + username + ", redirecting to " + redirectUrl);

        } else {
            // Authentication failed
            System.out.println("Login failed for " + username);
             redirectUrl += "/login.jsp?error=Invalid username or password";
        }
        response.sendRedirect(redirectUrl);
    }

     protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect GET requests to login page or dashboard if already logged in
         HttpSession session = request.getSession(false);
         if (session != null && session.getAttribute("user") != null) {
             User user = (User) session.getAttribute("user");
             redirectToDashboard(user.getRole(), response, request.getContextPath());
         } else {
             response.sendRedirect(request.getContextPath() + "/login.jsp");
         }
     }

     private void redirectToDashboard(String role, HttpServletResponse response, String contextPath) throws IOException {
        String dashboardUrl;
        switch (role) {
            case "admin":
                dashboardUrl = contextPath + "/admin/dashboard";
                break;
            case "creator":
                dashboardUrl = contextPath + "/creator/dashboard";
                break;
            case "business":
                dashboardUrl = contextPath + "/business/dashboard";
                break;
            default:
                dashboardUrl = contextPath + "/login.jsp"; // Fallback
        }
        response.sendRedirect(dashboardUrl);
    }
}
