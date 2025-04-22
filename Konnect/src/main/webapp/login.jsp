<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Login"/>
</jsp:include>

<div class="auth-container">
    <h2>Login to Konnect</h2>

    <%-- Display login errors passed via query parameter --%>
    <% String error = request.getParameter("error"); %>
    <% if (error != null && !error.isEmpty()) { %>
        <div class="alert alert-danger" role="alert">
            <%= error %>
        </div>
    <% } %>
    <%-- Display success messages (e.g., after registration) --%>
    <% String message = request.getParameter("message"); %>
    <% if (message != null && !message.isEmpty()) { %>
        <div class="alert alert-success" role="alert">
            <%= message %>
        </div>
    <% } %>

    <form action="LoginServlet" method="POST" class="needs-validation" novalidate>
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" class="form-control" required autofocus>
            <div class="invalid-feedback">Please enter your username.</div>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" class="form-control" required>
            <div class="invalid-feedback">Please enter your password.</div>
        </div>
        <button type="submit" class="btn btn-primary btn-full-width">Login</button>
    </form>

    <div class="form-links">
        Don't have an account? <a href="register.jsp">Register here</a>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
