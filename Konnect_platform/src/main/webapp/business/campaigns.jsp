<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Campaign" %>
<%
    // Check if user is logged in and has business role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("business")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get campaigns from request
    List<Campaign> campaigns = (List<Campaign>) request.getAttribute("campaigns");
    if (campaigns == null) {
        // If not set, redirect to campaign servlet to get the data
        response.sendRedirect(request.getContextPath() + "/campaign");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="My Campaigns" />
</jsp:include>

<jsp:include page="/includes/nav-business.jsp">
    <jsp:param name="active" value="campaigns" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Business Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-building"></i> Company Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign" class="active"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile/creators"><i class="fas fa-users"></i> Find Creators</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-bullhorn"></i> My Campaigns</h2>
                        <div class="dashboard-actions">
                            <a href="<%= request.getContextPath() %>/campaign/create" class="btn btn-fixed-height"><i class="fas fa-plus-circle"></i> Create Campaign</a>
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
                        <% if (campaigns.isEmpty()) { %>
                            <div class="empty-state">
                                <i class="fas fa-bullhorn empty-icon"></i>
                                <h3>No Campaigns Yet</h3>
                                <p>You haven't created any campaigns yet. Create your first campaign to start connecting with content creators.</p>
                                <a href="<%= request.getContextPath() %>/campaign/create" class="btn btn-fixed-height mt-3"><i class="fas fa-plus-circle"></i> Create Campaign</a>
                            </div>
                        <% } else { %>
                            <div class="table-responsive">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <th>Title</th>
                                            <th>Budget</th>
                                            <th>Status</th>
                                            <th>Applications</th>
                                            <th>Created</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Campaign campaign : campaigns) { %>
                                            <tr>
                                                <td><a href="<%= request.getContextPath() %>/campaign/view/<%= campaign.getCampaignId() %>"><%= campaign.getTitle() %></a></td>
                                                <td>$<%= String.format("%.2f", campaign.getBudget()) %></td>
                                                <td><span class="status-badge status-<%= campaign.getStatus() %>"><%= campaign.getStatus() %></span></td>
                                                <td>0</td>
                                                <td><%= campaign.getCreatedAt() != null ? campaign.getCreatedAt().toString().substring(0, 10) : "N/A" %></td>
                                                <td>
                                                    <div class="action-buttons">
                                                        <a href="<%= request.getContextPath() %>/campaign/view/<%= campaign.getCampaignId() %>" class="btn-icon" title="View"><i class="fas fa-eye"></i></a>
                                                        <a href="<%= request.getContextPath() %>/campaign/edit/<%= campaign.getCampaignId() %>" class="btn-icon" title="Edit"><i class="fas fa-edit"></i></a>
                                                        <a href="#" onclick="confirmDelete(<%= campaign.getCampaignId() %>)" class="btn-icon" title="Delete"><i class="fas fa-trash-alt"></i></a>
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
        function confirmDelete(campaignId) {
            if (confirm("Are you sure you want to delete this campaign? This action cannot be undone.")) {
                window.location.href = "<%= request.getContextPath() %>/campaign/delete/" + campaignId;
            }
        }
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
