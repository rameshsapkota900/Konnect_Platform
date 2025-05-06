<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.User" %>
<%
    // Check if user is logged in
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Get reported user from request
    User reportedUser = (User) request.getAttribute("reportedUser");
    if (reportedUser == null) {
        // If not set, redirect to dashboard
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Report User" />
</jsp:include>

<jsp:include page="/includes/nav.jsp" />

    <main class="main-content">
        <div class="container">
            <div class="card">
                <div class="card-header">
                    <h2><i class="fas fa-flag"></i> Report User</h2>
                </div>
                
                <div class="card-body">
                    <div class="report-info">
                        <p>You are reporting: <strong><%= reportedUser.getUsername() %></strong></p>
                        <p>Please provide details about why you are reporting this user. Our team will review your report and take appropriate action.</p>
                    </div>
                    
                    <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
                    </div>
                    <% } %>
                    
                    <form action="<%= request.getContextPath() %>/report/submit" method="post" class="form">
                        <input type="hidden" name="reportedUserId" value="<%= reportedUser.getUserId() %>">
                        
                        <div class="form-group">
                            <label for="reason">Reason for Report <span class="required">*</span></label>
                            <select id="reason" name="reason" class="form-control" required>
                                <option value="" disabled selected>Select a reason</option>
                                <option value="Inappropriate Content">Inappropriate Content</option>
                                <option value="Harassment">Harassment</option>
                                <option value="Spam">Spam</option>
                                <option value="Scam">Scam or Fraud</option>
                                <option value="Impersonation">Impersonation</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="description">Description <span class="required">*</span></label>
                            <textarea id="description" name="description" class="form-control" rows="5" required></textarea>
                            <div class="form-text">Please provide specific details about the issue.</div>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary"><i class="fas fa-paper-plane"></i> Submit Report</button>
                            <a href="javascript:history.back()" class="btn btn-outline">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
