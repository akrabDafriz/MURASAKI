const express = require('express');
const router = express.Router();
const statsController = require('./stats.controller');

router.post('/increase', statsController.increaseStats);
router.post('/', statsController.getStats);
router.post('/aspects', statsController.getAspects);

module.exports = router;    