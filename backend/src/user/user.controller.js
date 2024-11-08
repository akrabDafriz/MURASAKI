const userService = require('./user.service.js');   

const registerUser = async (req, res) => {
    const userDetails = req.body;
    try {
        const userId = await userService.registerUser(userDetails);
        res.status(201).json({ 
            success: true,
            message: "Register success",
            payload: userId 
        });
    } catch (error) {
        //console.log(error);
        res.status(500).json(error);
    }
};

const loginUser = async (req, res) => {
    const userDetails = req.body;
    try {
        const user = await userService.loginUser(userDetails);
        // Generate a token or start a session
        if(user.id === -1){
            return res.status(401).json({
                success: false,
                message: "No email found",
                payload: user
            });
        }
        if(user.id === -2){ 
            return res.status(401).json({ 
                success: false,
                message: "Wrong Password", 
                payload: user
            });
        }
        //console.log(user);
        res.status(200).json({
            success: true,
            message: "Logged in succesfully",
            payload: user
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const editProfile = async(req, res) => {
    try{
        result = await userService.editProfile(req.body);
        res.status(200).json({
            success: true,
            message: "Profile updated",
            payload: result
        });
    }catch(error){
        console.log(error);
        res.status(500).json(error);
    }
}

module.exports = {
    registerUser,
    loginUser,
    editProfile
};

