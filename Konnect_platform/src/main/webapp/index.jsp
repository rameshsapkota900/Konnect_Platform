<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="title" value="Connect Content Creators and Businesses" />
</jsp:include>

<jsp:include page="/includes/nav-public.jsp">
    <jsp:param name="active" value="home" />
</jsp:include>

    <main class="main-content">
        <div class="container">
            <section class="hero animate-fade-in">
                <div class="hero-content">
                    <h1>Connect. <span style="opacity: 0.7;">Collaborate.</span> Create.</h1>
                    <p>Konnect is a platform that brings content creators and businesses together for successful collaborations.</p>
                    <div class="mt-3 btn-group">
                        <a href="register?role=creator" class="btn"><i class="fas fa-user-circle"></i> Join as Creator</a>
                        <a href="register?role=business" class="btn"><i class="fas fa-briefcase"></i> Join as Business</a>
                    </div>
                </div>
                <div class="hero-shapes">
                    <div class="shape shape-1"></div>
                    <div class="shape shape-2"></div>
                    <div class="shape shape-3"></div>
                </div>
            </section>

            <section class="features">
                <div class="row">
                    <div class="col">
                        <div class="feature-card animate-fade-in" style="animation-delay: 0.2s;">
                            <h2><i class="fas fa-user-circle"></i> For Content Creators</h2>
                            <p>Showcase your social media presence, set your rates, and find businesses looking for your unique style.</p>
                            <ul>
                                <li>Create a professional profile</li>
                                <li>Browse active campaigns</li>
                                <li>Apply to relevant opportunities</li>
                                <li>Track your applications</li>
                                <li>Manage your earnings</li>
                                <li>Build your portfolio</li>
                            </ul>
                        </div>
                    </div>

                    <div class="col">
                        <div class="feature-card animate-fade-in" style="animation-delay: 0.4s;">
                            <h2><i class="fas fa-briefcase"></i> For Businesses</h2>
                            <p>Find the perfect content creators to promote your products or services to their engaged audience.</p>
                            <ul>
                                <li>Create promotional campaigns</li>
                                <li>Browse creator profiles</li>
                                <li>Invite creators to collaborate</li>
                                <li>Manage your campaigns</li>
                                <li>Track campaign performance</li>
                                <li>Access analytics and insights</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </section>

            <section class="how-it-works animate-fade-in" style="animation-delay: 0.6s;">
                <div class="container">
                    <h2>How It Works</h2>
                    <p class="section-subtitle text-center">Our simple 4-step process makes collaboration easy and effective</p>

                    <div class="row">
                        <div class="col">
                            <div class="step">
                                <div class="step-number">1</div>
                                <i class="fas fa-user-plus"></i>
                                <h3>Sign Up</h3>
                                <p>Create your account as a creator or business in just minutes</p>
                            </div>
                        </div>
                        <div class="col">
                            <div class="step">
                                <div class="step-number">2</div>
                                <i class="fas fa-search"></i>
                                <h3>Connect</h3>
                                <p>Find the perfect match for your collaboration needs</p>
                            </div>
                        </div>
                        <div class="col">
                            <div class="step">
                                <div class="step-number">3</div>
                                <i class="fas fa-handshake"></i>
                                <h3>Collaborate</h3>
                                <p>Work together to create amazing content that resonates</p>
                            </div>
                        </div>
                        <div class="col">
                            <div class="step">
                                <div class="step-number">4</div>
                                <i class="fas fa-chart-line"></i>
                                <h3>Grow</h3>
                                <p>Expand your reach and achieve your business goals</p>
                            </div>
                        </div>
                    </div>

                    <div class="text-center mt-3 btn-group">
                        <a href="register" class="btn btn-lg">Get Started Today</a>
                    </div>
                </div>
            </section>
        </div>
    </main>

    <jsp:include page="/includes/footer.jsp" />
</body>
</html>
