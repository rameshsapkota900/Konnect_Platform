<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enter Reset Code - Konnect</title>
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
            <form class="form-card" action="<%= request.getContextPath() %>/enter-reset-code" method="post" style="max-width: 440px;">
                <h2>Enter Reset Code</h2>
                
                <% if(request.getAttribute("message") != null) { %>
                    <div class="<%= request.getAttribute("messageType") != null ? request.getAttribute("messageType") : "info" %>-message">
                        <%= request.getAttribute("message") %>
                    </div>
                <% } %>

                <p style="text-align:center;">Please enter the 6-digit code that was sent to your email address.</p>
                
                <label for="resetCode">Reset Code</label>
                <input type="text" id="resetCode" name="resetCode" placeholder="Enter 6-digit code" required>
                <button type="submit" class="btn btn-primary">Verify Code</button>
                <a href="<%= request.getContextPath() %>/forgot-password" class="btn btn-secondary">Back to Forgot Password</a>
            </form>
        </section>
    </main>
    <footer>
        <p>&copy; 2025 Konnect. All rights reserved.</p>
    </footer>
</body>
</html>
