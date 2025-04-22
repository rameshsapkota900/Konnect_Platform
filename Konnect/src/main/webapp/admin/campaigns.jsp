<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.NumberFormat" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="View All Campaigns"/>
</jsp:include>

<h2>All Campaigns</h2>
<p>Overview of all campaigns created on the platform.</p>

<% List<Campaign> campaignList = (List<Campaign>) request.getAttribute("campaignList"); %>

<% if (campaignList == null || campaignList.isEmpty()) { %>
    <div class="alert alert-info">No campaigns found.</div>
<% } else { %>
    <div class="table-responsive">
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Business Owner</th>
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
                        <td><%= campaign.getCampaignId() %></td>
                        <td><%= campaign.getTitle() != null ? campaign.getTitle() : "N/A" %></td>
                        <td><%= campaign.getBusinessUsername() != null ? campaign.getBusinessUsername() : "N/A" %></td>
                        <td>
                             <% String statusClass = "";
                                switch (campaign.getStatus()) {
                                    case "active": statusClass = "text-success"; break;
                                    case "completed": statusClass = "text-muted"; break;
                                    case "archived": statusClass = "text-warning"; break;
                                    default: statusClass = ""; break;
                                }
                            %>
                            <span class="<%= statusClass %>"><%= campaign.getStatus() %></span>
                        </td>
                         <td><%= campaign.getBudget() != null ? currencyFormatter.format(campaign.getBudget()) : "N/A" %></td>
                        <td><%= campaign.getCreatedAt() != null ? campaign.getCreatedAt().toLocalDateTime().format(formatter) : "N/A" %></td>
                        <td class="actions">
                            <a href="#" class="btn btn-sm">View Details</a>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
<% } %>

<jsp:include page="/common/footer.jsp" />
