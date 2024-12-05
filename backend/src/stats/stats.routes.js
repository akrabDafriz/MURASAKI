const express = require('express');
const router = express.Router();
const statsController = require('./stats.controller');

router.post('/increase', statsController.increaseStats);
router.post('/', statsController.getStats);
router.get('/aspects', statsController.getAspects);

module.exports = router;    