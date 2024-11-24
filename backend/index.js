const express = require("express");
const bodyParser = require("body-parser");

const userRoutes = require("./src/user/user.routes"); // Import routes
const statsRoutes = require("./src/stats/stats.routes");

require('dotenv').config();

const app = express(); // Initialize Express app
const port = 7000;

// Middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Routes
app.use('/user', userRoutes);
app.use('/stats', statsRoutes);

// Start the server
app.listen(port, () => {
    console.log("🚀 Server is running and listening on port", port);
});
