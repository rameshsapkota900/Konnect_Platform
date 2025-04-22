<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Register"/>
</jsp:include>

<div class="auth-container">
    <h2>Register for Konnect</h2>

    <%-- Display registration errors passed via query parameter --%>
    <% String error = request.getParameter("error"); %>
    <% if (error != null && !error.isEmpty()) { %>
        <div class="alert alert-danger" role="alert">
            <%= error %>
        </div>
    <% } %>

    <form action="RegisterServlet" method="POST" class="needs-validation" novalidate>
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" class="form-control" required minlength="3">
            <div class="invalid-feedback">Please choose a username (at least 3 characters).</div>
        </div>
        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" class="form-control" required>
            <div class="invalid-feedback">Please enter a valid email address.</div>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" class="form-control" required minlength="6">
            <div class="invalid-feedback">Password must be at least 6 characters long.</div>
        </div>
        <div class="form-group">
            <label for="confirmPassword">Confirm Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
            <div class="invalid-feedback">Please confirm your password.</div>
        </div>
        <div class="form-group">
            <label for="role">Register As:</label>
            <select id="role" name="role" class="form-control" required>
                <option value="" disabled selected>-- Select Role --</option>
                <option value="creator">Content Creator</option>
                <option value="business">Business Owner</option>
            </select>
            <div class="invalid-feedback">Please select your role.</div>
        </div>
        <button type="submit" class="btn btn-primary btn-full-width">Register</button>
    </form>

    <div class="form-links">
        Already have an account? <a href="login.jsp">Login here</a>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
