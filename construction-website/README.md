# Shri Anand Construction Website

A modern, premium, and high-converting website for Shri Anand Construction (Yavatmal, Maharashtra).

## Features

- **Premium Design:** Black, Gold, and White "Dark Luxury" theme.
- **Responsive:** Mobile-first design optimized for Android and iOS users.
- **Lead Generation:** Inquiry form, floating WhatsApp button, and mobile fixed "Call Now" button.
- **Project Gallery:** Grid layout with Before/After image support.
- **Admin Panel:** Secure dashboard to manage inquiries, projects, reviews, and site content.
- **Mock Mode:** Can run without MongoDB using an in-memory database for testing.

## Tech Stack

- **Frontend:** HTML5, CSS3 (with FontAwesome and Google Fonts), JavaScript, EJS.
- **Backend:** Node.js with Express.
- **Database:** MongoDB (via Mongoose) or Mock In-Memory.

## Setup Instructions

1. **Prerequisites:**
   - Node.js installed.
   - MongoDB installed (or use Mock mode).

2. **Installation:**
   ```bash
   cd construction-website
   npm install
   ```

3. **Environment Variables:**
   Create a `.env` file in the `construction-website` directory:
   ```env
   PORT=3000
   MONGO_URI=mongodb://localhost:27014/construction_db
   USE_MOCK_DB=true
   ADMIN_EMAIL=admin@shrianand.com
   ADMIN_PASSWORD=admin123
   SESSION_SECRET=your_secret_key
   ```
   *Set `USE_MOCK_DB=false` and provide a valid `MONGO_URI` for production.*

4. **Running the App:**
   ```bash
   npm start
   ```
   The website will be available at `http://localhost:3000`.

## Admin Access

- **URL:** `http://localhost:3000/admin/login`
- **Default Credentials:**
  - Email: `admin@shrianand.com`
  - Password: `admin123`

## Directory Structure

- `/public`: Static assets (CSS, Images, JS).
- `/views`: EJS templates for public and admin pages.
- `/routes`: Express route definitions.
- `/controllers`: Business logic for handling requests.
- `/models`: Database schemas and mock logic.
- `/middleware`: Authentication and upload handling.
- `/uploads`: Directory for uploaded project images.
