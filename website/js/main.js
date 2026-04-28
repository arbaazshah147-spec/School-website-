document.addEventListener('DOMContentLoaded', () => {
    // 1. Initialize Dynamic Content from CONFIG
    initDynamicContent();

    // 2. Navbar Scroll Effect
    const header = document.querySelector('header');
    window.addEventListener('scroll', () => {
        if (window.scrollY > 50) {
            header.style.padding = '10px 0';
            header.style.backgroundColor = 'rgba(255, 255, 255, 0.98)';
        } else {
            header.style.padding = '20px 0';
            header.style.backgroundColor = 'rgba(255, 255, 255, 0.95)';
        }
    });

    // 3. WhatsApp Redirect
    const whatsappButtons = document.querySelectorAll('.whatsapp-btn, .whatsapp-float');
    whatsappButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const message = encodeURIComponent(`Hi ${CONFIG.businessName}, I'm interested in your services. Can I get more details?`);
            window.open(`https://wa.me/${CONFIG.whatsappNumber.replace('+', '').replace(' ', '')}?text=${message}`, '_blank');
        });
    });

    // 4. Portfolio Filtering
    const filterButtons = document.querySelectorAll('.filter-btn');
    const portfolioContainer = document.getElementById('portfolio-container');

    if (portfolioContainer) {
        initPortfolio();
        filterButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                filterButtons.forEach(b => b.classList.remove('active'));
                btn.classList.add('active');
                const filter = btn.getAttribute('data-filter');
                filterPortfolio(filter);
            });
        });
    }

    // 5. Form Submission (Simulation)
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            const submitBtn = form.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerText;
            submitBtn.innerText = "Sending...";
            submitBtn.disabled = true;

            setTimeout(() => {
                alert("Thank you! Your request has been sent. We will contact you shortly.");
                submitBtn.innerText = originalText;
                submitBtn.disabled = false;
                form.reset();
            }, 1500);
        });
    });

    // 6. Intersection Observer for Scroll Reveal
    const observerOptions = {
        threshold: 0.1
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);

    const isHeadless = navigator.userAgent.includes('Headless');
    document.querySelectorAll('.service-card, .package-card, .stat-item').forEach(el => {
        if (isHeadless) {
            el.style.opacity = '1';
            el.style.transform = 'translateY(0)';
        } else {
            el.style.opacity = '0';
            el.style.transform = 'translateY(30px)';
            el.style.transition = 'all 0.6s ease-out';
            observer.observe(el);
        }
    });

    // 7. Before/After Slider Interaction
    initSlider();
});

