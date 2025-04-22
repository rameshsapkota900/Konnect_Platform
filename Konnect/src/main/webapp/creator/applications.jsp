<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Application" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="My Applications & Invitations"/>
</jsp:include>

<h2>My Applications & Invitations</h2>

<% List<Application> applicationList = (List<Application>) request.getAttribute("applicationList"); %>
<% List<Application> invitationList = (List<Application>) request.getAttribute("invitationList"); %>
<% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); %>

<%-- Section for Invitations --%>
<div class="card mb-3" id="invitations">
    <div class="card-header">Pending Invitations</div>
    <div class="card-body">
        <% if (invitationList == null || invitationList.isEmpty()) { %>
            <div class="alert alert-info">You have no pending invitations.</div>
        <% } else { %>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Campaign Title</th>
                            <th>Business</th>
                            <th>Invite Message</th>
                            <th>Received At</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Application invite : invitationList) { %>
                            <tr>
                                <td><%= invite.getCampaignTitle() != null ? invite.getCampaignTitle() : "N/A" %></td>
                                <td><%= invite.getBusinessUsername() != null ? invite.getBusinessUsername() : "N/A" %></td>
                                <td><%= invite.getMessage() != null ? invite.getMessage() : "-" %></td>
                                <td><%= invite.getAppliedAt() != null ? invite.getAppliedAt().toLocalDateTime().format(formatter) : "N/A" %></td>
                                <td class="actions">
                                    <form action="<%= request.getContextPath() %>/creator/applications" method="POST" style="display: inline;">
                                        <input type="hidden" name="applicationId" value="<%= invite.getApplicationId() %>">
                                        <input type="hidden" name="action" value="acceptInvite">
                                        <button type="submit" class="btn btn-sm btn-success">Accept</button>
                                    </form>
                                    <form action="<%= request.getContextPath() %>/creator/applications" method="POST" style="display: inline;">
                                        <input type="hidden" name="applicationId" value="<%= invite.getApplicationId() %>">
                                        <input type="hidden" name="action" value="rejectInvite">
                                        <button type="submit" class="btn btn-sm btn-danger confirm-delete" data-confirm-message="Are you sure you want to reject this invitation?">Reject</button>
                                    </form>
                                    <a href="#" class="btn btn-sm">View Campaign</a>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </div>
</div>

<%-- Section for Applications Sent --%>
<div class="card">
    <div class="card-header">My Sent Applications</div>
    <div class="card-body">
        <% if (applicationList == null || applicationList.isEmpty()) { %>
            <div class="alert alert-info">You haven't applied to any campaigns yet. <a href="<%= request.getContextPath() %>/creator/campaigns">Browse campaigns now!</a></div>
        <% } else { %>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Campaign Title</th>
                            <th>Business</th>
                            <th>Status</th>
                            <th>Applied At</th>
                            <th>Your Message</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Application app : applicationList) { %>
                            <%-- Skip invitations shown above --%>
                            <% if ("invited".equals(app.getStatus())) continue; %>
                            <tr>
                                <td><%= app.getCampaignTitle() != null ? app.getCampaignTitle() : "N/A" %></td>
                                <td><%= app.getBusinessUsername() != null ? app.getBusinessUsername() : "N/A" %></td>
                                <td>
                                    <% String statusClass = ""; String statusText = app.getStatus();
                                        switch (statusText) {
                                            case "pending": statusClass = "text-warning"; statusText="Pending"; break;
                                            case "accepted": statusClass = "text-success"; statusText="Accepted"; break;
                                            case "rejected": statusClass = "text-danger"; statusText="Rejected"; break;
                                            default: statusClass = "text-muted"; break;
                                        }
                                    %>
                                    <span class="<%= statusClass %>"><strong><%= statusText %></strong></span>
                                </td>
                                <td><%= app.getAppliedAt() != null ? app.getAppliedAt().toLocalDateTime().format(formatter) : "N/A" %></td>
                                <td><%= app.getMessage() != null ? app.getMessage() : "-" %></td>
                                <td class="actions">
                                    <a href="#" class="btn btn-sm">View Campaign</a>
                                    <% if ("accepted".equals(app.getStatus())) { %>
                                        <a href="<%= request.getContextPath() %>/creator/chat?with=<%= app.getBusinessUserId() %>" class="btn btn-sm">Chat with Business</a>
                                    <% } %>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
