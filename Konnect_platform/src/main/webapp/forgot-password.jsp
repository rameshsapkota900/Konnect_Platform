<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Forgot Password" />
</jsp:include>

<jsp:include page="/includes/nav-public.jsp">
    <jsp:param name="active" value="login" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="auth-page-wrapper">
                <div class="form-container animate-fade-in">
                <h2 class="form-title"><i class="fas fa-key"></i> Forgot Password</h2>

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

                <p><i class="fas fa-info-circle"></i> Enter your email address and we'll send you instructions to reset your password.</p>

                <form action="forgot-password" method="post">
                    <div class="form-group">
                        <label for="email"><i class="fas fa-envelope"></i> Email</label>
                        <input type="email" id="email" name="email" class="form-control" placeholder="Enter your email address" required>
                    </div>

                    <div class="form-group-centered">
                        <button type="submit" class="btn"><i class="fas fa-paper-plane"></i> Send Reset Instructions</button>
                    </div>

                    <div class="form-text text-center">
                        <p>Remember your password? <a href="login"><i class="fas fa-sign-in-alt"></i> Login here</a></p>
                        <p>Don't have an account? <a href="register"><i class="fas fa-user-plus"></i> Register now</a></p>
                    </div>
                </form>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
