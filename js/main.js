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
            const formContainer = inquiryForm.parentElement;
            const successDiv = document.createElement('div');
            successDiv.className = 'form-success';
            successDiv.innerHTML = `
                <div style="background: #D4AF37; color: #000; padding: 2rem; border-radius: 8px; text-align: center; margin-bottom: 2rem;">
                    <h3>Submission Successful!</h3>
                    <p>Thank you, ${name}. Your inquiry for ${service} has been received.</p>
                    <p>We are redirecting you to WhatsApp to start a direct conversation...</p>
                </div>
            `;

            inquiryForm.style.display = 'none';
            formContainer.insertBefore(successDiv, inquiryForm);

            // WhatsApp Redirect
            const whatsappNumber = CONFIG.whatsapp;
            const whatsappText = `Hello Shri Anand Construction, I'm interested in ${service}. \nName: ${name} \nPhone: ${phone} \nEmail: ${email} \nMessage: ${message}`;
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

    // Update Footer Contact Info from Config
    const configPhoneEls = document.querySelectorAll('.config-phone');
    configPhoneEls.forEach(el => el.innerText = CONFIG.phone);

    const configEmailEls = document.querySelectorAll('.config-email');
    configEmailEls.forEach(el => {
        el.innerText = CONFIG.email;
        if (el.tagName === 'A') el.href = `mailto:${CONFIG.email}`;
    });

    const configAddressEls = document.querySelectorAll('.config-address');
    configAddressEls.forEach(el => el.innerText = CONFIG.address);
});
