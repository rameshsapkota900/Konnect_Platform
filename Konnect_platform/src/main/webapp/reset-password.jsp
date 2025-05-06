<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Reset Password" />
</jsp:include>

<jsp:include page="/includes/nav-public.jsp">
    <jsp:param name="active" value="login" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="auth-page-wrapper">
                <div class="form-container animate-fade-in">
                <h2 class="form-title"><i class="fas fa-lock-open"></i> Reset Password</h2>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <% if(request.getAttribute("success") != null) { %>
                    <div class="success-message">
                        <i class="fas fa-check-circle"></i> <%= request.getAttribute("success") %>
                    </div>
                <% } %>

                <p><i class="fas fa-info-circle"></i> Enter the reset code sent to your email and your new password.</p>

                <form action="reset-password" method="post">
                    <div class="form-group">
                        <label for="email"><i class="fas fa-envelope"></i> Email</label>
                        <input type="email" id="email" name="email" class="form-control" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" placeholder="Enter your email" required>
                    </div>

                    <div class="form-group">
                        <label for="code"><i class="fas fa-key"></i> Reset Code</label>
                        <input type="text" id="code" name="code" class="form-control" placeholder="Enter the 6-digit code" required>
                    </div>

                    <div class="form-group">
                        <label for="password"><i class="fas fa-lock"></i> New Password</label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="Create a new password" required>
                        <span class="form-text"><i class="fas fa-shield-alt"></i> Choose a strong password with at least 8 characters</span>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword"><i class="fas fa-check-circle"></i> Confirm New Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Confirm your new password" required>
                    </div>

                    <div class="form-group-centered">
                        <button type="submit" class="btn"><i class="fas fa-sync-alt"></i> Reset Password</button>
                    </div>

                    <div class="form-text text-center">
                        <p>Remember your password? <a href="login"><i class="fas fa-sign-in-alt"></i> Login here</a></p>
                    </div>
                </form>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
