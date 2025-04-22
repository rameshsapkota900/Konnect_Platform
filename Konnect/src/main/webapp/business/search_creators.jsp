<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Profile" %>
<%@ page import="com.konnect.model.User" %>
<%@ page import="com.konnect.model.Campaign" %>
<%@ page import="com.konnect.dao.CampaignDao" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Search Creators"/>
</jsp:include>

<h2>Search for Content Creators</h2>
<p>Find the right creators to collaborate with on your campaigns.</p>

<%-- Search Form --%>
<div class="card mb-3">
    <div class="card-body">
        <form action="<%= request.getContextPath() %>/business/search" method="GET">
            <div class="row">
                <div class="col-md-4 form-group">
                    <label for="keyword">Keyword</label>
                    <input type="text" id="keyword" name="keyword" class="form-control" placeholder="Username, name, bio..." value="<%= request.getAttribute("searchKeyword") != null ? request.getAttribute("searchKeyword") : "" %>">
                </div>
                <div class="col-md-3 form-group">
                    <label for="niche">Niche</label>
                    <input type="text" id="niche" name="niche" class="form-control" placeholder="e.g., Gaming, Beauty" value="<%= request.getAttribute("searchNiche") != null ? request.getAttribute("searchNiche") : "" %>">
                </div>
                <div class="col-md-3 form-group">
                    <label for="minFollowers">Min. Followers</label>
                    <input type="number" id="minFollowers" name="minFollowers" class="form-control" min="0" placeholder="e.g., 5000" value="<%= request.getAttribute("searchMinFollowers") != null ? request.getAttribute("searchMinFollowers") : "" %>">
                </div>
                <div class="col-md-2 form-group d-flex align-items-end">
                    <button type="submit" class="btn w-100">Search</button>
                </div>
            </div>
        </form>
    </div>
</div>

<%-- Search Results --%>
<%
    List<Profile> creatorProfiles = (List<Profile>) request.getAttribute("creatorProfiles");
    User currentBusiness = (User) session.getAttribute("user");
    CampaignDao campDao = new CampaignDao();
    List<Campaign> myActiveCampaigns = null;
    if(currentBusiness != null) {
        // Fetch only ACTIVE campaigns for the invite dropdown
        myActiveCampaigns = campDao.findByBusinessId(currentBusiness.getUserId());
        if(myActiveCampaigns != null) {
             myActiveCampaigns.removeIf(c -> !"active".equals(c.getStatus()));
        }
    }

%>

<% if (creatorProfiles != null) { %>
    <% if (creatorProfiles.isEmpty()) { %>
        <div class="alert alert-info">No creators found matching your criteria. Try broadening your search.</div>
    <% } else { %>
        <div class="list-group">
            <% for (Profile profile : creatorProfiles) { %>
                <div class="list-group-item list-group-item-action flex-column align-items-start mb-2 card">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1"><%= profile.getFullName() != null ? profile.getFullName() : profile.getUsername() %></h5>
                        <small class="text-muted">Followers: <%= profile.getFollowerCount() %></small>
                    </div>
                    <p class="mb-1"><strong>Niche:</strong> <%= profile.getNiche() != null ? profile.getNiche() : "Not specified" %></p>
                    <p class="mb-1"><%= profile.getBio() != null ? profile.getBio() : "" %></p>
                    <div class="mt-2">
                         <% if (profile.getMediaKitPath() != null && !profile.getMediaKitPath().isEmpty()) { %>
                            <a href="<%= request.getContextPath() %>/<%= profile.getMediaKitPath().replace(java.io.File.separatorChar, '/') %>" target="_blank" class="btn btn-sm">View Media Kit</a>
                         <% } %>
                         <a href="<%= request.getContextPath() %>/business/chat?with=<%= profile.getUserId() %>" class="btn btn-sm">Chat</a>

                        <%-- Invite Button/Form --%>
                        <% if (myActiveCampaigns != null && !myActiveCampaigns.isEmpty()) { %>
                            <form action="<%= request.getContextPath() %>/business/invite" method="POST" style="display: inline-block; margin-left: 5px;" class="needs-validation" novalidate>
                                <input type="hidden" name="creatorId" value="<%= profile.getUserId() %>">
                                <select name="campaignId" class="form-control form-control-sm d-inline-block" style="width: auto;" required>
                                    <option value="" disabled selected>Invite to Campaign...</option>
                                    <% for (Campaign c : myActiveCampaigns) { %>
                                        <option value="<%= c.getCampaignId() %>"><%= c.getTitle() %></option>
                                    <% } %>
                                </select>
                                <button type="submit" class="btn btn-sm btn-success">Invite</button>
                                <div class="invalid-feedback">Please select a campaign.</div>
                            </form>
                         <% } else { %>
                              <span class="text-muted ml-2">(Create an active campaign to invite)</span>
                         <% } %>

                        <%-- Include Report Button --%>
                         <%
                             request.setAttribute("reportedUserId", profile.getUserId());
                             request.setAttribute("reportedUsername", profile.getUsername());
                         %>
                         <jsp:include page="/common/report_form.jsp" />
                         <%-- Clear attributes after use --%>
                         <%
                             request.removeAttribute("reportedUserId");
                             request.removeAttribute("reportedUsername");
                         %>
                    </div>
                </div>
            <% } %>
        </div>
    <% } %>
<% } %>

<jsp:include page="/common/footer.jsp" />
