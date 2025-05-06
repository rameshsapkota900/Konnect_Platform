<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Report" %>
<%
    // Check if user is logged in and has admin role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("admin")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get reports from request
    List<Report> reports = (List<Report>) request.getAttribute("reports");
    if (reports == null) {
        // If not set, redirect to admin servlet to get the data
        response.sendRedirect(request.getContextPath() + "/admin/reports");
        return;
    }

    // Get filter value
    String statusFilter = (String) request.getAttribute("statusFilter");
    if (statusFilter == null) statusFilter = "all";
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Manage Reports" />
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
                        <h2 class="dashboard-title"><i class="fas fa-flag"></i> Manage Reports</h2>
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

                    <div class="filter-bar">
                        <form action="<%= request.getContextPath() %>/admin/reports" method="get" id="filterForm">
                            <div class="filter-group">
                                <label>Status:</label>
                                <select name="status" id="statusFilter" onchange="document.getElementById('filterForm').submit()">
                                    <option value="all" <%= statusFilter.equals("all") ? "selected" : "" %>>All Status</option>
                                    <option value="pending" <%= statusFilter.equals("pending") ? "selected" : "" %>>Pending</option>
                                    <option value="resolved" <%= statusFilter.equals("resolved") ? "selected" : "" %>>Resolved</option>
                                    <option value="dismissed" <%= statusFilter.equals("dismissed") ? "selected" : "" %>>Dismissed</option>
                                </select>
                            </div>
                        </form>
                    </div>

                    <div class="card">
                        <% if (reports.isEmpty()) { %>
                            <div class="empty-state">
                                <i class="fas fa-flag empty-icon"></i>
                                <h3>No Reports Found</h3>
                                <p>There are no reports matching your filter criteria.</p>
                            </div>
                        <% } else { %>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Reporter</th>
                                            <th>Reported User</th>
                                            <th>Reason</th>
                                            <th>Status</th>
                                            <th>Date</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Report report : reports) { %>
                                            <tr>
                                                <td><%= report.getReportId() %></td>
                                                <td><a href="<%= request.getContextPath() %>/admin/user/<%= report.getReporterId() %>"><%= report.getReporterUsername() %></a></td>
                                                <td><a href="<%= request.getContextPath() %>/admin/user/<%= report.getReportedUserId() %>"><%= report.getReportedUsername() %></a></td>
                                                <td><%= report.getReason() %></td>
                                                <td><span class="status-badge status-<%= report.getStatus() %>"><%= report.getStatus() %></span></td>
                                                <td><%= report.getCreatedAt() != null ? report.getCreatedAt().toString().substring(0, 10) : "N/A" %></td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <a href="<%= request.getContextPath() %>/admin/report/<%= report.getReportId() %>" class="btn-icon" title="View"><i class="fas fa-eye"></i></a>
                                                        <% if (report.getStatus().equals("pending")) { %>
                                                            <a href="#" onclick="resolveReport(<%= report.getReportId() %>)" class="btn-icon" title="Resolve"><i class="fas fa-check"></i></a>
                                                            <a href="#" onclick="dismissReport(<%= report.getReportId() %>)" class="btn-icon" title="Dismiss"><i class="fas fa-times"></i></a>
                                                        <% } %>
                                                    </div>
                                                </td>
                                            </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>
                        <% } %>
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
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
