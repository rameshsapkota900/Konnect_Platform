<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Verify Email" />
</jsp:include>

<jsp:include page="/includes/nav-public.jsp">
    <jsp:param name="active" value="" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="auth-page-wrapper">
                <div class="form-container animate-fade-in">
                <h2 class="form-title"><i class="fas fa-envelope-open-text"></i> Verify Your Email</h2>

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

                <p><i class="fas fa-info-circle"></i> Please enter the verification code sent to your email address.</p>

                <form action="verify" method="post">
                    <div class="form-group">
                        <label for="email"><i class="fas fa-envelope"></i> Email</label>
                        <input type="email" id="email" name="email" class="form-control" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" placeholder="Enter your email" required>
                    </div>

                    <div class="form-group">
                        <label for="code"><i class="fas fa-key"></i> Verification Code</label>
                        <input type="text" id="code" name="code" class="form-control" placeholder="Enter the 6-digit code" required>
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn"><i class="fas fa-check"></i> Verify Email</button>
                    </div>

                    <div class="form-text text-center">
                        <p>Didn't receive the code? <a href="javascript:void(0);" onclick="resendCode()"><i class="fas fa-paper-plane"></i> Resend Code</a></p>
                    </div>
                </form>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />

    <script>
        function resendCode() {
            var email = document.getElementById('email').value;
            if (email) {
                // Redirect to register page with email parameter
                window.location.href = 'register?email=' + email + '&resend=true';
            } else {
                alert('Please enter your email address');
            }
        }
    </script>
</body>
</html>
