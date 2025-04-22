<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="java.math.BigDecimal" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Campaign Form"/>
</jsp:include>

<%
    Campaign campaign = (Campaign) request.getAttribute("campaign");
    boolean isEditMode = (campaign != null && campaign.getCampaignId() > 0);
    String formAction = isEditMode ? "update" : "create";
    String pageTitle = isEditMode ? "Edit Campaign" : "Create New Campaign";

    // Populate form fields or defaults
    String title = (campaign != null && campaign.getTitle() != null) ? campaign.getTitle() : "";
    String description = (campaign != null && campaign.getDescription() != null) ? campaign.getDescription() : "";
    String requirements = (campaign != null && campaign.getRequirements() != null) ? campaign.getRequirements() : "";
    String budget = (campaign != null && campaign.getBudget() != null) ? campaign.getBudget().toPlainString() : "";
    String status = (campaign != null && campaign.getStatus() != null) ? campaign.getStatus() : "active"; // Default to active
    String imagePath = (campaign != null && campaign.getProductImagePath() != null) ? campaign.getProductImagePath() : null;
%>

<h2><%= pageTitle %></h2>
<p><%= isEditMode ? "Update the details of your campaign." : "Fill out the form to create a new campaign." %></p>

<div class="card">
    <div class="card-body">
        <form action="<%= request.getContextPath() %>/business/campaigns" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
            <%-- Hidden fields for action and ID (in edit mode) --%>
            <input type="hidden" name="action" value="<%= formAction %>">
            <% if (isEditMode) { %>
                <input type="hidden" name="campaignId" value="<%= campaign.getCampaignId() %>">
            <% } %>

            <div class="form-group">
                <label for="title">Campaign Title *</label>
                <input type="text" id="title" name="title" class="form-control" value="<%= title %>" required maxlength="150">
                <div class="invalid-feedback">Please enter a campaign title.</div>
            </div>

            <div class="form-group">
                <label for="description">Description *</label>
                <textarea id="description" name="description" class="form-control" rows="5" required placeholder="Describe the campaign goals, product/service, and what you're looking for in a creator."><%= description %></textarea>
                <div class="invalid-feedback">Please enter a description.</div>
            </div>

            <div class="form-group">
                <label for="requirements">Creator Requirements</label>
                <textarea id="requirements" name="requirements" class="form-control" rows="3" placeholder="e.g., Minimum follower count, specific platform (Instagram, YouTube), content style, deadlines."><%= requirements %></textarea>
            </div>

            <div class="form-group">
                <label for="budget">Budget (USD) (Optional)</label>
                <input type="number" id="budget" name="budget" class="form-control" value="<%= budget %>" step="0.01" min="0" placeholder="e.g., 500.00">
                <small class="form-text text-muted">Enter the total budget or rate you are offering.</small>
            </div>

            <div class="form-group">
                <label for="productImage">Product Image (Optional)</label>
                <input type="file" id="productImage" name="productImage" class="form-control" accept=".jpg,.jpeg,.png,.gif">
                <% if (imagePath != null && !imagePath.isEmpty()) { %>
                    <small class="form-text text-muted">
                        Current image: <img src="<%= request.getContextPath() %>/<%= imagePath.replace(java.io.File.separatorChar, '/') %>" alt="Current Product Image" style="max-height: 50px; vertical-align: middle; margin-left: 10px;">
                        <br>Uploading a new file will replace the existing one.
                    </small>
                <% } else { %>
                    <small class="form-text text-muted">Upload an image (max 10MB).</small>
                <% } %>
            </div>

            <div class="form-group">
                <label for="status">Campaign Status</label>
                <select id="status" name="status" class="form-control" required>
                    <option value="active" <%= "active".equals(status) ? "selected" : "" %>>Active (Visible to Creators)</option>
                    <option value="completed" <%= "completed".equals(status) ? "selected" : "" %>>Completed</option>
                    <option value="archived" <%= "archived".equals(status) ? "selected" : "" %>>Archived (Hidden)</option>
                </select>
            </div>

            <button type="submit" class="btn"><%= isEditMode ? "Update Campaign" : "Create Campaign" %></button>
            <a href="<%= request.getContextPath() %>/business/campaigns" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
