const pool = require('../../db');

// async function increaseStrength(){

// }

async function increaseStats(req_body){
    const { user_id, workout_type, sets_number } = req_body;

    let stats_up = 2*sets_number;

    
    const result = await pool.query(
        `UPDATE stats SET ${workout_type} = ${workout_type} + $1 WHERE user_id = $2`, [stats_up, user_id] 
    );
    
    return result.rows[0];
}

async function getStats(req_body){
    const {user_id} = req_body;

    let result = await pool.query(
        'SELECT * FROM stats WHERE user_id = $1', [user_id]
    );

    if(result.rows.length == 0){
        await pool.query(
            'INSERT INTO stats(user_id, strength, agility, vitality, flexibility, stability) VALUES($1, 0, 0, 0, 0, 0)', [user_id]
        );

        result = await pool.query(
            'SELECT * FROM stats WHERE user_id = $1', [user_id]
        );
    }
    return result.rows[0];
}
// c3c0d041-26f3-4fd6-a8cb-064c43385ece
module.exports = {
    increaseStats,
    getStats
};