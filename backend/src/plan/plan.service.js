const pool = require('../../db');

async function addPlan(req_body){
    const { user_id, exercise, deadline } = req_body; //deadline : YYYY-MM-DD (numbers only)

    const result = await pool.query(
        `INSERT INTO plans(user_id, exercise_name, deadline) VALUES($1, $2, $3)`, [user_id, exercise, deadline]
    );

    return result.rows[0];
}

async function getPlans(req_body){
    const { user_id } = req_body;

    let result = await pool.query(
        `SELECT * FROM plans WHERE user_id = $1`, [user_id]
    )
    
    if(result.rows.length == 0){
        return -1;
    }

    return result.rows;
}

// c3c0d041-26f3-4fd6-a8cb-064c43385ece
module.exports = {
    addPlan,
    getPlans
};