const express = require('express');
const router = express.Router();
const statsController = require('./stats.controller');

router.post('/increase', statsController.increaseStats);
router.get('/', statsController.getStats);
router.get('/aspects', statsController.getAspects);

module.exports = router;    