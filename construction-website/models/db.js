const mongoose = require('mongoose');

const InquirySchema = new mongoose.Schema({
    name: { type: String, required: true },
    phone: { type: String, required: true },
    projectType: { type: String, required: true },
    budget: { type: String },
    message: { type: String },
    status: { type: String, default: 'Pending' }, // Pending, Contacted
    createdAt: { type: Date, default: Date.now }
});

const ProjectSchema = new mongoose.Schema({
    title: { type: String, required: true },
    description: { type: String },
    beforeImage: { type: String },
    afterImage: { type: String },
    category: { type: String },
    createdAt: { type: Date, default: Date.now }
});

const ContentSchema = new mongoose.Schema({
    key: { type: String, required: true, unique: true },
    value: { type: mongoose.Schema.Types.Mixed, required: true }
});

const ReviewSchema = new mongoose.Schema({
    name: { type: String, required: true },
    rating: { type: Number, default: 5 },
    comment: { type: String, required: true },
    createdAt: { type: Date, default: Date.now }
});

const AdminSchema = new mongoose.Schema({
    email: { type: String, required: true, unique: true },
    password: { type: String, required: true }
});

// Mock In-Memory Storage for Testing
const mockData = {
    inquiries: [],
    reviews: [
        { name: "Rahul S.", rating: 5, comment: "Amazing quality and on-time delivery!" },
        { name: "Priya M.", rating: 4, comment: "Great interior design work. Highly recommended." }
    ],
    projects: [
        {
            _id: "mock_proj_1",
            title: "Modern Villa Project",
            category: "Residential",
            description: "A premium luxury villa construction project in Yavatmal.",
            beforeImage: "before_example.webp",
            afterImage: "after_example.webp",
            createdAt: new Date()
        }
    ]
};

// Mock Models for environments without MongoDB
const mockModels = {
    Inquiry: {
        find: () => ({ sort: () => Promise.resolve([...mockData.inquiries].reverse()) }),
        findByIdAndUpdate: (id, data) => {
            const idx = mockData.inquiries.findIndex(i => i._id === id);
            if (idx !== -1) Object.assign(mockData.inquiries[idx], data);
            return Promise.resolve(mockData.inquiries[idx]);
        },
        findByIdAndDelete: (id) => {
            mockData.inquiries = mockData.inquiries.filter(i => i._id !== id);
            return Promise.resolve({});
        },
        save: function() {
            this._id = Date.now().toString();
            this.createdAt = new Date();
            mockData.inquiries.push(this);
            return Promise.resolve(this);
        }
    },
    Review: {
        find: () => ({ sort: () => Promise.resolve([...mockData.reviews].reverse()) }),
        findByIdAndDelete: (id) => {
            mockData.reviews = mockData.reviews.filter(r => r._id !== id);
            return Promise.resolve({});
        },
        save: function() {
            this._id = Date.now().toString();
            this.createdAt = new Date();
            mockData.reviews.push(this);
            return Promise.resolve(this);
        }
    },
    Project: {
        find: () => ({
            limit: () => ({ sort: () => Promise.resolve(mockData.projects) }),
            sort: () => Promise.resolve([...mockData.projects].reverse())
        }),
        findById: (id) => Promise.resolve(mockData.projects.find(p => p._id === id)),
        findByIdAndUpdate: (id, data) => {
            const idx = mockData.projects.findIndex(p => p._id === id);
            if (idx !== -1) Object.assign(mockData.projects[idx], data);
            return Promise.resolve(mockData.projects[idx]);
        },
        findByIdAndDelete: (id) => {
            mockData.projects = mockData.projects.filter(p => p._id !== id);
            return Promise.resolve({});
        },
        save: function() {
            this._id = Date.now().toString();
            this.createdAt = new Date();
            mockData.projects.push(this);
            return Promise.resolve(this);
        }
    },
    Content: {
        find: () => Promise.resolve([
            { key: 'businessName', value: 'Shri Anand Construction' },
            { key: 'phone', value: '+917823888641' },
            { key: 'email', value: 'info@shrianand.com' },
            { key: 'address', value: 'Yavatmal, Maharashtra' },
            { key: 'heroTitle', value: 'We Build Your Dream Home' },
            { key: 'aboutText', value: 'Leading construction company in Yavatmal with over 10 years of experience in building premium homes.' }
        ]),
        findOneAndUpdate: () => Promise.resolve({})
    }
};

let useMock = false;
if (process.env.USE_MOCK_DB === 'true') {
    useMock = true;
}

const Inquiry = useMock ? function(data){ Object.assign(this, data); this.save = mockModels.Inquiry.save; } : mongoose.model('Inquiry', InquirySchema);
const Project = useMock ? function(data){ Object.assign(this, data); this.save = mockModels.Project.save; } : mongoose.model('Project', ProjectSchema);
const Review = useMock ? function(data){ Object.assign(this, data); this.save = mockModels.Review.save; } : mongoose.model('Review', ReviewSchema);
const Content = useMock ? mockModels.Content : mongoose.model('Content', ContentSchema);
const Admin = useMock ? {} : mongoose.model('Admin', AdminSchema);

if (useMock) {
    Inquiry.find = mockModels.Inquiry.find;
    Inquiry.findByIdAndUpdate = mockModels.Inquiry.findByIdAndUpdate;
    Inquiry.findByIdAndDelete = mockModels.Inquiry.findByIdAndDelete;

    Project.find = mockModels.Project.find;
    Project.findById = mockModels.Project.findById;
    Project.findByIdAndUpdate = mockModels.Project.findByIdAndUpdate;
    Project.findByIdAndDelete = mockModels.Project.findByIdAndDelete;

    Review.find = mockModels.Review.find;
    Review.findByIdAndDelete = mockModels.Review.findByIdAndDelete;
}

module.exports = {
    Inquiry,
    Project,
    Review,
    Content,
    Admin
};
