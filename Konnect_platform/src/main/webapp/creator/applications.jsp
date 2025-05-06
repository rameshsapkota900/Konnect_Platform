<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Application" %>
<%
    // Check if user is logged in and has creator role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("creator")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Get applications from request
    List<Application> applications = (List<Application>) request.getAttribute("applications");
    if (applications == null) {
        // If not set, redirect to application servlet to get the data
        response.sendRedirect(request.getContextPath() + "/application");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="My Applications" />
</jsp:include>

<jsp:include page="/includes/nav-creator.jsp">
    <jsp:param name="active" value="applications" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Creator Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-user-circle"></i> Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/application" class="active"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-file-alt"></i> My Applications</h2>
                        <div class="dashboard-actions">
                            <a href="<%= request.getContextPath() %>/campaign" class="btn"><i class="fas fa-bullhorn"></i> Browse Campaigns</a>
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

                    <div class="filter-bar">
                        <div class="filter-group">
                            <label>Filter by:</label>
                            <select id="statusFilter" onchange="filterApplications()">
                                <option value="all">All Status</option>
                                <option value="pending">Pending</option>
                                <option value="approved">Approved</option>
                                <option value="rejected">Rejected</option>
                            </select>
                        </div>
                    </div>

                    <div class="card">
                        <% if (applications.isEmpty()) { %>
                            <div class="empty-state">
                                <i class="fas fa-file-alt empty-icon"></i>
                                <h3>No Applications Yet</h3>
                                <p>You haven't applied to any campaigns yet. Browse available campaigns and submit your first application.</p>
                                <a href="<%= request.getContextPath() %>/campaign" class="btn mt-3"><i class="fas fa-bullhorn"></i> Browse Campaigns</a>
                            </div>
                        <% } else { %>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Campaign</th>
                                            <th>Business</th>
                                            <th>Status</th>
                                            <th>Applied On</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Application app : applications) { %>
                                            <tr class="application-row" data-status="<%= app.getStatus() %>">
                                                <td><a href="<%= request.getContextPath() %>/campaign/view/<%= app.getCampaignId() %>"><%= app.getCampaignTitle() %></a></td>
                                                <td><%= app.getBusinessUsername() %></td>
                                                <td><span class="status-badge status-<%= app.getStatus() %>"><%= app.getStatus() %></span></td>
                                                <td><%= app.getCreatedAt() != null ? app.getCreatedAt().toString().substring(0, 10) : "N/A" %></td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <a href="<%= request.getContextPath() %>/application/view/<%= app.getApplicationId() %>" class="btn-icon" title="View"><i class="fas fa-eye"></i></a>
                                                        <a href="<%= request.getContextPath() %>/message/conversation/<%= app.getBusinessId() %>" class="btn-icon" title="Message"><i class="fas fa-envelope"></i></a>
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
        function filterApplications() {
            const status = document.getElementById('statusFilter').value;
            const rows = document.querySelectorAll('.application-row');
            
            rows.forEach(row => {
                const rowStatus = row.dataset.status;
                if (status === 'all' || status === rowStatus) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
