<%@ page import="com.konnect.model.User" %>
<%-- This JSP fragment provides a form to report a user. --%>
<%-- It expects 'reportedUser' (User object) to be set in the request scope --%>
<%-- OR 'reportedUserId' (int) and 'reportedUsername' (String) --%>
<%
    User userToReport = (User) request.getAttribute("reportedUser");
    Integer reportedUserId = (Integer) request.getAttribute("reportedUserId");
    String reportedUsername = (String) request.getAttribute("reportedUsername");

    if (userToReport != null) {
        reportedUserId = userToReport.getUserId();
        reportedUsername = userToReport.getUsername();
    }

    // Ensure we have the necessary info
    boolean canDisplayForm = (reportedUserId != null && reportedUsername != null);

    User currentUser = (User) session.getAttribute("user");
    boolean canReport = currentUser != null && reportedUserId != null && currentUser.getUserId() != reportedUserId;

%>

<% if (canDisplayForm && canReport) { %>
<div class="card mt-3">
    <div class="card-header">
        Report User: <%= reportedUsername %>
    </div>
    <div class="card-body">
        <form action="<%= request.getContextPath() %>/common/reportUser" method="POST" class="needs-validation" novalidate>
            <input type="hidden" name="reportedUserId" value="<%= reportedUserId %>">

            <div class="form-group">
                <label for="reportReason">Reason for Reporting *</label>
                <select id="reportReason" name="reason" class="form-control" required>
                    <option value="" disabled selected>-- Select a Reason --</option>
                    <option value="spam">Spam or Misleading Content</option>
                    <option value="harassment">Harassment or Bullying</option>
                    <option value="scam">Scam or Fraudulent Activity</option>
                    <option value="inappropriate">Inappropriate Profile/Content</option>
                    <option value="unprofessional">Unprofessional Conduct</option>
                     <option value="other">Other (Please specify below)</option>
                </select>
                <div class="invalid-feedback">Please select a reason.</div>
            </div>

            <div class="form-group">
                <label for="reportDetails">Details (Optional)</label>
                <textarea id="reportDetails" name="details" class="form-control" rows="3" placeholder="Provide any additional details or context here."></textarea>
            </div>

            <button type="submit" class="btn btn-danger">Submit Report</button>
            <small class="text-muted d-block mt-2">Reports are reviewed by administrators. False reporting may lead to penalties.</small>
        </form>
    </div>
</div>
<% } else if (canDisplayForm && !canReport) { %>
    <%-- Maybe show message "You cannot report this user" or hide the section entirely --%>
<% } %>
