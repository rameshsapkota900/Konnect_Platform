<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.konnect.model.Profile" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Edit Profile"/>
</jsp:include>

<h2>Edit Your Profile</h2>
<p>Keep your information up-to-date for businesses to find you.</p>

<% Profile profile = (Profile) request.getAttribute("profile");
   // Provide default empty values if profile is null (shouldn't happen ideally)
   String fullName = (profile != null && profile.getFullName() != null) ? profile.getFullName() : "";
   String bio = (profile != null && profile.getBio() != null) ? profile.getBio() : "";
   String socialLinks = (profile != null && profile.getSocialMediaLinks() != null) ? profile.getSocialMediaLinks() : "";
   int followers = (profile != null) ? profile.getFollowerCount() : 0;
   String niche = (profile != null && profile.getNiche() != null) ? profile.getNiche() : "";
   String pricing = (profile != null && profile.getPricingInfo() != null) ? profile.getPricingInfo() : "";
   String mediaKitPath = (profile != null && profile.getMediaKitPath() != null) ? profile.getMediaKitPath() : null;
%>

<div class="card">
    <div class="card-body">
        <form action="<%= request.getContextPath() %>/creator/profile" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" class="form-control" value="<%= fullName %>" required>
                <div class="invalid-feedback">Please enter your full name.</div>
            </div>
            <div class="form-group">
                <label for="bio">Bio / About Me</label>
                <textarea id="bio" name="bio" class="form-control" rows="4" placeholder="Tell businesses about yourself, your content style, and audience."><%= bio %></textarea>
            </div>
            <div class="form-group">
                <label for="niche">Niche / Content Category</label>
                <input type="text" id="niche" name="niche" class="form-control" value="<%= niche %>" placeholder="e.g., Beauty, Gaming, Tech, Travel, Food">
            </div>
            <div class="form-group">
                <label for="followerCount">Primary Platform Follower Count</label>
                <input type="number" id="followerCount" name="followerCount" class="form-control" value="<%= followers %>" min="0" placeholder="e.g., 10000">
            </div>
            <div class="form-group">
                <label for="socialMediaLinks">Social Media Links</label>
                <textarea id="socialMediaLinks" name="socialMediaLinks" class="form-control" rows="3" placeholder="Enter links one per line, e.g.&#10;https://youtube.com/yourchannel&#10;https://instagram.com/yourprofile"><%= socialLinks %></textarea>
                <small class="form-text text-muted">Provide links to your main platforms.</small>
            </div>
            <div class="form-group">
                <label for="pricingInfo">Pricing / Collaboration Rates (Optional)</label>
                <textarea id="pricingInfo" name="pricingInfo" class="form-control" rows="3" placeholder="Describe your typical rates or packages, e.g., '$XXX per Instagram post', 'Starting from $YYYY for video integration'"><%= pricing %></textarea>
            </div>
            <div class="form-group">
                <label for="mediaKit">Upload Media Kit (PDF or Image)</label>
                <input type="file" id="mediaKit" name="mediaKit" class="form-control" accept=".pdf,.jpg,.jpeg,.png">
                <% if (mediaKitPath != null && !mediaKitPath.isEmpty()) { %>
                    <small class="form-text text-muted">
                        Current file: <a href="<%= request.getContextPath() %>/<%= mediaKitPath.replace(java.io.File.separatorChar, '/') %>" target="_blank">View Current Kit</a>. Uploading a new file will replace the existing one.
                    </small>
                <% } else { %>
                    <small class="form-text text-muted">Upload a file (max 10MB).</small>
                <% } %>
            </div>

            <button type="submit" class="btn">Save Profile</button>
            <a href="<%= request.getContextPath() %>/creator/dashboard" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
