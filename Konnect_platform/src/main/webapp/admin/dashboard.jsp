<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Check if user is logged in and has admin role
    if (session.getAttribute("user") == null || session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")) {
        // Forward to login page instead of redirect to prevent loops
        request.getRequestDispatcher("/login.jsp").forward(request, response);
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Admin Dashboard" />
</jsp:include>

<jsp:include page="/includes/nav-admin.jsp">
    <jsp:param name="active" value="dashboard" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Admin Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/admin/dashboard" class="active"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/users"><i class="fas fa-users"></i> Manage Users</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/campaigns"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/admin/reports"><i class="fas fa-flag"></i> Reports</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-tachometer-alt"></i> Admin Dashboard</h2>
                        <div class="dashboard-actions">
                            <div class="search-form">
                                <input type="text" class="search-input" placeholder="Search...">
                                <button type="submit" class="search-button"><i class="fas fa-search"></i></button>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-users"></i>
                                </div>
                                <div class="stats-info">
                                    <h3><%= request.getAttribute("totalUsers") != null ? request.getAttribute("totalUsers") : "0" %></h3>
                                    <p>Total Users</p>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-user-circle"></i>
                                </div>
                                <div class="stats-info">
                                    <h3><%= request.getAttribute("totalCreators") != null ? request.getAttribute("totalCreators") : "0" %></h3>
                                    <p>Content Creators</p>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-briefcase"></i>
                                </div>
                                <div class="stats-info">
                                    <h3><%= request.getAttribute("totalBusinesses") != null ? request.getAttribute("totalBusinesses") : "0" %></h3>
                                    <p>Business Owners</p>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-bullhorn"></i>
                                </div>
                                <div class="stats-info">
                                    <h3><%= request.getAttribute("activeCampaigns") != null ? request.getAttribute("activeCampaigns") : "0" %></h3>
                                    <p>Active Campaigns</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="card">
                                <h3 class="card-title"><i class="fas fa-chart-line"></i> Platform Analytics</h3>
                                <div class="analytics-grid">
                                    <div class="analytics-item">
                                        <div class="analytics-label">Active Users</div>
                                        <div class="analytics-value"><%= request.getAttribute("activeUsers") != null ? request.getAttribute("activeUsers") : "0" %></div>
                                    </div>
                                    <div class="analytics-item">
                                        <div class="analytics-label">Banned Users</div>
                                        <div class="analytics-value"><%= request.getAttribute("bannedUsers") != null ? request.getAttribute("bannedUsers") : "0" %></div>
                                    </div>
                                    <div class="analytics-item">
                                        <div class="analytics-label">Total Applications</div>
                                        <div class="analytics-value"><%= request.getAttribute("totalApplications") != null ? request.getAttribute("totalApplications") : "0" %></div>
                                    </div>
                                    <div class="analytics-item">
                                        <div class="analytics-label">Pending Applications</div>
                                        <div class="analytics-value"><%= request.getAttribute("pendingApplications") != null ? request.getAttribute("pendingApplications") : "0" %></div>
                                    </div>
                                    <div class="analytics-item">
                                        <div class="analytics-label">Total Reports</div>
                                        <div class="analytics-value"><%= request.getAttribute("totalReports") != null ? request.getAttribute("totalReports") : "0" %></div>
                                    </div>
                                    <div class="analytics-item">
                                        <div class="analytics-label">Pending Reports</div>
                                        <div class="analytics-value"><%= request.getAttribute("pendingReports") != null ? request.getAttribute("pendingReports") : "0" %></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="card">
                                <h3 class="card-title"><i class="fas fa-info-circle"></i> Platform Overview</h3>
                                <p>Welcome to the admin dashboard. Here you can manage users, campaigns, and reports.</p>
                                <p>Use the sidebar navigation to access different sections of the admin panel.</p>
                            </div>
                        </div>

                        <div class="col">
                            <div class="card">
                                <h3 class="card-title"><i class="fas fa-history"></i> Recent Activity</h3>
                                <div class="activity-list">
                                    <div class="activity-item">
                                        <div class="activity-icon"><i class="fas fa-user-plus"></i></div>
                                        <div class="activity-details">
                                            <div class="activity-text">New user registered</div>
                                            <div class="activity-time">2 hours ago</div>
                                        </div>
                                    </div>
                                    <div class="activity-item">
                                        <div class="activity-icon"><i class="fas fa-bullhorn"></i></div>
                                        <div class="activity-details">
                                            <div class="activity-text">New campaign created</div>
                                            <div class="activity-time">5 hours ago</div>
                                        </div>
                                    </div>
                                    <div class="activity-item">
                                        <div class="activity-icon"><i class="fas fa-flag"></i></div>
                                        <div class="activity-details">
                                            <div class="activity-text">New report submitted</div>
                                            <div class="activity-time">1 day ago</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="card">
                                <h3 class="card-title"><i class="fas fa-tasks"></i> Quick Actions</h3>
                                <div class="mt-3 btn-group">
                                    <a href="<%= request.getContextPath() %>/admin/users" class="btn btn-fixed-height"><i class="fas fa-user-plus"></i> Manage Users</a>
                                    <a href="<%= request.getContextPath() %>/admin/campaigns" class="btn btn-fixed-height"><i class="fas fa-plus-circle"></i> View Campaigns</a>
                                    <a href="<%= request.getContextPath() %>/admin/reports" class="btn btn-fixed-height"><i class="fas fa-chart-line"></i> View Reports</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
