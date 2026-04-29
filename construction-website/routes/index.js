const express = require('express');
const router = express.Router();
const { Inquiry, Project, Content, Review } = require('../models/db');

// Helper to get all content settings
async function getContent() {
    const content = await Content.find({});
    const settings = {};
    content.forEach(item => {
        settings[item.key] = item.value;
    });
    return settings;
}

router.get('/', async (req, res) => {
    const settings = await getContent();
    const projects = await Project.find({}).limit(3).sort({ createdAt: -1 });
    const reviews = await Review.find({}).sort({ createdAt: -1 });
    res.render('index', { title: 'Home', ...settings, projects, reviews });
});

router.get('/about', async (req, res) => {
    const settings = await getContent();
    res.render('about', { title: 'About Us', ...settings });
});

router.get('/services', async (req, res) => {
    const settings = await getContent();
    res.render('services', { title: 'Our Services', ...settings });
});

router.get('/projects', async (req, res) => {
    const settings = await getContent();
    const projects = await Project.find({}).sort({ createdAt: -1 });
    res.render('projects', { title: 'Our Projects', ...settings, projects });
});

router.get('/contact', async (req, res) => {
    const settings = await getContent();
    res.render('contact', { title: 'Contact Us', ...settings });
});

router.post('/inquiry', async (req, res) => {
    try {
        const { name, phone, projectType, budget, message } = req.body;
        const newInquiry = new Inquiry({ name, phone, projectType, budget, message });
        await newInquiry.save();
        res.send("<script>alert('Thank you! Your inquiry has been received. We will contact you soon.'); window.location.href='/';</script>");
    } catch (err) {
        res.status(500).send("Error saving inquiry");
    }
});

module.exports = router;
