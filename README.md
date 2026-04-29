# Shri Anand Construction Website

A modern, premium, high-converting website for a construction company.

## Features
- **Dark Luxury Theme**: Black, Gold, and White color palette.
- **Responsive Design**: Mobile-first approach with sticky "Call Now" and floating WhatsApp buttons.
- **Admin Panel**: Full control over inquiries, projects, and site content.
- **Lead Generation**: Inquiry forms that save to MongoDB/Mock DB.
- **Dynamic Content**: Easily update business details and services via the dashboard.

## Tech Stack
- **Frontend**: HTML5, CSS3, JavaScript, EJS
- **Backend**: Node.js, Express
- **Database**: MongoDB (with local Mock DB support)
- **Authentication**: Session-based admin login

## Setup Instructions

1. **Install Dependencies**:
   ```bash
   npm install
   ```

2. **Environment Configuration**:
   Create a `.env` file in the root directory (optional, defaults are provided in `server.js`):
   ```env
   PORT=3000
   MONGODB_URI=mongodb://localhost:27017/shri_anand_construction
   SESSION_SECRET=your_secret_key
   ADMIN_EMAIL=admin@example.com
   ADMIN_PASSWORD=admin123
   USE_MOCK_DB=true # Set to false to use real MongoDB
   ```

3. **Run the Application**:
   ```bash
   npm start
   ```
   The website will be available at `http://localhost:3000`.

4. **Admin Panel**:
   - Access at `http://localhost:3000/admin/login`.
   - Default credentials (if not set in .env):
     - **Email**: admin@example.com
     - **Password**: admin123

## Project Structure
- `server.js`: Main entry point and server configuration.
- `models/`: Database schema and mock data logic.
- `routes/`: Express routes for frontend and admin panel.
- `controllers/`: Logic for admin actions.
- `views/`: EJS templates for all pages.
- `public/`: Static assets (CSS, JS, Images).
- `uploads/`: Directory for uploaded project images.
