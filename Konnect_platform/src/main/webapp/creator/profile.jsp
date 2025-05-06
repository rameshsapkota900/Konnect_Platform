<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.CreatorProfile" %>
<%
    // Check if user is logged in and has creator role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("creator")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Get profile from request
    CreatorProfile profile = (CreatorProfile) request.getAttribute("profile");
    if (profile == null) {
        // If not set, redirect to profile servlet to get the data
        response.sendRedirect(request.getContextPath() + "/profile");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="My Profile" />
</jsp:include>

<jsp:include page="/includes/nav-creator.jsp">
    <jsp:param name="active" value="profile" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Creator Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile" class="active"><i class="fas fa-user-circle"></i> Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-user-circle"></i> My Profile</h2>
                        <div class="dashboard-actions">
                            <button id="previewBtn" class="btn btn-outline"><i class="fas fa-eye"></i> Preview Profile</button>
                        </div>
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

                    <div class="card">
                        <form action="<%= request.getContextPath() %>/profile/update" method="post" class="form">
                            <div class="form-section">
                                <h3 class="section-title">Basic Information</h3>
                                
                                <div class="form-group">
                                    <label for="fullName">Full Name <span class="required">*</span></label>
                                    <input type="text" id="fullName" name="fullName" class="form-control" value="<%= profile.getFullName() != null ? profile.getFullName() : "" %>" required>
                                </div>
                                
                                <div class="form-group">
                                    <label for="bio">Bio <span class="required">*</span></label>
                                    <textarea id="bio" name="bio" class="form-control" rows="4" required><%= profile.getBio() != null ? profile.getBio() : "" %></textarea>
                                    <div class="form-text">Tell businesses about yourself, your content style, and your audience.</div>
                                </div>
                            </div>

                            <div class="form-section">
                                <h3 class="section-title">Social Media Platforms</h3>
                                
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="instagramLink">Instagram Profile</label>
                                        <input type="url" id="instagramLink" name="instagramLink" class="form-control" value="<%= profile.getInstagramLink() != null ? profile.getInstagramLink() : "" %>" placeholder="https://instagram.com/yourusername">
                                    </div>
                                    
                                    <div class="form-group col-md-6">
                                        <label for="instagramFollowers">Instagram Followers</label>
                                        <input type="number" id="instagramFollowers" name="instagramFollowers" class="form-control" value="<%= profile.getInstagramFollowers() %>" min="0">
                                    </div>
                                </div>
                                
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="youtubeLink">YouTube Channel</label>
                                        <input type="url" id="youtubeLink" name="youtubeLink" class="form-control" value="<%= profile.getYoutubeLink() != null ? profile.getYoutubeLink() : "" %>" placeholder="https://youtube.com/c/yourchannel">
                                    </div>
                                    
                                    <div class="form-group col-md-6">
                                        <label for="youtubeFollowers">YouTube Subscribers</label>
                                        <input type="number" id="youtubeFollowers" name="youtubeFollowers" class="form-control" value="<%= profile.getYoutubeFollowers() %>" min="0">
                                    </div>
                                </div>
                                
                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="tiktokLink">TikTok Profile</label>
                                        <input type="url" id="tiktokLink" name="tiktokLink" class="form-control" value="<%= profile.getTiktokLink() != null ? profile.getTiktokLink() : "" %>" placeholder="https://tiktok.com/@yourusername">
                                    </div>
                                    
                                    <div class="form-group col-md-6">
                                        <label for="tiktokFollowers">TikTok Followers</label>
                                        <input type="number" id="tiktokFollowers" name="tiktokFollowers" class="form-control" value="<%= profile.getTiktokFollowers() %>" min="0">
                                    </div>
                                </div>
                            </div>

                            <div class="form-section">
                                <h3 class="section-title">Pricing</h3>
                                
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label for="postPrice">Post Price (USD)</label>
                                        <input type="number" id="postPrice" name="postPrice" class="form-control" value="<%= profile.getPostPrice() %>" min="0" step="0.01">
                                        <div class="form-text">Price for a standard post</div>
                                    </div>
                                    
                                    <div class="form-group col-md-4">
                                        <label for="storyPrice">Story Price (USD)</label>
                                        <input type="number" id="storyPrice" name="storyPrice" class="form-control" value="<%= profile.getStoryPrice() %>" min="0" step="0.01">
                                        <div class="form-text">Price for a story</div>
                                    </div>
                                    
                                    <div class="form-group col-md-4">
                                        <label for="videoPrice">Video Price (USD)</label>
                                        <input type="number" id="videoPrice" name="videoPrice" class="form-control" value="<%= profile.getVideoPrice() %>" min="0" step="0.01">
                                        <div class="form-text">Price for a video</div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-section">
                                <h3 class="section-title">Content Categories</h3>
                                
                                <div class="form-group">
                                    <label for="interests">Interests/Categories <span class="required">*</span></label>
                                    <input type="text" id="interests" name="interests" class="form-control" value="<%= profile.getInterests() != null ? profile.getInterests() : "" %>" required>
                                    <div class="form-text">Enter your content categories separated by commas (e.g., Fashion, Beauty, Fitness)</div>
                                </div>
                            </div>

                            <div class="form-actions">
                                <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Save Profile</button>
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
                    <div class="profile-stats">
                        <span class="stat"><i class="fab fa-instagram"></i> <span id="previewInstagramFollowers">0</span></span>
                        <span class="stat"><i class="fab fa-youtube"></i> <span id="previewYoutubeFollowers">0</span></span>
                        <span class="stat"><i class="fab fa-tiktok"></i> <span id="previewTiktokFollowers">0</span></span>
                    </div>
                </div>
                <div class="profile-body">
                    <div class="profile-section">
                        <h4>About</h4>
                        <p id="previewBio"></p>
                    </div>
                    <div class="profile-section">
                        <h4>Social Media</h4>
                        <div class="social-links">
                            <a id="previewInstagram" href="#" target="_blank"><i class="fab fa-instagram"></i> Instagram</a>
                            <a id="previewYoutube" href="#" target="_blank"><i class="fab fa-youtube"></i> YouTube</a>
                            <a id="previewTiktok" href="#" target="_blank"><i class="fab fa-tiktok"></i> TikTok</a>
                        </div>
                    </div>
                    <div class="profile-section">
                        <h4>Content Categories</h4>
                        <div id="previewInterests" class="tag-list"></div>
                    </div>
                    <div class="profile-section">
                        <h4>Pricing</h4>
                        <div class="pricing-table">
                            <div class="price-item">
                                <span class="price-label">Post</span>
                                <span class="price-value">$<span id="previewPostPrice">0</span></span>
                            </div>
                            <div class="price-item">
                                <span class="price-label">Story</span>
                                <span class="price-value">$<span id="previewStoryPrice">0</span></span>
                            </div>
                            <div class="price-item">
                                <span class="price-label">Video</span>
                                <span class="price-value">$<span id="previewVideoPrice">0</span></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Profile Preview Modal
        const modal = document.getElementById('profilePreviewModal');
        const btn = document.getElementById('previewBtn');
        const span = document.getElementsByClassName('close')[0];
        
        btn.onclick = function() {
            updatePreview();
            modal.style.display = 'block';
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
            document.getElementById('previewName').textContent = document.getElementById('fullName').value || 'Your Name';
            document.getElementById('previewBio').textContent = document.getElementById('bio').value || 'Your bio will appear here...';
            
            // Social media followers
            document.getElementById('previewInstagramFollowers').textContent = document.getElementById('instagramFollowers').value || '0';
            document.getElementById('previewYoutubeFollowers').textContent = document.getElementById('youtubeFollowers').value || '0';
            document.getElementById('previewTiktokFollowers').textContent = document.getElementById('tiktokFollowers').value || '0';
            
            // Social media links
            const instagramLink = document.getElementById('instagramLink').value;
            const youtubeLink = document.getElementById('youtubeLink').value;
            const tiktokLink = document.getElementById('tiktokLink').value;
            
            document.getElementById('previewInstagram').href = instagramLink || '#';
            document.getElementById('previewYoutube').href = youtubeLink || '#';
            document.getElementById('previewTiktok').href = tiktokLink || '#';
            
            // Pricing
            document.getElementById('previewPostPrice').textContent = document.getElementById('postPrice').value || '0';
            document.getElementById('previewStoryPrice').textContent = document.getElementById('storyPrice').value || '0';
            document.getElementById('previewVideoPrice').textContent = document.getElementById('videoPrice').value || '0';
            
            // Interests/Categories
            const interestsContainer = document.getElementById('previewInterests');
            interestsContainer.innerHTML = '';
            
            const interests = document.getElementById('interests').value.split(',');
            interests.forEach(interest => {
                if (interest.trim()) {
                    const tag = document.createElement('span');
                    tag.className = 'tag';
                    tag.textContent = interest.trim();
                    interestsContainer.appendChild(tag);
                }
            });
        }
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
