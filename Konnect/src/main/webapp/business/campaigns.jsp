<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.NumberFormat" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="My Campaigns"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>My Campaigns</h2>
    <a href="<%= request.getContextPath() %>/business/campaigns?action=create" class="btn btn-success">Create New Campaign</a>
</div>
<p>Manage your existing campaigns or  class="btn btn-success">Create New Campaign</a>
</div>
<p>Manage your existing campaigns or create new ones.</p>


<% List<Campaign> campaignList = (List<Campaign>) request.getAttribute("campaignList"); %>

<% if (campaignList == null || campaignList.isEmpty()) { %>
    <div class="alert alert-info">You haven't created any campaigns yet. <a href="<%= request.getContextPath() %>/business/campaigns?action=create">Create one now!</a></div>
<% } else { %>
    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Status</th>
                    <th>Budget</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                 <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); %>
                 <% NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(java.util.Locale.US); %>
                <% for (Campaign campaign : campaignList) { %>
                    <tr>
                        <td>
                           <a href="<%= request.getContextPath() %>/business/campaigns?action=edit&id=<%= campaign.getCampaignId() %>">
                                <%= campaign.getTitle() != null ? campaign.getTitle() : "N/A" %>
                           </a>
                           <% if (campaign.getProductImagePath() != null && !campaign.getProductImagePath().isEmpty()) { %>
                                <img src="<%= request.getContextPath() %>/<%= campaign.getProductImagePath().replace(java.io.File.separatorChar, '/') %>" alt="Product" style="max-width: 40px; height: auto; margin-left: 10px; vertical-align: middle;">
                           <% } %>
                        </td>
                        <td>
                            <% String statusClass = "";
                               switch (campaign.getStatus()) {
                                   case "active": statusClass = "text-success"; break;
                                   case "completed": statusClass = "text-muted"; break;
                                   case "archived": statusClass = "text-warning"; break;
                                   default: statusClass = ""; break;
                               }
                           %>
                           <span class="<%= statusClass %>"><strong><%= campaign.getStatus().substring(0, 1).toUpperCase() + campaign.getStatus().substring(1) %></strong></span>
                        </td>
                        <td><%= campaign.getBudget() != null ? currencyFormatter.format(campaign.getBudget()) : "-" %></td>
                        <td><%= campaign.getCreatedAt() != null ? campaign.getCreatedAt().toLocalDateTime().format(formatter) : "N/A" %></td>
                        <td class="actions">
                            <a href="<%= request.getContextPath() %>/business/campaigns?action=edit&id=<%= campaign.getCampaignId() %>" class="btn btn-sm">Edit</a>
                            <a href="<%= request.getContextPath() %>/business/applications?campaignId=<%= campaign.getCampaignId() %>" class="btn btn-sm">View Apps</a>
                            <form action="<%= request.getContextPath() %>/business/campaigns" method="POST" style="display: inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="campaignId" value="<%= campaign.getCampaignId() %>">
                                <button type="submit" class="btn btn-sm btn-danger confirm-delete" data-confirm-message="Are you sure you want to delete this campaign and all its applications?">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
<% } %>

<jsp:include page="/common/footer.jsp" />
