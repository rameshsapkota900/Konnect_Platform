package com.konnect.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get existing session, don't create new
        if (session != null) {
            session.invalidate(); // Invalidate the session
             System.out.println("User logged out.");
        }
        // Redirect to the login page
        response.sendRedirect(request.getContextPath() + "/login.jsp?message=You have been logged out.");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Typically logout is done via GET, but handle POST just in case
        doGet(request, response);
    }
}
