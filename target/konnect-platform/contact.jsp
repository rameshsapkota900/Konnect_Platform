<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.konnect.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - Konnect</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
    <header>
        <div class="header-row">
            <a href="<%= request.getContextPath() %>/" class="header-logo">Konnect</a>
            <nav>
                <ul class="nav-list">
                    <li><a href="<%= request.getContextPath() %>/" >Home</a></li>
                    <li><a href="<%= request.getContextPath() %>/about">About</a></li>
                    <li><a href="<%= request.getContextPath() %>/contact" class="active">Contact</a></li>
                    <li><a href="<%= request.getContextPath() %>/login">Login</a></li>
                    <li><a href="<%= request.getContextPath() %>/register">Register</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Connect Content Creators and Businesses</div>
    </header>
    <main>
        <section class="contact-section">
            <div class="contact-card">
                <h2>Contact Us</h2>
                <p>Have questions or need assistance? We're here to help! Reach out to us using any of the methods below or fill out the contact form.</p>
                <div class="contact-info">
                    <div>
                        <div class="contact-item">
                            <i class="fas fa-map-marker-alt"></i>
                            <span>123 Konnect Street, Digital City</span>
                        </div>
                        <div class="contact-item">
                            <i class="fas fa-phone"></i>
                            <span>+1 (555) 123-4567</span>
                        </div>
                    </div>
                    <div>
                        <div class="contact-item">
                            <i class="fas fa-envelope"></i>
                            <span>support@konnect.com</span>
                        </div>
                        <div class="contact-item">
                            <i class="fas fa-clock"></i>
                            <span>Mon-Fri: 9AM-5PM</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="contact-card">
                <h3>Send Us a Message</h3>
                <% if (request.getParameter("submitted") != null) { %>
                    <div class="success-message">
                        <i class="fas fa-check-circle"></i>
                        <p>Thank you for your message! We'll get back to you as soon as possible.</p>
                    </div>
                <% } %>
                <div class="contact-form">
                    <form action="https://formsubmit.co/rameshsapkota900@gmail.com" method="POST" id="contactForm">
                        <!-- FormSubmit Configuration -->
                        <input type="hidden" name="_template" value="table" />
                        <input type="hidden" name="_captcha" value="false" />
                        <input type="hidden" name="_next" value="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/contact?submitted=true" />
                        <input type="hidden" name="_subject" value="New Contact Form Submission from Konnect" />
                        <input type="text" name="_honey" style="display:none" />
                        <input type="hidden" name="_autoresponse" value="Thank you for your message! We've received your inquiry and will get back to you as soon as possible. Click here to return to Konnect: <%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() %>/contact?submitted=true" />
                        <div class="form-group">
                            <label for="name"><i class="fas fa-user"></i> Your Name</label>
                            <input type="text" id="name" name="name" placeholder="Enter your name" required />
                        </div>
                        <div class="form-group">
                            <label for="email"><i class="fas fa-envelope"></i> Your Email</label>
                            <input type="email" id="email" name="email" placeholder="Enter your email" required />
                        </div>
                        <div class="form-group">
                            <label for="subject"><i class="fas fa-tag"></i> Subject</label>
                            <input type="text" id="subject" name="subject" placeholder="Enter subject" required />
                        </div>
                        <div class="form-group">
                            <label for="message"><i class="fas fa-comment"></i> Your Message</label>
                            <textarea id="message" name="message" placeholder="Enter your message" required></textarea>
                        </div>
                        <button type="submit"><i class="fas fa-paper-plane"></i> Send Message</button>
                    </form>
                </div>
            </div>
        </section>
    </main>
    <footer>
        <p>&copy; 2025 Konnect. All rights reserved.</p>
    </footer>
    <script>
        document.getElementById('contactForm').addEventListener('submit', function(event) {
            localStorage.setItem('formSubmitted', 'true');
            localStorage.setItem('formSubmitTime', new Date().getTime());
            console.log('Form submitted');
        });
    </script>
</body>
</html>
