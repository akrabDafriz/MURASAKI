const express = require('express');
const router = express.Router();
const planController = require('./plan.controller');

router.post('/add', planController.addPlan);
router.post('/', planController.getPlans);

module.exports = router;    