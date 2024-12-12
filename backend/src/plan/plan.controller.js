const planService = require('./plan.service.js');

const addPlan = async (req, res) => {
    try {
        console.log(req.body);
        result = await planService.addPlan(req.body);
        res.status(200).json({
            success: true,
            message: "Plan added Successfully",
            payload: null
        });
    } catch (error) {
        // console.log("body:");
        // console.log(req.body);
        console.log(error);
        res.status(500).json(error);
    }
}

const getPlans = async (req, res) => {
    try {
        console.log(req.body);
        result = await planService.getPlans(req.body);
        if (result == -1) { //happens when there's no record found in the plan table that has this user's id
            res.status(200).json({
                success: true,
                message: "No plan exists for this user",
                payload: null
            });
        }
        else {
            res.status(200).json({
                success: true,
                message: "Plan retrieved successfully",
                payload: result // result is in the form of json array, consisting of each row of the plans with this user's id
            });
        }
    } catch (error) {
        console.log(error);
        res.status(500).json(error);
    }
}

module.exports = {
    getPlans,
    addPlan
};