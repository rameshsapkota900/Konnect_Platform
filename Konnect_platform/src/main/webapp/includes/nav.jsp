<%-- Navigation for general pages --%>
<header>
    <div class="container">
        <nav class="navbar">
            <a href="${pageContext.request.contextPath}/index.jsp" class="logo"><i class="fas fa-link"></i> Konnect</a>
            <button class="mobile-menu-toggle" id="mobile-menu">
                <i class="fas fa-bars"></i>
            </button>
            <ul class="nav-links" id="nav-links">
                <% if (session.getAttribute("user") != null) { 
                    String role = (String) session.getAttribute("role");
                    String dashboardUrl = "";
                    
                    if (role.equals("admin")) {
                        dashboardUrl = request.getContextPath() + "/dashboard";
                    } else if (role.equals("business")) {
                        dashboardUrl = request.getContextPath() + "/dashboard";
                    } else if (role.equals("creator")) {
                        dashboardUrl = request.getContextPath() + "/dashboard";
                    }
                %>
                    <li><a href="<%= dashboardUrl %>"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                <% } else { %>
                    <li><a href="${pageContext.request.contextPath}/login"><i class="fas fa-sign-in-alt"></i> Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register"><i class="fas fa-user-plus"></i> Register</a></li>
                <% } %>
                <li>
                    <button id="theme-toggle" class="theme-toggle" title="Switch Theme">
                        <i class="fas fa-moon"></i>
                    </button>
                </li>
            </ul>
        </nav>
    </div>
</header>

<script>
    // Mobile menu toggle
    document.getElementById('mobile-menu').addEventListener('click', function() {
        document.getElementById('nav-links').classList.toggle('active');
    });
</script>
