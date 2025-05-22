<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Konnect - Connect Content Creators and Businesses</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
    <header>
        <div class="header-row">
            <a href="<%= request.getContextPath() %>/" class="header-logo">Konnect</a>
            <nav>
                <ul class="nav-list">
                    <li><a href="<%= request.getContextPath() %>/" class="active">Home</a></li>
                    <li><a href="<%= request.getContextPath() %>/about">About</a></li>
                    <li><a href="<%= request.getContextPath() %>/contact">Contact</a></li>
                    <li><a href="<%= request.getContextPath() %>/login">Login</a></li>
                    <li><a href="<%= request.getContextPath() %>/register">Register</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Connect Content Creators and Businesses</div>
    </header>

    <main>
        <section class="hero">
            <div class="hero-content">
                <h2 style="font-size:4rem;font-weight:800;letter-spacing:-1px;">Bridge the Gap Between Creators and Brands</h2>
                <p style="font-size:1.7rem;max-width:900px;margin:2rem auto 2.5rem auto;">Konnect is a platform that connects content creators with businesses for promotional collaborations.</p>
                <div class="cta-buttons">
                    <a href="<%= request.getContextPath() %>/register?role=creator" class="btn btn-primary">Join as Creator</a>
                    <a href="<%= request.getContextPath() %>/register?role=business" class="btn btn-secondary">Join as Business</a>
                </div>
            </div>
        </section>

        <section class="features">
            <h2 style="font-size:2.5rem;font-weight:700;text-align:center;margin-bottom:3rem;">How It Works</h2>
            <div class="feature-grid">
                <div class="feature-card">
                    <h3>For Content Creators</h3>
                    <ul>
                        <li>Create your profile showcasing your social media presence</li>
                        <li>Browse campaigns that match your interests</li>
                        <li>Apply to campaigns you're interested in</li>
                        <li>Track your applications and collaborations</li>
                    </ul>
                </div>
                <div class="feature-card">
                    <h3>For Businesses</h3>
                    <ul>
                        <li>Create campaigns to promote your products or services</li>
                        <li>Browse creators based on interests and follower count</li>
                        <li>Invite creators to collaborate on your campaigns</li>
                        <li>Manage your campaigns and collaborations</li>
                    </ul>
                </div>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved.</p>
    </footer>
</body>
</html>
