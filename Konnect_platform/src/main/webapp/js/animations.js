/**
 * Konnect Platform - Animations
 * Advanced animations and interactive effects
 * Version: 1.0.0
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize all animations
    initParallaxEffect();
    initCardHoverEffects();
    initCounterAnimations();
    initTypingEffect();
    initScrollReveal();
});

/**
 * Initialize parallax effect for hero section
 */
function initParallaxEffect() {
    const heroSection = document.querySelector('.hero');
    const shapes = document.querySelectorAll('.shape');
    
    if (!heroSection || !shapes.length) return;
    
    // Add parallax effect on mouse move
    heroSection.addEventListener('mousemove', function(e) {
        const x = e.clientX / window.innerWidth;
        const y = e.clientY / window.innerHeight;
        
        shapes.forEach((shape, index) => {
            // Different movement speed for each shape
            const speed = (index + 1) * 20;
            const xOffset = (x - 0.5) * speed;
            const yOffset = (y - 0.5) * speed;
            
            shape.style.transform = `translate(${xOffset}px, ${yOffset}px)`;
        });
    });
    
    // Reset position when mouse leaves
    heroSection.addEventListener('mouseleave', function() {
        shapes.forEach(shape => {
            shape.style.transform = 'translate(0, 0)';
            shape.style.transition = 'transform 0.5s ease';
        });
    });
}

/**
 * Initialize hover effects for cards
 */
function initCardHoverEffects() {
    const cards = document.querySelectorAll('.card, .stats-card, .feature-card');
    
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = 'var(--shadow-lg)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = 'var(--shadow-md)';
        });
    });
}

/**
 * Initialize counter animations for statistics
 */
function initCounterAnimations() {
    const statsNumbers = document.querySelectorAll('.stats-info h3');
    
    if (!statsNumbers.length) return;
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const target = entry.target;
                const targetValue = parseFloat(target.textContent.replace(/[^0-9.-]+/g, ''));
                
                if (isNaN(targetValue)) return;
                
                let prefix = '';
                if (target.textContent.startsWith('$')) {
                    prefix = '$';
                }
                
                let startValue = 0;
                const duration = 1500;
                const startTime = performance.now();
                
                function updateCounter(currentTime) {
                    const elapsedTime = currentTime - startTime;
                    const progress = Math.min(elapsedTime / duration, 1);
                    
                    // Easing function for smooth animation
                    const easedProgress = 1 - Math.pow(1 - progress, 3);
                    
                    const currentValue = Math.floor(startValue + (targetValue - startValue) * easedProgress);
                    target.textContent = `${prefix}${currentValue}`;
                    
                    if (progress < 1) {
                        requestAnimationFrame(updateCounter);
                    } else {
                        target.textContent = `${prefix}${targetValue}`;
                    }
                }
                
                requestAnimationFrame(updateCounter);
                observer.unobserve(target);
            }
        });
    }, {
        threshold: 0.1
    });
    
    statsNumbers.forEach(number => {
        observer.observe(number);
    });
}

/**
 * Initialize typing effect for hero heading
 */
function initTypingEffect() {
    const heroHeading = document.querySelector('.hero h1');
    
    if (!heroHeading) return;
    
    // Only apply if not already processed
    if (heroHeading.getAttribute('data-typing-initialized')) return;
    
    const originalText = heroHeading.innerHTML;
    heroHeading.setAttribute('data-typing-initialized', 'true');
    
    // Clear the heading
    heroHeading.innerHTML = '';
    
    // Create a span for the cursor
    const cursor = document.createElement('span');
    cursor.classList.add('typing-cursor');
    cursor.textContent = '|';
    cursor.style.animation = 'blink 1s step-end infinite';
    
    // Add CSS for cursor blinking
    if (!document.querySelector('#typing-cursor-style')) {
        const style = document.createElement('style');
        style.id = 'typing-cursor-style';
        style.textContent = `
            @keyframes blink {
                from, to { opacity: 1; }
                50% { opacity: 0; }
            }
            
            .typing-cursor {
                color: var(--primary-color);
                font-weight: bold;
            }
        `;
        document.head.appendChild(style);
    }
    
    // Add the cursor to the heading
    heroHeading.appendChild(cursor);
    
    // Type the text character by character
    let i = 0;
    const typingSpeed = 50; // milliseconds per character
    
    function typeCharacter() {
        if (i < originalText.length) {
            // Check if we're at an HTML tag
            if (originalText[i] === '<') {
                // Find the end of the tag
                const tagEnd = originalText.indexOf('>', i);
                if (tagEnd !== -1) {
                    // Insert the entire tag at once
                    const tag = originalText.substring(i, tagEnd + 1);
                    heroHeading.insertAdjacentHTML('beforeend', tag);
                    i = tagEnd + 1;
                } else {
                    // Just in case there's a < without a >
                    heroHeading.insertAdjacentHTML('beforeend', originalText[i]);
                    i++;
                }
            } else {
                // Insert regular character
                heroHeading.insertAdjacentHTML('beforeend', originalText[i]);
                i++;
            }
            
            // Move cursor to the end
            heroHeading.appendChild(cursor);
            
            // Schedule next character
            setTimeout(typeCharacter, typingSpeed);
        }
    }
    
    // Start typing after a short delay
    setTimeout(typeCharacter, 500);
}

/**
 * Initialize scroll reveal animations
 */
function initScrollReveal() {
    const revealElements = document.querySelectorAll('.card, .stats-card, .feature-card, .dashboard-header, .sidebar');
    
    if (!revealElements.length) return;
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('revealed');
                observer.unobserve(entry.target);
            }
        });
    }, {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    });
    
    // Add CSS for reveal animation
    if (!document.querySelector('#scroll-reveal-style')) {
        const style = document.createElement('style');
        style.id = 'scroll-reveal-style';
        style.textContent = `
            .card, .stats-card, .feature-card, .dashboard-header, .sidebar {
                opacity: 0;
                transform: translateY(20px);
                transition: opacity 0.6s ease, transform 0.6s ease;
            }
            
            .revealed {
                opacity: 1;
                transform: translateY(0);
            }
        `;
        document.head.appendChild(style);
    }
    
    revealElements.forEach(element => {
        observer.observe(element);
    });
}
