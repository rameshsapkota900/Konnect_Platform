<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - Konnect</title>
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
                    <li><a href="<%= request.getContextPath() %>/" >Home</a></li>
                    <li><a href="<%= request.getContextPath() %>/about" class="active">About</a></li>
                    <li><a href="<%= request.getContextPath() %>/contact">Contact</a></li>
                    <li><a href="<%= request.getContextPath() %>/login">Login</a></li>
                    <li><a href="<%= request.getContextPath() %>/register">Register</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Connect Content Creators and Businesses</div>
    </header>
    <main>
        <section class="about-section">
            <div class="about-card">
                <h2>About Konnect</h2>
                <p>Welcome to Konnect, the premier platform connecting content creators with businesses for impactful collaborations. Established in 2023, Konnect has quickly become the go-to marketplace for authentic brand partnerships.</p>
                <p>At Konnect, we believe in the power of genuine connections between creators and brands. Our mission is to facilitate meaningful collaborations that benefit both parties while delivering authentic content to audiences.</p>
            </div>
            <div class="about-card">
                <h3>Our Mission</h3>
                <p>Konnect aims to revolutionize the creator economy by providing a transparent, efficient, and user-friendly platform where creators and businesses can connect, collaborate, and grow together.</p>
                <p>We're committed to:</p>
                <ul>
                    <li>Empowering creators to monetize their content and build sustainable careers</li>
                    <li>Helping businesses find the perfect creators to authentically represent their brands</li>
                    <li>Facilitating transparent and fair collaborations</li>
                    <li>Building a community based on trust, respect, and mutual growth</li>
                </ul>
            </div>
            <div class="about-card">
                <h3>How It Works</h3>
                <p><strong>For Creators:</strong> Create a profile showcasing your content style, audience demographics, and previous work. Browse available campaigns, apply to those that align with your brand, and get paid for successful collaborations.</p>
                <p><strong>For Businesses:</strong> Create campaigns detailing your requirements, target audience, and budget. Review applications from creators, select the best matches for your brand, and manage the entire collaboration process through our platform.</p>
            </div>
            <div class="about-card">
                <h3>Our Team</h3>
                <p>Konnect is powered by a passionate team of professionals dedicated to revolutionizing the creator economy. Led by our visionary CEO Ramesh Sapkota, our team brings together expertise in technology, marketing, and business development to create the ultimate platform for connecting creators and businesses.</p>
                <div class="team-section">
                    <div class="team-member">
                        <div class="profile-image">
                            <img src="<%= request.getContextPath() %>/images/team/ramesh.jpg" alt="Ramesh Sapkota">
                        </div>
                        <h4>Ramesh Sapkota</h4>
                        <p>Founder & CEO</p>
                    </div>
                    <div class="team-member">
                        <div class="profile-image">
                            <img src="<%= request.getContextPath() %>/images/team/rohan.jpg" alt="Rohan Rai">
                        </div>
                        <h4>Rohan Rai</h4>
                        <p>Chief Technology Officer</p>
                    </div>
                    <div class="team-member">
                        <div class="profile-image">
                            <img src="<%= request.getContextPath() %>/images/team/aayush.jpg"alt="Aayush Prasai">
                        </div>
                        <h4>Aayush Prasai</h4>
                        <p>Head of Creator Relations</p>
                    </div>
                    <div class="team-member">
                        <div class="profile-image">
                            <img src="<%= request.getContextPath() %>/images/team/riyaz.jpg" alt="Riyaz Katwal">
                        </div>
                        <h4>Riyaz Katwal</h4>
                        <p>Business Development</p>
                    </div>
                    <div class="team-member">
                        <div class="profile-image">
                            <img src="<%= request.getContextPath() %>/images/team/ganesh.jpg" alt="Ganesh Rouniyar">
                        </div>
                        <h4>Ganesh Rouniyar</h4>
                        <p>Marketing Director</p>
                    </div>
                </div>
            </div>
        </section>
    </main>
    <footer>
        <p>&copy; 2025 Konnect. All rights reserved.</p>
    </footer>
</body>
</html>
