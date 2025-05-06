/**
 * Konnect Platform - Theme Switcher
 * A modern theme switcher with smooth transitions and local storage persistence
 * Version: 1.0.0
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize theme switcher
    initThemeSwitcher();
    
    // Add ripple effect to buttons
    initRippleEffect();
    
    // Initialize animations
    initAnimations();
});

/**
 * Initialize the theme switcher functionality
 */
function initThemeSwitcher() {
    const themeToggle = document.getElementById('theme-toggle');
    const htmlElement = document.documentElement;
    const prefersDarkScheme = window.matchMedia('(prefers-color-scheme: dark)');
    
    // Check for saved theme preference or use the system preference
    const savedTheme = localStorage.getItem('theme');
    
    // Set initial theme
    if (savedTheme) {
        htmlElement.setAttribute('data-theme', savedTheme);
        updateThemeIcon(savedTheme);
    } else {
        // If no saved preference, use system preference
        const systemTheme = prefersDarkScheme.matches ? 'dark' : 'light';
        htmlElement.setAttribute('data-theme', systemTheme);
        updateThemeIcon(systemTheme);
    }
    
    // Add click event to theme toggle button
    if (themeToggle) {
        themeToggle.addEventListener('click', function(e) {
            // Add ripple effect
            addRippleEffect(this, e);
            
            // Toggle theme
            const currentTheme = htmlElement.getAttribute('data-theme');
            const newTheme = currentTheme === 'light' ? 'dark' : 'light';
            
            // Apply smooth transition
            htmlElement.classList.add('theme-transition');
            
            // Update theme
            htmlElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            
            // Update icon
            updateThemeIcon(newTheme);
            
            // Remove transition class after animation completes
            setTimeout(() => {
                htmlElement.classList.remove('theme-transition');
            }, 300);
        });
    }
    
    // Listen for system theme changes
    prefersDarkScheme.addEventListener('change', function(e) {
        // Only update if user hasn't set a preference
        if (!localStorage.getItem('theme')) {
            const newTheme = e.matches ? 'dark' : 'light';
            htmlElement.setAttribute('data-theme', newTheme);
            updateThemeIcon(newTheme);
        }
    });
}

/**
 * Update the theme toggle icon based on current theme
 */
function updateThemeIcon(theme) {
    const themeToggle = document.getElementById('theme-toggle');
    if (!themeToggle) return;
    
    // Check if using Material Icons or Font Awesome
    const iconElement = themeToggle.querySelector('.material-icons') || themeToggle.querySelector('i');
    
    if (iconElement) {
        if (iconElement.classList.contains('material-icons')) {
            // Material Icons
            iconElement.textContent = theme === 'light' ? 'dark_mode' : 'light_mode';
        } else {
            // Font Awesome
            iconElement.className = theme === 'light' ? 'fas fa-moon' : 'fas fa-sun';
        }
    }
}

/**
 * Initialize ripple effect for buttons and interactive elements
 */
function initRippleEffect() {
    // Add ripple class to buttons and interactive elements
    const buttons = document.querySelectorAll('.btn, .theme-toggle, .mobile-menu-toggle, .sidebar-menu a, .nav-links a');
    
    buttons.forEach(button => {
        button.classList.add('ripple');
        
        button.addEventListener('click', function(e) {
            addRippleEffect(this, e);
        });
    });
}

/**
 * Add ripple effect to an element
 */
function addRippleEffect(element, event) {
    const rect = element.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    
    const ripple = document.createElement('span');
    ripple.classList.add('ripple-effect');
    ripple.style.left = `${x}px`;
    ripple.style.top = `${y}px`;
    
    element.appendChild(ripple);
    
    setTimeout(() => {
        ripple.remove();
    }, 600);
}

/**
 * Initialize animations for elements with animation classes
 */
function initAnimations() {
    // Get all elements with animation classes
    const animatedElements = document.querySelectorAll('.animate-fade-in, .animate-slide-right, .animate-slide-left');
    
    // Set up Intersection Observer
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            // Add animation when element is in viewport
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.visibility = 'visible';
                
                // Stop observing after animation is triggered
                observer.unobserve(entry.target);
            }
        });
    }, {
        threshold: 0.1
    });
    
    // Observe each animated element
    animatedElements.forEach(element => {
        // Set initial state
        element.style.opacity = '0';
        element.style.visibility = 'hidden';
        
        // Start observing
        observer.observe(element);
    });
    
    // Add staggered animation delay to elements in the same container
    const animationContainers = document.querySelectorAll('.features, .dashboard .row');
    
    animationContainers.forEach(container => {
        const items = container.querySelectorAll('.animate-fade-in, .animate-slide-right, .animate-slide-left');
        
        items.forEach((item, index) => {
            item.style.animationDelay = `${0.1 + (index * 0.1)}s`;
        });
    });
}
