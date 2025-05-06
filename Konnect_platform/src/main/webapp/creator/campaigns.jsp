<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.Campaign" %>
<%
    // Check if user is logged in and has creator role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("creator")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    
    // Get campaigns from request
    List<Campaign> campaigns = (List<Campaign>) request.getAttribute("campaigns");
    if (campaigns == null) {
        // If not set, redirect to campaign servlet to get the data
        response.sendRedirect(request.getContextPath() + "/campaign");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Available Campaigns" />
</jsp:include>

<jsp:include page="/includes/nav-creator.jsp">
    <jsp:param name="active" value="campaigns" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Creator Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-user-circle"></i> Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign" class="active"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-bullhorn"></i> Available Campaigns</h2>
                        <div class="dashboard-actions">
                            <form action="<%= request.getContextPath() %>/campaign/search" method="get" class="search-form">
                                <input type="text" name="keyword" placeholder="Search campaigns..." class="search-input">
                                <button type="submit" class="search-button"><i class="fas fa-search"></i></button>
                            </form>
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

                    <div class="filter-bar">
                        <div class="filter-group">
                            <label>Filter by:</label>
                            <select id="budgetFilter" onchange="applyFilters()">
                                <option value="">Budget (All)</option>
                                <option value="0-100">$0 - $100</option>
                                <option value="100-500">$100 - $500</option>
                                <option value="500-1000">$500 - $1000</option>
                                <option value="1000+">$1000+</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label>Sort by:</label>
                            <select id="sortBy" onchange="applyFilters()">
                                <option value="newest">Newest First</option>
                                <option value="oldest">Oldest First</option>
                                <option value="budget-high">Budget (High to Low)</option>
                                <option value="budget-low">Budget (Low to High)</option>
                            </select>
                        </div>
                    </div>

                    <% if (campaigns.isEmpty()) { %>
                        <div class="card">
                            <div class="empty-state">
                                <i class="fas fa-bullhorn empty-icon"></i>
                                <h3>No Campaigns Available</h3>
                                <p>There are no active campaigns available at the moment. Check back later for new opportunities.</p>
                            </div>
                        </div>
                    <% } else { %>
                        <div class="campaign-grid">
                            <% for (Campaign campaign : campaigns) { %>
                                <div class="campaign-card" data-budget="<%= campaign.getBudget() %>">
                                    <div class="campaign-card-header">
                                        <h3><a href="<%= request.getContextPath() %>/campaign/view/<%= campaign.getCampaignId() %>"><%= campaign.getTitle() %></a></h3>
                                        <span class="budget-badge">$<%= String.format("%.2f", campaign.getBudget()) %></span>
                                    </div>
                                    <div class="campaign-card-body">
                                        <p class="campaign-description"><%= campaign.getDescription().length() > 150 ? campaign.getDescription().substring(0, 150) + "..." : campaign.getDescription() %></p>
                                    </div>
                                    <div class="campaign-card-footer">
                                        <span class="campaign-date"><i class="fas fa-calendar-alt"></i> <%= campaign.getCreatedAt() != null ? campaign.getCreatedAt().toString().substring(0, 10) : "N/A" %></span>
                                        <a href="<%= request.getContextPath() %>/campaign/view/<%= campaign.getCampaignId() %>" class="btn btn-sm">View Details</a>
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
        function applyFilters() {
            const budgetFilter = document.getElementById('budgetFilter').value;
            const sortBy = document.getElementById('sortBy').value;
            const campaignCards = document.querySelectorAll('.campaign-card');
            
            // Filter by budget
            campaignCards.forEach(card => {
                const budget = parseFloat(card.dataset.budget);
                let show = true;
                
                if (budgetFilter) {
                    if (budgetFilter === '0-100' && (budget < 0 || budget > 100)) show = false;
                    if (budgetFilter === '100-500' && (budget < 100 || budget > 500)) show = false;
                    if (budgetFilter === '500-1000' && (budget < 500 || budget > 1000)) show = false;
                    if (budgetFilter === '1000+' && budget < 1000) show = false;
                }
                
                card.style.display = show ? 'flex' : 'none';
            });
            
            // Sort campaigns
            const campaignGrid = document.querySelector('.campaign-grid');
            const cardsArray = Array.from(campaignCards).filter(card => card.style.display !== 'none');
            
            cardsArray.sort((a, b) => {
                const budgetA = parseFloat(a.dataset.budget);
                const budgetB = parseFloat(b.dataset.budget);
                const dateA = new Date(a.querySelector('.campaign-date').textContent.replace('📅 ', ''));
                const dateB = new Date(b.querySelector('.campaign-date').textContent.replace('📅 ', ''));
                
                if (sortBy === 'newest') return dateB - dateA;
                if (sortBy === 'oldest') return dateA - dateB;
                if (sortBy === 'budget-high') return budgetB - budgetA;
                if (sortBy === 'budget-low') return budgetA - budgetB;
                
                return 0;
            });
            
            // Re-append sorted cards
            cardsArray.forEach(card => campaignGrid.appendChild(card));
        }
    </script>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
