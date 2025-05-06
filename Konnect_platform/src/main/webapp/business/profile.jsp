<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.BusinessProfile" %>
<%
    // Check if user is logged in and has business role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("business")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get profile from request
    BusinessProfile profile = (BusinessProfile) request.getAttribute("profile");
    if (profile == null) {
        // If not set, redirect to profile servlet to get the data
        response.sendRedirect(request.getContextPath() + "/profile");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Business Profile" />
</jsp:include>

<jsp:include page="/includes/nav-business.jsp">
    <jsp:param name="active" value="profile" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Business Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile" class="active"><i class="fas fa-building"></i> Company Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile/creators"><i class="fas fa-users"></i> Find Creators</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-building"></i> Business Profile</h2>
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

                    <div class="card compact">
                        <form action="<%= request.getContextPath() %>/profile/update" method="post" class="form">
                            <div class="form-section">
                                <h3 class="section-title">Business Information</h3>

                                <div class="form-group">
                                    <label for="businessName">Business Name <span class="required">*</span></label>
                                    <input type="text" id="businessName" name="businessName" class="form-control" value="<%= profile.getBusinessName() != null ? profile.getBusinessName() : "" %>" required>
                                </div>

                                <div class="form-group">
                                    <label for="businessDescription">Business Description <span class="required">*</span></label>
                                    <textarea id="businessDescription" name="businessDescription" class="form-control" rows="3" required><%= profile.getBusinessDescription() != null ? profile.getBusinessDescription() : "" %></textarea>
                                    <div class="form-text">Describe your business, products/services, and target audience.</div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="website">Website</label>
                                        <input type="url" id="website" name="website" class="form-control" value="<%= profile.getWebsite() != null ? profile.getWebsite() : "" %>" placeholder="https://www.yourbusiness.com">
                                    </div>

                                    <div class="form-group col-md-6">
                                        <label for="industry">Industry <span class="required">*</span></label>
                                        <select id="industry" name="industry" class="form-control" required>
                                            <option value="" disabled <%= profile.getIndustry() == null ? "selected" : "" %>>Select Industry</option>
                                            <option value="Fashion & Apparel" <%= "Fashion & Apparel".equals(profile.getIndustry()) ? "selected" : "" %>>Fashion & Apparel</option>
                                            <option value="Beauty & Cosmetics" <%= "Beauty & Cosmetics".equals(profile.getIndustry()) ? "selected" : "" %>>Beauty & Cosmetics</option>
                                            <option value="Food & Beverage" <%= "Food & Beverage".equals(profile.getIndustry()) ? "selected" : "" %>>Food & Beverage</option>
                                            <option value="Health & Wellness" <%= "Health & Wellness".equals(profile.getIndustry()) ? "selected" : "" %>>Health & Wellness</option>
                                            <option value="Technology" <%= "Technology".equals(profile.getIndustry()) ? "selected" : "" %>>Technology</option>
                                            <option value="Travel & Hospitality" <%= "Travel & Hospitality".equals(profile.getIndustry()) ? "selected" : "" %>>Travel & Hospitality</option>
                                            <option value="Home & Lifestyle" <%= "Home & Lifestyle".equals(profile.getIndustry()) ? "selected" : "" %>>Home & Lifestyle</option>
                                            <option value="Entertainment" <%= "Entertainment".equals(profile.getIndustry()) ? "selected" : "" %>>Entertainment</option>
                                            <option value="Sports & Fitness" <%= "Sports & Fitness".equals(profile.getIndustry()) ? "selected" : "" %>>Sports & Fitness</option>
                                            <option value="Education" <%= "Education".equals(profile.getIndustry()) ? "selected" : "" %>>Education</option>
                                            <option value="Finance" <%= "Finance".equals(profile.getIndustry()) ? "selected" : "" %>>Finance</option>
                                            <option value="Other" <%= "Other".equals(profile.getIndustry()) ? "selected" : "" %>>Other</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="form-actions">
                                <button type="button" id="previewBtnInline" class="btn btn-outline btn-fixed-height"><i class="fas fa-eye"></i> Preview Profile</button>
                                <button type="submit" class="btn btn-primary btn-fixed-height"><i class="fas fa-save"></i> Save Profile</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Profile Preview Modal -->
    <div id="profilePreviewModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <h2>Profile Preview</h2>
            <div class="profile-preview">
                <div class="profile-header">
                    <h3 id="previewName"></h3>
                    <span class="industry-badge" id="previewIndustry"></span>
                </div>
                <div class="profile-body">
                    <div class="profile-section">
                        <h4>About</h4>
                        <p id="previewDescription"></p>
                    </div>
                    <div class="profile-section">
                        <h4>Website</h4>
                        <a id="previewWebsite" href="#" target="_blank"></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Profile Preview Modal
        const modal = document.getElementById('profilePreviewModal');
        const btnInline = document.getElementById('previewBtnInline');
        const span = document.getElementsByClassName('close')[0];

        btnInline.onclick = function() {
            updatePreview();
            modal.style.display = 'block';

            // Add animation class to the modal content
            const modalContent = document.querySelector('.modal-content');
            modalContent.style.animation = 'none';
            setTimeout(() => {
                modalContent.style.animation = 'modalFadeIn 0.3s ease';
            }, 10);
        }

        span.onclick = function() {
            modal.style.display = 'none';
        }

        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }

        function updatePreview() {
            // Update preview with form values
            document.getElementById('previewName').textContent = document.getElementById('businessName').value || 'Your Business Name';
            document.getElementById('previewDescription').textContent = document.getElementById('businessDescription').value || 'Your business description will appear here...';

            // Industry
            const industrySelect = document.getElementById('industry');
            const selectedIndustry = industrySelect.options[industrySelect.selectedIndex].text;
            document.getElementById('previewIndustry').textContent = selectedIndustry !== 'Select Industry' ? selectedIndustry : '';

            // Website
            const website = document.getElementById('website').value;
            const websiteLink = document.getElementById('previewWebsite');

            if (website) {
                websiteLink.href = website;
                websiteLink.textContent = website;
                websiteLink.style.display = 'inline';
            } else {
                websiteLink.style.display = 'none';
            }
        }

        // Add keyboard shortcut (Escape key) to close modal
        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape' && modal.style.display === 'block') {
                modal.style.display = 'none';
            }
        });
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
