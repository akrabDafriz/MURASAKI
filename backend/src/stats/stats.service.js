const pool = require('../../db');

// async function increaseStrength(){

// }

async function increaseStats(req_body){
    const { user_id, aspect_to_change, sets_number } = req_body;

    let stats_up = 2*sets_number;

    
    const result = await pool.query(
        `UPDATE aspects SET ${aspect_to_change} = ${aspect_to_change} + $1 WHERE user_id = $2`, [stats_up, user_id] 
    );

    if (aspect_to_change == 'arm_strength' || aspect_to_change == 'back_strength'){
        await pool.query(
            `UPDATE stats SET strength = strength + $1 WHERE user_id = $2`, [stats_up, user_id] 
        );
    }
    else if ( aspect_to_change == 'foot_agility' || aspect_to_change == 'leg_speed') {
        await pool.query(
            `UPDATE stats SET agility = agility + $1 WHERE user_id = $2`, [stats_up, user_id] 
        );
    }
    else if ( aspect_to_change == 'heart_vitality') {
        await pool.query(
            `UPDATE stats SET vitality = vitality + $1 WHERE user_id = $2`, [stats_up, user_id] 
        );
    }
    else if ( aspect_to_change == 'body_flexibility') {
        await pool.query(
            `UPDATE stats SET flexibility = flexibility + $1 WHERE user_id = $2`, [stats_up, user_id] 
        );
    }
    else if ( aspect_to_change == 'fcore_stability') {
        await pool.query(
            `UPDATE stats SET stability = stability + $1 WHERE user_id = $2`, [stats_up, user_id] 
        );
    }
    
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

async function getAspects(req_body){
    const {user_id} = req_body;

    let result = await pool.query(
        'SELECT * FROM aspects WHERE user_id = $1', [user_id]
    );

    if(result.rows.length == 0){
        await pool.query(
            'INSERT INTO aspects(user_id, arm_strength, back_strength, foot_agility, leg_speed, heart_vitality, body_flexibility, core_stability) VALUES($1, 0, 0, 0, 0, 0, 0, 0)', [user_id]
        );

        result = await pool.query(
            'SELECT * FROM aspects WHERE user_id = $1', [user_id]
        );
    }
    return result.rows[0];
}

// c3c0d041-26f3-4fd6-a8cb-064c43385ece
module.exports = {
    increaseStats,
    getStats,
    getAspects
};