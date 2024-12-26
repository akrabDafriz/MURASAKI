const express = require('express');
const router = express.Router();
const userController = require('./user.controller');

router.post('/register', userController.registerUser);
router.post('/login', userController.loginUser);
router.put('/update', userController.editProfile);
router.post('/level', userController.getLevel); //get level 

module.exports = router;    