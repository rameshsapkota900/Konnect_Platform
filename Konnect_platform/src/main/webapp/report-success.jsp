<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Check if user is logged in
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Report Submitted" />
</jsp:include>

<jsp:include page="/includes/nav.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="card">
                <div class="card-header">
                    <h2><i class="fas fa-check-circle"></i> Report Submitted</h2>
                </div>
                
                <div class="card-body">
                    <div class="success-container">
                        <div class="success-icon">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <h3>Thank You for Your Report</h3>
                        <p>Your report has been successfully submitted. Our team will review it and take appropriate action if necessary.</p>
                        <p>Report ID: <strong><%= request.getAttribute("reportId") %></strong></p>
                        <div class="mt-4">
                            <a href="<%= request.getContextPath() %>/dashboard" class="btn"><i class="fas fa-home"></i> Return to Dashboard</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