function initDynamicContent() {
    // Inject Business Name
    document.querySelectorAll('.business-name').forEach(el => el.innerText = CONFIG.businessName);

    // Inject Location
    document.querySelectorAll('.location-text').forEach(el => el.innerText = CONFIG.location);

    // Inject Contact Info
    document.querySelectorAll('.phone-text').forEach(el => el.innerText = CONFIG.whatsappNumber);
    document.querySelectorAll('.email-text').forEach(el => el.innerText = CONFIG.email);
    document.querySelectorAll('.owner-name').forEach(el => el.innerText = CONFIG.ownerName);

    // Inject Stats (Home)
    const statProj = document.querySelector('.stat-projects');
    if (statProj) statProj.innerText = CONFIG.stats.projects;
    const statCli = document.querySelector('.stat-clients');
    if (statCli) statCli.innerText = CONFIG.stats.clients;
    const statExp = document.querySelector('.stat-experience');
    if (statExp) statExp.innerText = CONFIG.stats.experience;

    // Inject Services (Home & Services Page)
    const servicesContainer = document.getElementById('services-container');
    const servicesPageContainer = document.getElementById('services-page-container');

    if (servicesContainer || servicesPageContainer) {
        const container = servicesContainer || servicesPageContainer;
        CONFIG.services.forEach(service => {
            const card = document.createElement('div');
            card.className = 'service-card';
            card.innerHTML = `
                <div style="background: var(--light-gray); height: 200px; display: flex; align-items: center; justify-content: center; font-size: 3rem; color: var(--primary-color);">
                    <i class="fas ${getServiceIcon(service.id)}"></i>
                </div>
                <h3>${service.title}</h3>
                <p>${service.description}</p>
                <div style="margin-top: 20px; display: flex; gap: 10px; justify-content: center;">
                    <a href="quote.html" class="btn btn-primary">Get Quote</a>
                    <a href="#" class="btn btn-outline whatsapp-btn" style="border-color: var(--primary-color); color: var(--secondary-color); padding: 15px 20px;"><i class="fab fa-whatsapp"></i></a>
                </div>
            `;
            container.appendChild(card);
        });
    }

    // Inject Packages (Home)
    const packagesContainer = document.getElementById('packages-container');
    if (packagesContainer) {
        CONFIG.packages.forEach((pkg, index) => {
            const card = document.createElement('div');
            card.className = `package-card ${index === 1 ? 'featured' : ''}`;
            card.innerHTML = `
                <h3>${pkg.name}</h3>
                <div class="price">${pkg.price}</div>
                <ul>
                    ${pkg.features.map(f => `<li>${f}</li>`).join('')}
                </ul>
                <a href="quote.html" class="btn ${index === 1 ? 'btn-primary' : 'btn-outline'}" style="width: 100%; border-color: var(--primary-color); color: ${index === 1 ? 'var(--secondary-color)' : 'var(--secondary-color)'}">Select Plan</a>
            `;
            packagesContainer.appendChild(card);
        });
    }
}

function initSlider() {
    const slider = document.getElementById('transformation-slider');
    if (!slider) return;

    const afterImg = slider.querySelector('.after');
    const handle = slider.querySelector('.slider-handle');

    const moveSlider = (e) => {
        let x = e.type.includes('touch') ? e.touches[0].clientX : e.clientX;
        let rect = slider.getBoundingClientRect();
        let position = ((x - rect.left) / rect.width) * 100;

        if (position < 0) position = 0;
        if (position > 100) position = 100;

        afterImg.style.width = `${position}%`;
        handle.style.left = `${position}%`;
    };

    slider.addEventListener('mousemove', moveSlider);
    slider.addEventListener('touchmove', moveSlider);
}

function getServiceIcon(id) {
    switch(id) {
        case 'pop-ceiling': return 'fa-layer-group';
        case 'wall-design': return 'fa-paint-roller';
        case 'interior-decoration': return 'fa-couch';
        case 'renovation': return 'fa-tools';
        default: return 'fa-home';
    }
}

const portfolioData = [
    { title: "Luxury Living Room", category: "Luxury", image: "assets/p1.jpg" },
    { title: "Modern Shop Interior", category: "Commercial", image: "assets/p2.webp" },
    { title: "Budget Friendly Bedroom", category: "Budget", image: "assets/p3.webp" },
    { title: "Geometric Ceiling Art", category: "Modern", image: "assets/p4.webp" },
    { title: "Royal Suite POP", category: "Luxury", image: "assets/p5.jpeg" },
    { title: "Office Cabin Renovation", category: "Commercial", image: "assets/p6.webp" }
];

function initPortfolio() {
    filterPortfolio('all');
}

function filterPortfolio(filter) {
    const container = document.getElementById('portfolio-container');
    if (!container) return;

    container.innerHTML = '';
    const filtered = filter === 'all' ? portfolioData : portfolioData.filter(item => item.category === filter);

    filtered.forEach(item => {
        const div = document.createElement('div');
        div.className = 'portfolio-item';
        div.innerHTML = `
            <img src="${item.image}" alt="${item.title}" style="width: 100%; height: 100%; object-fit: cover;">
            <div class="portfolio-overlay">
                <h3>${item.title}</h3>
                <p>${item.category}</p>
                <a href="quote.html" class="btn btn-primary" style="margin-top: 15px; padding: 8px 15px; font-size: 0.9rem;">Enquire Now</a>
            </div>
        `;
        container.appendChild(div);
    });
}
