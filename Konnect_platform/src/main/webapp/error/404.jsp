<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="404 - Page Not Found" />
</jsp:include>

<jsp:include page="/includes/nav-public.jsp">
    <jsp:param name="active" value="" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="error-container">
                <h1>404 - Page Not Found</h1>
                <p>The page you are looking for does not exist or has been moved.</p>
                <a href="../index.jsp" class="btn">Go to Home Page</a>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
