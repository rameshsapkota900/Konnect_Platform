<%-- Navigation for business pages --%>
<header>
    <div class="container">
        <nav class="navbar">
            <a href="${pageContext.request.contextPath}/business/dashboard.jsp" class="logo"><i class="fas fa-link"></i> Konnect</a>
            <button class="mobile-menu-toggle" id="mobile-menu">
                <i class="fas fa-bars"></i>
            </button>
            <ul class="nav-links" id="nav-links">
                <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
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
