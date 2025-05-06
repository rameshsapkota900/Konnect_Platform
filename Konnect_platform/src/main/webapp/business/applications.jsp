<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Application" %>
<%
    // Check if user is logged in and has business role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("business")) {
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
    <jsp:param name="title" value="Campaign Applications" />
</jsp:include>

<jsp:include page="/includes/nav-business.jsp">
    <jsp:param name="active" value="applications" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Business Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-building"></i> Company Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile/creators"><i class="fas fa-users"></i> Find Creators</a></li>
                        <li><a href="<%= request.getContextPath() %>/application" class="active"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-file-alt"></i> Campaign Applications</h2>
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
                            <select id="campaignFilter" onchange="filterApplications()">
                                <option value="all">All Campaigns</option>
                                <!-- Campaign options would be dynamically generated -->
                            </select>
                        </div>
                        <div class="filter-group">
                            <label>Status:</label>
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
                                <p>You haven't received any applications for your campaigns yet. Create more campaigns or invite creators to apply.</p>
                                <a href="<%= request.getContextPath() %>/campaign/create" class="btn mt-3"><i class="fas fa-plus-circle"></i> Create Campaign</a>
                                <a href="<%= request.getContextPath() %>/profile/creators" class="btn mt-3"><i class="fas fa-users"></i> Find Creators</a>
                            </div>
                        <% } else { %>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Creator</th>
                                            <th>Campaign</th>
                                            <th>Status</th>
                                            <th>Applied On</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Application app : applications) { %>
                                            <tr class="application-row" data-campaign="<%= app.getCampaignId() %>" data-status="<%= app.getStatus() %>">
                                                <td><a href="<%= request.getContextPath() %>/profile/view/<%= app.getCreatorId() %>"><%= app.getCreatorUsername() %></a></td>
                                                <td><a href="<%= request.getContextPath() %>/campaign/view/<%= app.getCampaignId() %>"><%= app.getCampaignTitle() %></a></td>
                                                <td><span class="status-badge status-<%= app.getStatus() %>"><%= app.getStatus() %></span></td>
                                                <td><%= app.getCreatedAt() != null ? app.getCreatedAt().toString().substring(0, 10) : "N/A" %></td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <a href="<%= request.getContextPath() %>/application/view/<%= app.getApplicationId() %>" class="btn-icon" title="View"><i class="fas fa-eye"></i></a>
                                                        <a href="<%= request.getContextPath() %>/message/conversation/<%= app.getCreatorId() %>" class="btn-icon" title="Message"><i class="fas fa-envelope"></i></a>
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
        // Populate campaign filter options
        document.addEventListener('DOMContentLoaded', function() {
            const campaignFilter = document.getElementById('campaignFilter');
            const rows = document.querySelectorAll('.application-row');
            const campaigns = new Map();
            
            rows.forEach(row => {
                const campaignId = row.dataset.campaign;
                const campaignTitle = row.querySelector('td:nth-child(2) a').textContent;
                campaigns.set(campaignId, campaignTitle);
            });
            
            campaigns.forEach((title, id) => {
                const option = document.createElement('option');
                option.value = id;
                option.textContent = title;
                campaignFilter.appendChild(option);
            });
        });
        
        function filterApplications() {
            const campaignId = document.getElementById('campaignFilter').value;
            const status = document.getElementById('statusFilter').value;
            const rows = document.querySelectorAll('.application-row');
            
            rows.forEach(row => {
                const rowCampaignId = row.dataset.campaign;
                const rowStatus = row.dataset.status;
                
                const campaignMatch = campaignId === 'all' || campaignId === rowCampaignId;
                const statusMatch = status === 'all' || status === rowStatus;
                
                if (campaignMatch && statusMatch) {
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
