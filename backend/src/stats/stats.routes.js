const express = require('express');
const router = express.Router();
const statsController = require('./stats.controller');

router.post('/increase', statsController.increaseStats);
router.get('/', statsController.getStats);

module.exports = router;    