<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Login" />
</jsp:include>

<jsp:include page="/includes/nav-public.jsp">
    <jsp:param name="active" value="login" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="auth-page-wrapper">
                <div class="form-container animate-fade-in mdc-elevation--z4">
                    <h2 class="form-title mdc-typography--headline6">
                        <span class="material-icons">login</span> Login to Your Account
                    </h2>

                    <% if(request.getAttribute("error") != null) { %>
                        <div class="error-message">
                            <span class="material-icons">error_outline</span> <%= request.getAttribute("error") %>
                        </div>
                    <% } %>

                    <% if(request.getAttribute("success") != null) { %>
                        <div class="success-message">
                            <span class="material-icons">check_circle_outline</span> <%= request.getAttribute("success") %>
                        </div>
                    <% } %>

                    <form action="login" method="post">
                        <div class="form-group">
                            <label for="username">USERNAME OR EMAIL</label>
                            <div class="mdc-text-field-container">
                                <input type="text" id="username" name="username" class="form-control" placeholder="Enter your username or email" required>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="password">PASSWORD</label>
                            <div class="mdc-text-field-container">
                                <input type="password" id="password" name="password" class="form-control" placeholder="Enter your password" required>
                            </div>
                            <span class="form-text">
                                <a href="forgot-password" class="mdc-typography--caption">
                                    <span class="material-icons" style="font-size: 14px; vertical-align: middle;">help_outline</span> Forgot password?
                                </a>
                            </span>
                        </div>

                        <div class="form-group-centered">
                            <button type="submit" class="btn ripple">
                                <span class="material-icons">login</span> Login
                            </button>
                        </div>

                        <div class="form-text text-center mdc-typography--body2">
                            Don't have an account?
                            <a href="register">
                                <span class="material-icons" style="font-size: 14px; vertical-align: middle;">person_add</span> Register here
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
