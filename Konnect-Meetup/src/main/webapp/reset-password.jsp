<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password - Konnect</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
    <header>
        <div class="header-row">
            <a href="<%= request.getContextPath() %>/" class="header-logo">Konnect</a>
            <nav>
                <ul class="nav-list">
                    <li><a href="<%= request.getContextPath() %>/">Home</a></li>
                    <li><a href="<%= request.getContextPath() %>/about">About</a></li>
                    <li><a href="<%= request.getContextPath() %>/contact">Contact</a></li>
                    <li><a href="<%= request.getContextPath() %>/login">Login</a></li>
                    <li><a href="<%= request.getContextPath() %>/register">Register</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Connect Content Creators and Businesses</div>
    </header>
    <main>
        <section class="form-section">
            <form class="form-card" action="<%= request.getContextPath() %>/reset-password" method="post" style="max-width: 440px;">
                <h2>Reset Password</h2>
                
                <% if(request.getAttribute("message") != null) { %>
                    <div class="<%= request.getAttribute("messageType") != null ? request.getAttribute("messageType") : "info" %>-message">
                        <%= request.getAttribute("message") %>
                    </div>
                <% } %>

                <% if(request.getParameter("code") != null) { %>
                    <input type="hidden" name="code" value="<%= request.getParameter("code") %>">
                <% } %>

                <label for="password">New Password</label>
                <input type="password" id="password" name="password" required>
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
                <button type="submit" class="btn btn-primary">Reset Password</button>
                <a href="<%= request.getContextPath() %>/login" class="btn btn-secondary">Back to Login</a>
            </form>
        </section>
    </main>
    <footer>
        <p>&copy; 2025 Konnect. All rights reserved.</p>
    </footer>
</body>
</html>
