const { Inquiry, Project, Content, Review } = require('../models/db');
const bcrypt = require('bcryptjs');

exports.login = async (req, res) => {
    const { email, password } = req.body;
    const adminEmail = process.env.ADMIN_EMAIL || 'admin@example.com';
    const adminPass = process.env.ADMIN_PASSWORD || 'admin123';

    // Simple comparison for this project structure, but ensuring we check both
    if (email === adminEmail && password === adminPass) {
        req.session.isAdmin = true;
        return res.redirect('/admin/dashboard');
    }
    res.send("<script>alert('Invalid Credentials'); window.location.href='/admin/login';</script>");
};

exports.getDashboard = async (req, res) => {
    const inquiries = await Inquiry.find({}).sort({ createdAt: -1 });
    res.render('admin/dashboard', { title: 'Inquiry Management', inquiries });
};

exports.markInquiryContacted = async (req, res) => {
    await Inquiry.findByIdAndUpdate(req.params.id, { status: 'Contacted' });
    res.redirect('/admin/dashboard');
};

exports.deleteInquiry = async (req, res) => {
    await Inquiry.findByIdAndDelete(req.params.id);
    res.redirect('/admin/dashboard');
};

exports.getProjects = async (req, res) => {
    const projects = await Project.find({}).sort({ createdAt: -1 });
    res.render('admin/projects', { title: 'Project Management', projects });
};

exports.getEditProject = async (req, res) => {
    const project = await Project.findById(req.params.id);
    res.render('admin/edit_project', { title: 'Edit Project', project });
};

exports.updateProject = async (req, res) => {
    const { title, description, category } = req.body;
    const updateData = { title, description, category };

    if (req.files['afterImage']) {
        updateData.afterImage = req.files['afterImage'][0].filename;
    }
    if (req.files['beforeImage']) {
        updateData.beforeImage = req.files['beforeImage'][0].filename;
    }

    await Project.findByIdAndUpdate(req.params.id, updateData);
    res.redirect('/admin/projects');
};

exports.addProject = async (req, res) => {
    const { title, description, category } = req.body;
    const afterImage = req.files['afterImage'] ? req.files['afterImage'][0].filename : '';
    const beforeImage = req.files['beforeImage'] ? req.files['beforeImage'][0].filename : '';

    const newProject = new Project({ title, description, category, afterImage, beforeImage });
    await newProject.save();
    res.redirect('/admin/projects');
};

exports.deleteProject = async (req, res) => {
    await Project.findByIdAndDelete(req.params.id);
    res.redirect('/admin/projects');
};

exports.getContent = async (req, res) => {
    const content = await Content.find({});
    res.render('admin/content', { title: 'Content Management', content });
};

exports.updateContent = async (req, res) => {
    const updates = req.body;
    for (let key in updates) {
        await Content.findOneAndUpdate({ key }, { value: updates[key] }, { upsert: true });
    }
    res.redirect('/admin/content');
};

exports.getReviews = async (req, res) => {
    const reviews = await Review.find({}).sort({ createdAt: -1 });
    res.render('admin/reviews', { title: 'Review Management', reviews });
};

exports.addReview = async (req, res) => {
    const { name, rating, comment } = req.body;
    const newReview = new Review({ name, rating, comment });
    await newReview.save();
    res.redirect('/admin/reviews');
};

exports.deleteReview = async (req, res) => {
    await Review.findByIdAndDelete(req.params.id);
    res.redirect('/admin/reviews');
};
