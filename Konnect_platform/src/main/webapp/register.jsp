<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Register" />
</jsp:include>

<jsp:include page="/includes/nav-public.jsp">
    <jsp:param name="active" value="register" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="auth-page-wrapper register-page-wrapper">
                <div class="form-container animate-fade-in">
                <h2 class="form-title"><i class="fas fa-user-plus"></i> Create Your Account</h2>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <form action="register" method="post">
                    <div class="form-group">
                        <label for="username"><i class="fas fa-user"></i> Username</label>
                        <input type="text" id="username" name="username" class="form-control" placeholder="Choose a username" required>
                        <span class="form-text"><i class="fas fa-info-circle"></i> Choose a unique username for your account</span>
                    </div>

                    <div class="form-group">
                        <label for="email"><i class="fas fa-envelope"></i> Email</label>
                        <input type="email" id="email" name="email" class="form-control" placeholder="Enter your email" required>
                        <span class="form-text"><i class="fas fa-shield-alt"></i> We'll never share your email with anyone else</span>
                    </div>

                    <div class="form-group">
                        <label for="password"><i class="fas fa-lock"></i> Password</label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="Create a password" required>
                        <span class="form-text"><i class="fas fa-key"></i> Choose a strong password with at least 8 characters</span>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword"><i class="fas fa-check-circle"></i> Confirm Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Confirm your password" required>
                    </div>

                    <div class="form-group">
                        <label><i class="fas fa-users"></i> Account Type</label>
                        <div class="radio-group">
                            <label>
                                <input type="radio" name="role" value="creator" <%= request.getParameter("role") != null && request.getParameter("role").equals("creator") ? "checked" : "" %> required>
                                <i class="fas fa-user-circle"></i> Content Creator
                            </label>
                            <label>
                                <input type="radio" name="role" value="business" <%= request.getParameter("role") != null && request.getParameter("role").equals("business") ? "checked" : "" %> required>
                                <i class="fas fa-briefcase"></i> Business Owner
                            </label>
                        </div>
                    </div>

                    <div class="form-group-centered">
                        <button type="submit" class="btn"><i class="fas fa-user-plus"></i> Register</button>
                    </div>

                    <div class="form-text text-center">
                        Already have an account? <a href="login"><i class="fas fa-sign-in-alt"></i> Login here</a>
                    </div>
                </form>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
