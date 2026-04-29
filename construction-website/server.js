require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const session = require('express-session');
const path = require('path');
const { Content } = require('./models/db');

const app = express();

// Connect to MongoDB only if not mocking
if (process.env.USE_MOCK_DB !== 'true') {
    mongoose.connect(process.env.MONGO_URI || 'mongodb://localhost:27017/construction_db')
        .then(() => console.log('MongoDB Connected'))
        .catch(err => console.log('MongoDB Connection Error:', err));
} else {
    console.log('Using Mock Database');
}

// Middleware
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(session({
    secret: process.env.SESSION_SECRET || 'secret',
    resave: false,
    saveUninitialized: true
}));

// Initialize default content if not exists
async function initContent() {
    if (process.env.USE_MOCK_DB === 'true') return;
    const defaults = [
        { key: 'businessName', value: 'Shri Anand Construction' },
        { key: 'phone', value: '+917823888641' },
        { key: 'email', value: 'info@shrianand.com' },
        { key: 'address', value: 'Yavatmal, Maharashtra' },
        { key: 'heroTitle', value: 'We Build Your Dream Home' },
        { key: 'aboutText', value: 'Leading construction company in Yavatmal with over 10 years of experience in building premium homes.' }
    ];

    try {
        for (const item of defaults) {
            await Content.findOneAndUpdate({ key: item.key }, item, { upsert: true });
        }
    } catch (e) {
        console.error("Content initialization failed:", e.message);
    }
}
initContent();

// Routes
const publicRoutes = require('./routes/index');
const adminRoutes = require('./routes/admin');

app.use('/', publicRoutes);
app.use('/admin', adminRoutes);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});
