const statsService = require('./stats.service.js');   

const increaseStats = async(req, res) => {
    try{
        console.log(req.body);
        result = await statsService.increaseStats(req.body);
        res.status(200).json({
            success: true,
            message: "Stats Increased Successfully",
            payload: result
        });
    }catch(error){
        // console.log("body:");
        // console.log(req.body);
        console.log(error);
        res.status(500).json(error);
    }
}

const getStats = async(req, res) => {
    try{
        result = await statsService.getStats(req.body);
        res.status(200).json({
            success: true,
            message: "Stats retrieved successfully",
            payload: result
        });
    }catch(error){
        console.log(error);
        res.status(500).json(error);
    }
}

module.exports = {
    increaseStats,
    getStats
};
