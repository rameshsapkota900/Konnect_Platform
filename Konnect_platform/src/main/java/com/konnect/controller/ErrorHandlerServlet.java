package com.konnect.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet for handling errors
 */
@WebServlet("/error")
public class ErrorHandlerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get error code
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");
        
        if (statusCode != null) {
            // Handle specific error codes
            switch (statusCode) {
                case 404:
                    request.getRequestDispatcher("/error/404.jsp").forward(request, response);
                    break;
                case 500:
                    request.getRequestDispatcher("/error/500.jsp").forward(request, response);
                    break;
                default:
                    request.setAttribute("errorCode", statusCode);
                    request.setAttribute("errorMessage", errorMessage);
                    request.getRequestDispatcher("/error/generic.jsp").forward(request, response);
            }
        } else {
            // Default error handling
            request.getRequestDispatcher("/error/generic.jsp").forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
