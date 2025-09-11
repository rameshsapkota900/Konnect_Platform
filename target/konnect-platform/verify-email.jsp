<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify Email - Konnect</title>
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
            <div class="form-card">
                <h2>Verify Your Email</h2>

                <% if(request.getAttribute("message") != null) { %>
                    <div class="<%= request.getAttribute("messageType") != null ? request.getAttribute("messageType") : "info" %>-message">
                        <%= request.getAttribute("message") %>
                    </div>
                <% } %>

                <p style="text-align:center;">Please check your email for a verification code. Enter the 6-digit code below to verify your account.</p>

                <!-- Form to enter verification code -->
                <form action="<%= request.getContextPath() %>/verify-email" method="get" style="margin-bottom: 20px;">
                    <label for="code">Verification Code</label>
                    <input type="text" id="code" name="code" placeholder="Enter 6-digit code" required>
                    <button type="submit" class="btn btn-primary">Verify Email</button>
                </form>

                <hr style="margin: 20px 0; border: 0; border-top: 1px solid #eee;">

                <p style="text-align:center;">If you did not receive the email or the code has expired, you can request a new one below.</p>

                <!-- Form to resend verification email -->
                <form action="<%= request.getContextPath() %>/verify-email" method="post">
                    <% if(request.getAttribute("email") != null) { %>
                        <input type="hidden" name="email" value="<%= request.getAttribute("email") %>">
                    <% } else { %>
                        <label for="email">Your Email Address</label>
                        <input type="email" id="email" name="email" required>
                    <% } %>
                    <button type="submit" class="btn btn-secondary">Resend Verification Email</button>
                </form>
            </div>
        </section>
    </main>
    <footer>
        <p>&copy; 2025 Konnect. All rights reserved.</p>
    </footer>
</body>
</html>
