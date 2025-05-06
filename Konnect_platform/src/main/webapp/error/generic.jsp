<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Error" />
</jsp:include>

<jsp:include page="/includes/nav-public.jsp">
    <jsp:param name="active" value="" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="error-container">
                <h1>Error</h1>
                <% if(request.getAttribute("errorCode") != null) { %>
                    <p>Error Code: <%= request.getAttribute("errorCode") %></p>
                <% } %>

                <% if(request.getAttribute("errorMessage") != null) { %>
                    <p>Error Message: <%= request.getAttribute("errorMessage") %></p>
                <% } else { %>
                    <p>An unexpected error occurred. Please try again later.</p>
                <% } %>

                <a href="../index.jsp" class="btn">Go to Home Page</a>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
