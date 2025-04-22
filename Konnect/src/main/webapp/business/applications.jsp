<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.konnect.model.Application" %>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Campaign Applications"/>
</jsp:include>

<h2>Campaign Applications</h2>
<p>Review applications and invitations for your campaigns.</p>

<%
    Map<String, List<Application>> applicationsByCampaign = (Map<String, List<Application>>) request.getAttribute("applicationsByCampaign");
    List<Campaign> myCampaigns = (List<Campaign>) request.getAttribute("myCampaigns"); // For filtering maybe
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String filterCampaignId = request.getParameter("campaignId"); // Check if filtering by specific campaign
%>

<%-- Optional: Add filtering dropdown --%>
<% if (myCampaigns != null && !myCampaigns.isEmpty()) { %>
<div class="mb-3">
    <label for="campaignFilter">Filter by Campaign:</label>
    <select id="campaignFilter" class="form-control" style="max-width: 300px; display: inline-block;"
            onchange="if (this.value) window.location.href='<%= request.getContextPath() %>/business/applications?campaignId=' + this.value; else window.location.href='<%= request.getContextPath() %>/business/applications';">
        <option value="">-- Show All --</option>
        <% for (Campaign c : myCampaigns) { %>
            <option value="<%= c.getCampaignId() %>" <%= String.valueOf(c.getCampaignId()).equals(filterCampaignId) ? "selected" : "" %>>
                <%= c.getTitle() %>
            </option>
        <% } %>
    </select>
</div>
<% } %>

<% if (applicationsByCampaign == null || applicationsByCampaign.isEmpty()) { %>
    <div class="alert alert-info">
       <% if (filterCampaignId != null) { %>
            No applications found for the selected campaign.
       <% } else { %>
            No applications found for any of your campaigns yet.
       <% } %>
    </div>
<% } else { %>
    <%-- Iterate through campaigns that have applications --%>
    <% for (Map.Entry<String, List<Application>> entry : applicationsByCampaign.entrySet()) { %>
        <% String campaignTitle = entry.getKey(); %>
        <% List<Application> appsForThisCampaign = entry.getValue(); %>

        <%-- If filtering, skip campaigns that don't match --%>
        <% if (filterCampaignId != null && !appsForThisCampaign.isEmpty() && !filterCampaignId.equals(String.valueOf(appsForThisCampaign.get(0).getCampaignId()))) {
            continue;
           }
        %>

        <div class="card mb-3">
            <div class="card-header">
                Applications for: <strong><%= campaignTitle %></strong>
                <%-- Link back to edit campaign --%>
                <% if (!appsForThisCampaign.isEmpty()) { %>
                    <a href="<%= request.getContextPath() %>/business/campaigns?action=edit&id=<%= appsForThisCampaign.get(0).getCampaignId() %>" class="btn btn-sm float-right">Edit Campaign</a>
                <% } %>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Creator</th>
                                <th>Status</th>
                                <th>Date</th>
                                <th>Message/Note</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Application app : appsForThisCampaign) { %>
                                <tr>
                                    <td>
                                        <%= app.getCreatorUsername() != null ? app.getCreatorUsername() : "N/A" %>
                                    </td>
                                    <td>
                                        <% String statusClass = ""; String statusText = app.getStatus();
                                           switch (statusText) {
                                               case "pending": statusClass = "text-warning"; statusText="Pending"; break;
                                               case "accepted": statusClass = "text-success"; statusText="Accepted"; break;
                                               case "rejected": statusClass = "text-danger"; statusText="Rejected"; break;
                                               case "invited": statusClass = "text-info"; statusText="Invited"; break;
                                               default: statusClass = "text-muted"; break;
                                           }
                                       %>
                                       <span class="<%= statusClass %>"><strong><%= statusText %></strong></span>
                                    </td>
                                    <td><%= app.getAppliedAt() != null ? app.getAppliedAt().toLocalDateTime().format(formatter) : "N/A" %></td>
                                    <td><%= app.getMessage() != null && !app.getMessage().isEmpty() ? app.getMessage() : "-" %></td>
                                    <td class="actions">
                                        <% if ("pending".equals(app.getStatus())) { %>
                                            <form action="<%= request.getContextPath() %>/business/applications" method="POST" style="display: inline;">
                                                <input type="hidden" name="applicationId" value="<%= app.getApplicationId() %>">
                                                <input type="hidden" name="action" value="acceptApp">
                                                <button type="submit" class="btn btn-xs btn-success">Accept</button>
                                            </form>
                                            <form action="<%= request.getContextPath() %>/business/applications" method="POST" style="display: inline;">
                                                <input type="hidden" name="applicationId" value="<%= app.getApplicationId() %>">
                                                <input type="hidden" name="action" value="rejectApp">
                                                <button type="submit" class="btn btn-xs btn-danger confirm-delete" data-confirm-message="Are you sure you want to reject this application?">Reject</button>
                                            </form>
                                        <% } else if ("accepted".equals(app.getStatus())) { %>
                                            <a href="<%= request.getContextPath() %>/business/chat?with=<%= app.getCreatorUserId() %>" class="btn btn-xs">Chat</a>
                                        <% } else if ("invited".equals(app.getStatus())) { %>
                                            <span class="text-muted">Waiting for response</span>
                                        <% } else { %>
                                            <span class="text-muted">-</span>
                                        <% } %>
                                        <%
                                            request.setAttribute("reportedUserId", app.getCreatorUserId());
                                            request.setAttribute("reportedUsername", app.getCreatorUsername());
                                        %>
                                        <jsp:include page="/common/report_form.jsp" />
                                        <%
                                            request.removeAttribute("reportedUserId");
                                            request.removeAttribute("reportedUsername");
                                        %>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    <% } %>
<% } %>

<jsp:include page="/common/footer.jsp" />
