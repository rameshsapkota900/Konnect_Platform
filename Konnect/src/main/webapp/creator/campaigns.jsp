<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.NumberFormat" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Browse Campaigns"/>
</jsp:include>

<h2>Browse Available Campaigns</h2>
<p>Find opportunities from businesses looking for creators like you.</p>

<% List<Campaign> campaignList = (List<Campaign>) request.getAttribute("campaignList"); %>

<% if (campaignList == null || campaignList.isEmpty()) { %>
    <div class="alert alert-info">No available campaigns matching your criteria right now. Check back later!</div>
<% } else { %>
    <div class="campaign-list">
        <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); %>
        <% NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(java.util.Locale.US); %>

        <% for (Campaign campaign : campaignList) { %>
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span><%= campaign.getTitle() %></span>
                    <span class="text-muted small">Posted by: <%= campaign.getBusinessUsername() %></span>
                </div>
                <div class="card-body">
                    <% if (campaign.getProductImagePath() != null && !campaign.getProductImagePath().isEmpty()) { %>
                        <img src="<%= request.getContextPath() %>/<%= campaign.getProductImagePath().replace(java.io.File.separatorChar, '/') %>" alt="Product Image for <%= campaign.getTitle() %>" class="card-image-small mb-3">
                    <% } %>

                    <p><%= campaign.getDescription() %></p>
                    <dl>
                        <dt>Requirements:</dt>
                        <dd><%= campaign.getRequirements() != null ? campaign.getRequirements() : "See description." %></dd>
                        <dt>Budget:</dt>
                        <dd><%= campaign.getBudget() != null ? currencyFormatter.format(campaign.getBudget()) : "Not specified" %></dd>
                        <dt>Posted On:</dt>
                        <dd><%= campaign.getCreatedAt() != null ? campaign.getCreatedAt().toLocalDateTime().format(formatter) : "N/A" %></dd>
                    </dl>
                </div>
                <div class="card-footer">
                    <form action="<%= request.getContextPath() %>/creator/campaigns" method="POST" style="display: inline;">
                        <input type="hidden" name="action" value="apply">
                        <input type="hidden" name="campaignId" value="<%= campaign.getCampaignId() %>">
                        <button type="submit" class="btn btn-success">Apply Now</button>
                    </form>
                    <a href="#" class="btn">View Details</a>
                    <%
                        request.setAttribute("reportedUserId", campaign.getBusinessUserId());
                        request.setAttribute("reportedUsername", campaign.getBusinessUsername());
                    %>
                    <jsp:include page="/common/report_form.jsp" />
                    <%
                        request.removeAttribute("reportedUserId");
                        request.removeAttribute("reportedUsername");
                    %>
                </div>
            </div>
        <% } %>
    </div>
<% } %>

<jsp:include page="/common/footer.jsp" />
