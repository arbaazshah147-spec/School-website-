const express = require('express');
const router = express.Router();
const adminController = require('../controllers/adminController');
const multer = require('multer');
const path = require('path');

// Ensure uploads directory exists
const fs = require('fs');
const uploadDir = './public/uploads/';
if (!fs.existsSync(uploadDir)){
    fs.mkdirSync(uploadDir, { recursive: true });
}

// Multer Setup for Image Uploads
const storage = multer.diskStorage({
    destination: uploadDir,
    filename: function(req, file, cb) {
        cb(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname));
    }
});
const upload = multer({
    storage: storage,
    fileFilter: (req, file, cb) => {
        if (file.mimetype.startsWith('image/')) {
            cb(null, true);
        } else {
            cb(new Error('Only images are allowed!'), false);
        }
    }
});

// Middleware to check if admin is logged in
const isAdmin = (req, res, next) => {
    if (req.session.isAdmin) {
        return next();
    }
    res.redirect('/admin/login');
};

// Admin Auth Routes
router.get('/login', (req, res) => res.render('admin/login', { title: 'Admin Login' }));
router.post('/login', adminController.login);
router.get('/logout', (req, res) => {
    req.session.destroy();
    res.redirect('/admin/login');
});

// Protected Admin Routes
router.get('/dashboard', isAdmin, adminController.getDashboard);
router.post('/inquiry/contacted/:id', isAdmin, adminController.markInquiryContacted);
router.post('/inquiry/delete/:id', isAdmin, adminController.deleteInquiry);

router.get('/projects', isAdmin, adminController.getProjects);
router.post('/projects/add', isAdmin, upload.fields([{ name: 'beforeImage' }, { name: 'afterImage' }]), adminController.addProject);
router.get('/projects/edit/:id', isAdmin, adminController.getEditProject);
router.post('/projects/update/:id', isAdmin, upload.fields([{ name: 'beforeImage', maxCount: 1 }, { name: 'afterImage', maxCount: 1 }]), adminController.updateProject);
router.post('/projects/delete/:id', isAdmin, adminController.deleteProject);

router.get('/content', isAdmin, adminController.getContent);
router.post('/content/update', isAdmin, adminController.updateContent);

// Reviews
router.get('/reviews', isAdmin, adminController.getReviews);
router.post('/reviews', isAdmin, adminController.addReview);
router.post('/reviews/delete/:id', isAdmin, adminController.deleteReview);

module.exports = router;
