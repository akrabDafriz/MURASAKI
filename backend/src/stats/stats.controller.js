const statsService = require('./stats.service.js');

const increaseStats = async (req, res) => {
    try {
        console.log(req.body); // body nya json: {user_id (string), workout_name (string), sets_number(integer)}
        result = await statsService.increaseStats(req.body);
        res.status(200).json({
            success: true,
            message: result, //"Stats Increased Successfully",
            payload: null
            //tadinya return user_id, arm_strength, back_strength, foot_agility, leg_speed, heart_vitality, body_flexibility, core_stability
        });
    } catch (error) {
        // console.log("body:");
        // console.log(req.body);
        console.log(error);
        res.status(500).json(error);
    }
}

const getStats = async (req, res) => {
    try {
        console.log(req.body);
        console.log("getStats success");
        result = await statsService.getStats(req.body);
        res.status(200).json({
            success: true,
            message: "Stats retrieved successfully",
            payload: result
        });
        console.log(result);
    } catch (error) {
        console.log(error);
        res.status(500).json(error);
    }
}

const getAspects = async (req, res) => {
    try {
        console.log(req.body);
        result = await statsService.getAspects(req.body);
        res.status(200).json({
            success: true,
            message: "Aspects retrieved successfully",
            payload: result
        });
        console.log(result);
    } catch (error) {
        console.log(error);
        res.status(500).json(error);
    }
}

module.exports = {
    increaseStats,
    getStats,
    getAspects
};