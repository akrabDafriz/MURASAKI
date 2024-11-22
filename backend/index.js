const express = require("express");
const bodyParser = require("body-parser");
const userRoutes = require("./src/user/user.routes");
const { Pool } = require('pg'); // PostgreSQL client
require('dotenv').config();

const app = express(); // Initialize Express app
const port = 7000;

// Middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Database connection pool
const pool = new Pool({
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    database: process.env.DB_NAME,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    ssl: { rejectUnauthorized: false } // Neon requires SSL
});

// Test the database connection
pool.query('SELECT NOW()', (err, res) => {
    if (err) {
        console.error('Database connection failed:', err.stack);
    } else {
        console.log('Connected to Neon DB! Current time:', res.rows[0].now);
    }
});

// Routes
app.use('/user', userRoutes);

// Start the server
app.listen(port, () => {
    console.log("🚀 Server is running and listening on port", port);
});

// Export the pool for use in other modules
module.exports = pool;