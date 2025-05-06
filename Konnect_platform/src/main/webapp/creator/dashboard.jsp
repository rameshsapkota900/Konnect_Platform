<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Check if user is logged in and has creator role
    if (session.getAttribute("user") == null || !session.getAttribute("role").equals("creator")) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Creator Dashboard" />
</jsp:include>

<jsp:include page="/includes/nav-creator.jsp">
    <jsp:param name="active" value="dashboard" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <div class="dashboard">
                <div class="sidebar">
                    <h3 class="sidebar-title">Creator Panel</h3>
                    <ul class="sidebar-menu">
                        <li><a href="dashboard.jsp" class="active"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                        <li><a href="profile.jsp"><i class="fas fa-user-circle"></i> Profile</a></li>
                        <li><a href="campaigns.jsp"><i class="fas fa-bullhorn"></i> Campaigns</a></li>
                        <li><a href="applications.jsp"><i class="fas fa-file-alt"></i> Applications</a></li>
                        <li><a href="earnings.jsp"><i class="fas fa-money-bill-wave"></i> Earnings</a></li>
                        <li><a href="settings.jsp"><i class="fas fa-cog"></i> Settings</a></li>
                        <li><a href="../logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                    </ul>
                </div>

                <div class="dashboard-content">
                    <div class="dashboard-header">
                        <h2 class="dashboard-title"><i class="fas fa-tachometer-alt"></i> Creator Dashboard</h2>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-bullhorn"></i>
                                </div>
                                <div class="stats-info">
                                    <h3>0</h3>
                                    <p>Available Campaigns</p>
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
                                    <p>Active Applications</p>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-check-circle"></i>
                                </div>
                                <div class="stats-info">
                                    <h3>0</h3>
                                    <p>Completed Campaigns</p>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="stats-card">
                                <div class="stats-icon">
                                    <i class="fas fa-money-bill-wave"></i>
                                </div>
                                <div class="stats-info">
                                    <h3>$0</h3>
                                    <p>Total Earnings</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="card">
                                <h3 class="card-title"><i class="fas fa-info-circle"></i> Welcome to Konnect!</h3>
                                <p>Welcome to your creator dashboard. Here you can manage your profile, browse campaigns, and track your applications.</p>
                                <p>To get started, complete your profile to showcase your social media presence and set your rates.</p>
                                <div class="mt-3">
                                    <a href="profile.jsp" class="btn"><i class="fas fa-edit"></i> Complete Your Profile</a>
                                </div>
                            </div>
                        </div>

                        <div class="col">
                            <div class="card">
                                <h3 class="card-title"><i class="fas fa-bullhorn"></i> Recent Campaigns</h3>
                                <p>No campaigns available at the moment.</p>
                                <div class="mt-3">
                                    <a href="campaigns.jsp" class="btn"><i class="fas fa-search"></i> Browse Campaigns</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <div class="card">
                                <h3 class="card-title"><i class="fas fa-file-alt"></i> My Applications</h3>
                                <p>You haven't applied to any campaigns yet.</p>
                            </div>
                        </div>

                        <div class="col">
                            <div class="card">
                                <h3 class="card-title"><i class="fas fa-lightbulb"></i> Quick Tips</h3>
                                <ul>
                                    <li>Complete your profile with accurate information</li>
                                    <li>Add your social media links and follower counts</li>
                                    <li>Set competitive rates for your services</li>
                                    <li>Browse and apply to relevant campaigns</li>
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
