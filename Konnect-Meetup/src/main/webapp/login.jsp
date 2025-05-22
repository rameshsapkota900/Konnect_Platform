<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/login" class="active">Login</a></li>
                    <li><a href="<%= request.getContextPath() %>/register">Register</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Connect Content Creators and Businesses</div>
    </header>
    <main>
        <section class="form-section">
            <form class="form-card" action="<%= request.getContextPath() %>/login" method="post" style=" max-width: 440px;">
                <h2>Login</h2>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <% if(request.getAttribute("message") != null) { %>
                    <div class="<%= request.getAttribute("messageType") != null ? request.getAttribute("messageType") : "info" %>-message">
                        <%= request.getAttribute("message") %>
                    </div>
                <% } %>

                <label for="email">Email</label>
                <input type="email" id="email" name="email" required>
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
                <button type="submit" class="btn btn-primary">Login</button>
                <a href="<%= request.getContextPath() %>/forgot-password">Forgot password?</a>
                <div style="margin-top: 15px; text-align: center;">
                    <p>Don't have an account? <a href="<%= request.getContextPath() %>/register">Register here</a></p>
                </div>
            </form>
        </section>
    </main>
    <footer>
        <p>&copy; 2025 Konnect. All rights reserved.</p>
    </footer>
</body>
</html>
