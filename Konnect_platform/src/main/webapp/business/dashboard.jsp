<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Check if user is logged in and has business role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("business")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Business Dashboard" />
</jsp:include>

<jsp:include page="/includes/nav-business.jsp">
    <jsp:param name="active" value="dashboard" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Business Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="<%= request.getContextPath() %>/dashboard" class="active"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile"><i class="fas fa-building"></i> Company Profile</a></li>
                        <li><a href="<%= request.getContextPath() %>/campaign"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="<%= request.getContextPath() %>/profile/creators"><i class="fas fa-users"></i> Find Creators</a></li>
                        <li><a href="<%= request.getContextPath() %>/application"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="<%= request.getContextPath() %>/message"><i class="fas fa-envelope"></i> Messages</a></li>
                        <li><a href="<%= request.getContextPath() %>/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-tachometer-alt"></i> Business Dashboard</h2>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-bullhorn"></i>
                                </div>
                                <div class="stats-info">
                                    <h3>0</h3>
                                    <p>Active Campaigns</p>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-file-alt"></i>
                                </div>
                                <div class="stats-info">
                                    <h3>0</h3>
                                    <p>Applications</p>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-users"></i>
                                </div>
                                <div class="stats-info">
                                    <h3>0</h3>
                                    <p>Collaborations</p>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-credit-card"></i>
                                </div>
                                <div class="stats-info">
                                    <h3>$0</h3>
                                    <p>Total Spent</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="card welcome-card">
                                <h3 class="card-title"><i class="fas fa-rocket"></i> Welcome to Konnect!</h3>
                                <p>Welcome to your business dashboard. Here you can manage your business profile, create campaigns, and find content creators to collaborate with.</p>
                                <p>To get started, complete your business profile and create your first campaign to connect with talented creators.</p>
                                <div class="welcome-actions">
                                    <a href="profile.jsp" class="btn btn-primary btn-block btn-fixed-height"><i class="fas fa-building"></i> Complete Your Profile</a>
                                    <a href="<%= request.getContextPath() %>/campaign/create"class="btn btn-outline btn-block btn-fixed-height"><i class="fas fa-plus-circle"></i> Create Campaign</a>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="card campaigns-card">
                                <h3 class="card-title"><i class="fas fa-bullhorn"></i> My Campaigns</h3>
                                <div class="empty-state">
                                    <i class="fas fa-bullhorn" style="color: var(--warning-color);"></i>
                                    <p>You haven't created any campaigns yet.</p>
                                    <a href="<%= request.getContextPath() %>/campaign" class="btn btn-primary btn-block btn-fixed-height"><i class="fas fa-list"></i> View All Campaigns</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="card applications-card">
                                <h3 class="card-title"><i class="fas fa-file-alt"></i> Recent Applications</h3>
                                <div class="empty-state">
                                    <i class="fas fa-file-alt" style="color: var(--success-color);"></i>
                                    <p>No applications received yet. Create a campaign to start receiving applications from creators.</p>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="card tips-card">
                                <h3 class="card-title"><i class="fas fa-lightbulb"></i> Quick Tips</h3>
                                <ul class="tips-list">
                                    <li>Complete your business profile with accurate information to attract the right creators</li>
                                    <li>Create detailed campaign descriptions with clear objectives and expectations</li>
                                    <li>Set realistic budgets for your campaigns based on creator expertise and reach</li>
                                    <li>Browse creator profiles to find the perfect match for your brand</li>
                                    <li>Maintain clear communication with creators throughout your collaboration</li>
                                </ul>
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
