# Project M.U.R.A.S.A.K.I. - README

## Overview
**M.U.R.A.S.A.K.I.** is a gamified mobile application designed to encourage exercise through an engaging and interactive user experience. The app integrates features such as exercise planning, progress tracking, and personalized avatars to motivate users and enhance their fitness journey.

---

## Features
- **User Accounts**: Secure sign-up and login functionality.
- **Exercise Planning**: Create and manage personalized exercise plans.
- **Progress Tracking**: Record and track your workout progress.
- **Gamified Experience**: Level up and view stats through interactive avatars.
- **Responsive UI/UX**: Optimized for various screen sizes and devices.

---

## Technologies Used
### Frontend:
- **Android Studio**: Primary development environment.
- **XML Layouts**: For designing pages such as Home, Plan Input, Progress Input, and Aspects.
- **Glide & Lottie**: For image rendering and animations.
- **Retrofit**: For handling REST API calls.

### Backend:
- **Node.js**: Server-side runtime.
- **Express.js**: Framework for creating API endpoints.
- **Postgres**: Database for storing user data, exercise plans, and stats.

---

## Prerequisites
- **Software**:
  - Android Studio
  - Node.js (v14 or higher)
  - Postgres (v13 or higher)
  
- **Skills**:
  - Android Development (Java/Kotlin).
  - Backend Development (Node.js & Express).
  - Database Management (SQL).

---

## Installation
### Backend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/murasaki.git
   cd murasaki/backend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Set up the database:
   - Create a Postgres database and configure the connection details in `.env`.
   - Example `.env` file:
     ```env
     DB_HOST=localhost
     DB_PORT=5432
     DB_USER=your_username
     DB_PASSWORD=your_password
     DB_NAME=murasaki_db
     ```
4. Start the server:
   ```bash
   npm start
   ```

### Frontend Setup
1. Open the `murasaki/frontend` folder in Android Studio.
2. Connect an emulator or device.
3. Build and run the application.

---

## Usage
### 1. Getting Started
- **Create an Account**:
  - Open the app and sign up by providing a username, email, and password.
- **Log in**:
  - Enter your credentials on the Login Page and access your dashboard.

### 2. Planning Exercises
- Navigate to the **Plan Page**.
- Click **Add Exercise** and select up to 7 categories.
- View your exercise list on the Plan Page.

### 3. Tracking Progress
- Navigate to the **Progress Input Page**.
- Select the category and workout type to record your progress.
- Updated stats will appear on the Home Page and Aspects Page.

---

## Challenges & Solutions
- **Null Pointer Exception**:
  - Added validation to ensure all required data is initialized before use.
- **Visual Layout Issues**:
  - Used ConstraintLayout and custom lines to display avatar attributes effectively.

---

## Future Potential Improvements
- Enhanced gamification elements, including competitive leaderboards.
- Integration of wearable device support for real-time tracking.
- Advanced analytics and AI-driven exercise suggestions.

---

