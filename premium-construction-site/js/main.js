document.addEventListener('DOMContentLoaded', () => {
    // Mobile Menu Toggle
    const menuToggle = document.querySelector('.menu-toggle');
    const navLinks = document.querySelector('.nav-links');

    if (menuToggle && navLinks) {
        menuToggle.addEventListener('click', () => {
            navLinks.classList.toggle('active');
            menuToggle.innerHTML = navLinks.classList.contains('active') ? '✕' : '☰';
        });
    }

    // Close mobile menu on link click
    const navItems = document.querySelectorAll('.nav-links a');
    navItems.forEach(item => {
        item.addEventListener('click', () => {
            navLinks.classList.remove('active');
            menuToggle.innerHTML = '☰';
        });
    });

    // Inquiry Form Handling
    const inquiryForm = document.getElementById('inquiryForm');
    if (inquiryForm) {
        inquiryForm.addEventListener('submit', (e) => {
            e.preventDefault();

            // Basic Validation
            const name = document.getElementById('name').value;
            const phone = document.getElementById('phone').value;
            const email = document.getElementById('email').value;
            const service = document.getElementById('service').value;
            const message = document.getElementById('message').value;

            if (!name || !phone || !email || !service || !message) {
                alert('Please fill in all fields.');
                return;
            }

            // Success Message
            alert('Thank you! Your inquiry has been submitted successfully. We will contact you soon.');

            // WhatsApp Redirect
            const whatsappNumber = CONFIG.whatsapp;
            const whatsappText = `Hello Apex Construction, I'm interested in ${service}. \nName: ${name} \nPhone: ${phone} \nEmail: ${email} \nMessage: ${message}`;
            const whatsappURL = `https://wa.me/${whatsappNumber}?text=${encodeURIComponent(whatsappText)}`;

            window.open(whatsappURL, '_blank');

            // Reset Form
            inquiryForm.reset();
        });
    }

    // Dynamic Content Loading (if needed)
    const yearSpan = document.getElementById('currentYear');
    if (yearSpan) {
        yearSpan.innerText = new Date().getFullYear();
    }

    const businessNameElements = document.querySelectorAll('.business-name');
    businessNameElements.forEach(el => el.innerText = CONFIG.businessName);
});
