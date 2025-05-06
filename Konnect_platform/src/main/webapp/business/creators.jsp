<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.CreatorProfile" %>
<%
    // Check if user is logged in and has business role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("business")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Get creators from request
    List<CreatorProfile> creators = (List<CreatorProfile>) request.getAttribute("creators");
    if (creators == null) {
        // If not set, redirect to profile servlet to get the data
        response.sendRedirect(request.getContextPath() + "/profile/creators");
        return;
    }

    // Get search term if any
    String searchTerm = (String) request.getAttribute("searchTerm");
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Find Creators" />
</jsp:include>

<jsp:include page="/includes/nav-business.jsp">
    <jsp:param name="active" value="creators" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Business Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-building"></i> Company Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile/creators" class="active"><i class="fas fa-users"></i> Find Creators</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-users"></i> Find Creators</h2>
                        <div class="dashboard-actions">
                            <form action="<%= request.getContextPath() %>/profile/search-creators" method="post" class="search-form">
                                <input type="text" name="interests" placeholder="Search by interests..." class="search-input" value="<%= searchTerm != null ? searchTerm : "" %>">
                                <button type="submit" class="search-button btn-fixed-height"><i class="fas fa-search"></i></button>
                            </form>
                        </div>
                    </div>

                    <div class="filter-bar">
                        <div class="filter-group">
                            <label>Filter by:</label>
                            <select id="platformFilter" onchange="filterCreators()">
                                <option value="all">All Platforms</option>
                                <option value="instagram">Instagram</option>
                                <option value="youtube">YouTube</option>
                                <option value="tiktok">TikTok</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label>Followers:</label>
                            <select id="followersFilter" onchange="filterCreators()">
                                <option value="all">Any</option>
                                <option value="0-1000">0 - 1K</option>
                                <option value="1000-10000">1K - 10K</option>
                                <option value="10000-100000">10K - 100K</option>
                                <option value="100000+">100K+</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label>Sort by:</label>
                            <select id="sortBy" onchange="filterCreators()">
                                <option value="followers-high">Followers (High to Low)</option>
                                <option value="followers-low">Followers (Low to High)</option>
                                <option value="price-high">Price (High to Low)</option>
                                <option value="price-low">Price (Low to High)</option>
                            </select>
                        </div>
                    </div>

                    <% if (searchTerm != null) { %>
                    <div class="search-results-info">
                        <p>Showing results for: <strong><%= searchTerm %></strong> <a href="<%= request.getContextPath() %>/profile/creators" class="clear-search">(Clear)</a></p>
                    </div>
                    <% } %>

                    <% if (creators.isEmpty()) { %>
                        <div class="card">
                            <div class="empty-state">
                                <i class="fas fa-users empty-icon"></i>
                                <h3>No Creators Found</h3>
                                <% if (searchTerm != null) { %>
                                    <p>No creators match your search criteria. Try different keywords or browse all creators.</p>
                                    <a href="<%= request.getContextPath() %>/profile/creators" class="btn btn-fixed-height mt-3"><i class="fas fa-users"></i> View All Creators</a>
                                <% } else { %>
                                    <p>There are no creators available at the moment. Check back later.</p>
                                <% } %>
                            </div>
                        </div>
                    <% } else { %>
                        <div class="creator-grid">
                            <% for (CreatorProfile creator : creators) { %>
                                <div class="creator-card"
                                     data-instagram="<%= creator.getInstagramFollowers() > 0 ? "true" : "false" %>"
                                     data-youtube="<%= creator.getYoutubeFollowers() > 0 ? "true" : "false" %>"
                                     data-tiktok="<%= creator.getTiktokFollowers() > 0 ? "true" : "false" %>"
                                     data-followers="<%= creator.getTotalFollowers() %>"
                                     data-price="<%= creator.getPostPrice() %>">
                                    <div class="creator-card-header">
                                        <h3><a href="<%= request.getContextPath() %>/profile/view/<%= creator.getUserId() %>"><%= creator.getFullName() %></a></h3>
                                        <div class="creator-stats">
                                            <% if (creator.getInstagramFollowers() > 0) { %>
                                                <span class="stat"><i class="fab fa-instagram"></i> <%= formatFollowers(creator.getInstagramFollowers()) %></span>
                                            <% } %>
                                            <% if (creator.getYoutubeFollowers() > 0) { %>
                                                <span class="stat"><i class="fab fa-youtube"></i> <%= formatFollowers(creator.getYoutubeFollowers()) %></span>
                                            <% } %>
                                            <% if (creator.getTiktokFollowers() > 0) { %>
                                                <span class="stat"><i class="fab fa-tiktok"></i> <%= formatFollowers(creator.getTiktokFollowers()) %></span>
                                            <% } %>
                                        </div>
                                    </div>
                                    <div class="creator-card-body">
                                        <p class="creator-bio"><%= creator.getBio().length() > 100 ? creator.getBio().substring(0, 100) + "..." : creator.getBio() %></p>
                                        <div class="creator-interests">
                                            <%
                                                if (creator.getInterests() != null) {
                                                    String[] interests = creator.getInterests().split(",");
                                                    for (int i = 0; i < Math.min(interests.length, 3); i++) {
                                                        if (interests[i].trim().length() > 0) {
                                            %>
                                                        <span class="tag"><%= interests[i].trim() %></span>
                                            <%
                                                        }
                                                    }
                                                    if (interests.length > 3) {
                                            %>
                                                        <span class="tag">+<%= interests.length - 3 %> more</span>
                                            <%
                                                    }
                                                }
                                            %>
                                        </div>
                                    </div>
                                    <div class="creator-card-footer">
                                        <div class="creator-price">
                                            <span>From $<%= String.format("%.2f", creator.getPostPrice()) %></span>
                                        </div>
                                        <div class="creator-actions">
                                            <a href="<%= request.getContextPath() %>/profile/view/<%= creator.getUserId() %>" class="btn btn-sm btn-fixed-height">View Profile</a>
                                            <a href="<%= request.getContextPath() %>/message/conversation/<%= creator.getUserId() %>" class="btn btn-sm btn-outline btn-fixed-height"><i class="fas fa-envelope"></i></a>
                                        </div>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    <% } %>
                </div>
            </div>
        </div>
    </main>

    <script>
        function filterCreators() {
            const platformFilter = document.getElementById('platformFilter').value;
            const followersFilter = document.getElementById('followersFilter').value;
            const sortBy = document.getElementById('sortBy').value;
            const creatorCards = document.querySelectorAll('.creator-card');

            // Filter by platform and followers
            creatorCards.forEach(card => {
                const hasInstagram = card.dataset.instagram === 'true';
                const hasYoutube = card.dataset.youtube === 'true';
                const hasTiktok = card.dataset.tiktok === 'true';
                const followers = parseInt(card.dataset.followers);

                let showByPlatform = true;
                if (platformFilter === 'instagram' && !hasInstagram) showByPlatform = false;
                if (platformFilter === 'youtube' && !hasYoutube) showByPlatform = false;
                if (platformFilter === 'tiktok' && !hasTiktok) showByPlatform = false;

                let showByFollowers = true;
                if (followersFilter === '0-1000' && (followers < 0 || followers > 1000)) showByFollowers = false;
                if (followersFilter === '1000-10000' && (followers < 1000 || followers > 10000)) showByFollowers = false;
                if (followersFilter === '10000-100000' && (followers < 10000 || followers > 100000)) showByFollowers = false;
                if (followersFilter === '100000+' && followers < 100000) showByFollowers = false;

                card.style.display = (showByPlatform && showByFollowers) ? 'flex' : 'none';
            });

            // Sort creators
            const creatorGrid = document.querySelector('.creator-grid');
            const cardsArray = Array.from(creatorCards).filter(card => card.style.display !== 'none');

            cardsArray.sort((a, b) => {
                const followersA = parseInt(a.dataset.followers);
                const followersB = parseInt(b.dataset.followers);
                const priceA = parseFloat(a.dataset.price);
                const priceB = parseFloat(b.dataset.price);

                if (sortBy === 'followers-high') return followersB - followersA;
                if (sortBy === 'followers-low') return followersA - followersB;
                if (sortBy === 'price-high') return priceB - priceA;
                if (sortBy === 'price-low') return priceA - priceB;

                return 0;
            });

            // Re-append sorted cards
            cardsArray.forEach(card => creatorGrid.appendChild(card));
        }
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>

<%!
    // Helper method to format follower counts
    private String formatFollowers(int followers) {
        if (followers < 1000) {
            return String.valueOf(followers);
        } else if (followers < 1000000) {
            return String.format("%.1fK", followers / 1000.0);
        } else {
            return String.format("%.1fM", followers / 1000000.0);
        }
    }
%>
