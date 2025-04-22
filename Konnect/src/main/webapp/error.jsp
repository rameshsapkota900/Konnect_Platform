<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Error"/>
</jsp:include>

<div class="container text-center">
    <h2 class="text-danger">Oops! Something went wrong.</h2>

    <%
        String errorMessage = "An unexpected error occurred."; // Default message
        Object exceptionObj = request.getAttribute("jakarta.servlet.error.exception"); // Standard attribute for exceptions

        if (exceptionObj instanceof Exception) {
            Exception e = (Exception) exceptionObj;
            if (e.getMessage() != null && !e.getMessage().isEmpty()) {
                errorMessage = e.getMessage();
            }
            Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
            if (statusCode != null) {
                errorMessage += " (Status Code: " + statusCode + ")";
            }
        } else {
            String customError = (String) request.getAttribute("errorMessage");
            if (customError != null) {
                errorMessage = customError;
            }
        }
    %>

    <p class="lead">We encountered an issue processing your request.</p>
    <p><%= errorMessage %></p>

    <div class="mt-4">
        <p>You can try:</p>
        <ul style="list-style: none; padding: 0;">
            <li class="mb-2"><a href="javascript:history.back()" class="btn">Go Back</a></li>
            <li class="mb-2"><a href="<%= request.getContextPath() %>/" class="btn">Return to Dashboard</a></li>
        </ul>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
