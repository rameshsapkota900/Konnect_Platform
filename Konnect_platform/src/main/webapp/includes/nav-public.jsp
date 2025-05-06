<%-- Navigation for public pages - Material Design --%>
<header class="mdc-elevation--z4">
    <div class="container">
        <nav class="navbar">
            <a href="${pageContext.request.contextPath}/index.jsp" class="logo">
                <span class="material-icons">link</span> Konnect
            </a>
            <button class="mobile-menu-toggle ripple" id="mobile-menu">
                <span class="material-icons">menu</span>
            </button>
            <ul class="nav-links" id="nav-links">
                <li>
                    <a href="${pageContext.request.contextPath}/login" ${param.active == 'login' ? 'class="active"' : ''}>
                        <span class="material-icons">login</span> Login
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/register" ${param.active == 'register' ? 'class="active"' : ''}>
                        <span class="material-icons">person_add</span> Register
                    </a>
                </li>
                <li>
                    <button id="theme-toggle" class="theme-toggle" title="Switch Theme">
                        <span class="material-icons">dark_mode</span>
                    </button>
                </li>
            </ul>
        </nav>
    </div>
</header>

<script>
    // Mobile menu toggle with ripple effect
    document.getElementById('mobile-menu').addEventListener('click', function(e) {
        // Add ripple effect
        const button = this;
        const rect = button.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;

        const ripple = document.createElement('span');
        ripple.classList.add('ripple-effect');
        ripple.style.left = `${x}px`;
        ripple.style.top = `${y}px`;

        button.appendChild(ripple);

        setTimeout(() => {
            ripple.remove();
        }, 600);

        // Toggle menu
        document.getElementById('nav-links').classList.toggle('active');
    });
</script>
