<%-- Modern Material Design Footer for Konnect Platform --%>
<footer>
    <div class="container">
        <div class="footer-content">
            <p class="mdc-typography--body1">
                <i class="fas fa-link"></i> Konnect - A Platform to Connect Content Creators and Businesses
            </p>

            <div class="social-links">
                <a href="#" title="Facebook" class="ripple">
                    <i class="fab fa-facebook-f"></i>
                </a>
                <a href="#" title="Twitter" class="ripple">
                    <i class="fab fa-twitter"></i>
                </a>
                <a href="#" title="Instagram" class="ripple">
                    <i class="fab fa-instagram"></i>
                </a>
                <a href="#" title="LinkedIn" class="ripple">
                    <i class="fab fa-linkedin-in"></i>
                </a>
                <a href="#" title="YouTube" class="ripple">
                    <i class="fab fa-youtube"></i>
                </a>
                <a href="#" title="TikTok" class="ripple">
                    <i class="fab fa-tiktok"></i>
                </a>
            </div>

            <ul class="footer-links">
                <li><a href="#"><i class="fas fa-info-circle"></i> About Us</a></li>
                <li><a href="#"><i class="fas fa-file-contract"></i> Terms of Service</a></li>
                <li><a href="#"><i class="fas fa-shield-alt"></i> Privacy Policy</a></li>
                <li><a href="#"><i class="fas fa-envelope"></i> Contact</a></li>
                <li><a href="#"><i class="fas fa-question-circle"></i> FAQ</a></li>
                <li><a href="#"><i class="fas fa-blog"></i> Blog</a></li>
            </ul>

            <div class="newsletter">
                <h3 class="mdc-typography--subtitle1">Subscribe to our newsletter</h3>
                <form class="newsletter-form">
                    <input type="email" placeholder="Enter your email" required>
                    <button type="submit" class="btn">
                        <span class="material-icons">send</span> Subscribe
                    </button>
                </form>
            </div>
        </div>

        <div class="copyright">
            <p>&copy; 2025 Konnect. All rights reserved.</p>
        </div>
    </div>
</footer>

<script>
    // Add ripple effect to social links
    document.querySelectorAll('.social-links a').forEach(link => {
        link.addEventListener('click', function(e) {
            const rect = this.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;

            const ripple = document.createElement('span');
            ripple.classList.add('ripple-effect');
            ripple.style.left = `${x}px`;
            ripple.style.top = `${y}px`;

            this.appendChild(ripple);

            setTimeout(() => {
                ripple.remove();
            }, 600);
        });
    });

    // Newsletter form submission
    document.querySelector('.newsletter-form').addEventListener('submit', function(e) {
        e.preventDefault();
        const email = this.querySelector('input[type="email"]').value;
        // Add your newsletter subscription logic here
        console.log('Subscribing email:', email);

        // Show a more modern confirmation
        const button = this.querySelector('button');
        const originalText = button.innerHTML;

        button.innerHTML = '<span class="material-icons">check_circle</span> Subscribed!';
        button.style.backgroundColor = 'var(--success-color)';

        setTimeout(() => {
            button.innerHTML = originalText;
            button.style.backgroundColor = '';
            this.reset();
        }, 2000);
    });
</script>
