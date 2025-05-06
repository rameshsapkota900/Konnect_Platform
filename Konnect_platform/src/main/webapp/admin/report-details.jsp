<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.Report" %>
<%
    // Check if user is logged in and has admin role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("admin")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get report from request
    Report report = (Report) request.getAttribute("report");
    if (report == null) {
        // If not set, redirect to reports page
        response.sendRedirect(request.getContextPath() + "/admin/reports");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Report Details" />
</jsp:include>

<jsp:include page="/includes/nav-admin.jsp">
    <jsp:param name="active" value="reports" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Admin Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/admin/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/users"><i class="fas fa-users"></i> Manage Users</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/campaigns"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/reports" class="active"><i class="fas fa-flag"></i> Reports</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title">
                            <a href="<%= request.getContextPath() %>/admin/reports" class="back-link"><i class="fas fa-arrow-left"></i></a>
                            Report Details
                        </h2>
                        <div class="dashboard-actions">
                            <% if (report.getStatus().equals("pending")) { %>
                                <a href="#" onclick="resolveReport(<%= report.getReportId() %>)" class="btn btn-success"><i class="fas fa-check"></i> Resolve</a>
                                <a href="#" onclick="dismissReport(<%= report.getReportId() %>)" class="btn btn-danger"><i class="fas fa-times"></i> Dismiss</a>
                            <% } %>
                        </div>
                    </div>

                    <% if (request.getAttribute("success") != null) { %>
                    <div class="success-message">
                        <i class="fas fa-check-circle"></i> <%= request.getAttribute("success") %>
                    </div>
                    <% } %>

                    <% if (request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
                    </div>
                    <% } %>

                    <div class="card">
                        <div class="report-details">
                            <div class="report-header">
                                <h3>Report #<%= report.getReportId() %></h3>
                                <span class="status-badge status-<%= report.getStatus() %> status-large"><%= report.getStatus() %></span>
                            </div>

                            <div class="report-section">
                                <h4>Report Information</h4>
                                <div class="detail-grid">
                                    <div class="detail-item">
                                        <span class="detail-label">Reporter:</span>
                                        <span class="detail-value"><a href="<%= request.getContextPath() %>/admin/user/<%= report.getReporterId() %>"><%= report.getReporterUsername() %></a></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Reported User:</span>
                                        <span class="detail-value"><a href="<%= request.getContextPath() %>/admin/user/<%= report.getReportedUserId() %>"><%= report.getReportedUsername() %></a></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Reason:</span>
                                        <span class="detail-value"><%= report.getReason() %></span>
                                    </div>
                                    <div class="detail-item">
                                        <span class="detail-label">Date Reported:</span>
                                        <span class="detail-value"><%= report.getCreatedAt() != null ? report.getCreatedAt().toString() : "N/A" %></span>
                                    </div>
                                    <% if (!report.getStatus().equals("pending")) { %>
                                        <div class="detail-item">
                                            <span class="detail-label">Date Resolved:</span>
                                            <span class="detail-value"><%= report.getUpdatedAt() != null ? report.getUpdatedAt().toString() : "N/A" %></span>
                                        </div>
                                    <% } %>
                                </div>
                            </div>

                            <div class="report-section">
                                <h4>Description</h4>
                                <div class="report-description">
                                    <p><%= report.getDescription() %></p>
                                </div>
                            </div>

                            <div class="report-actions">
                                <a href="<%= request.getContextPath() %>/admin/user/<%= report.getReportedUserId() %>" class="btn"><i class="fas fa-user"></i> View Reported User</a>
                                <% if (report.getStatus().equals("pending")) { %>
                                    <a href="#" onclick="banUser(<%= report.getReportedUserId() %>)" class="btn btn-danger"><i class="fas fa-ban"></i> Ban Reported User</a>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script>
        function resolveReport(reportId) {
            if (confirm("Are you sure you want to mark this report as resolved?")) {
                window.location.href = "<%= request.getContextPath() %>/admin/report/resolve/" + reportId;
            }
        }

        function dismissReport(reportId) {
            if (confirm("Are you sure you want to dismiss this report?")) {
                window.location.href = "<%= request.getContextPath() %>/admin/report/dismiss/" + reportId;
            }
        }

        function banUser(userId) {
            if (confirm("Are you sure you want to ban this user? They will no longer be able to access the platform.")) {
                window.location.href = "<%= request.getContextPath() %>/admin/ban-user/" + userId;
            }
        }
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
