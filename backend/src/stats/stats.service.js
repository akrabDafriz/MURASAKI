const pool = require('../../db');

async function increaseStats(req_body) {
    const { user_id, workout_name, sets_number } = req_body;

    const { rows: mappings } = await pool.query(
        `SELECT aspect, increment 
        FROM workout_aspect_mapping 
        WHERE workout_name = $1`,
        [workout_name]
    );

    if (mappings.length === 0) {
        return 'Invalid workout name or no mappings found.';
    }

    const incrementFactor = sets_number;
    let totalExpIncrement = 0;

    for (const { aspect, increment } of mappings) {
        const adjustedIncrement = increment * incrementFactor;
        totalExpIncrement += adjustedIncrement;

        await pool.query(
            `UPDATE aspects 
            SET ${aspect} = ${aspect} + $1 
            WHERE user_id = $2`,
            [adjustedIncrement, user_id]
        );

        const statsCategory = getStatsCategory(aspect);
        if (statsCategory) {
            await pool.query(
                `UPDATE stats 
                SET ${statsCategory} = ${statsCategory} + $1 
                WHERE user_id = $2`,
                [adjustedIncrement, user_id]
            );
        }
    }

    await pool.query(
        `UPDATE user_levels
        SET current_exp = current_exp + $1
        WHERE user_id = $2`,
        [totalExpIncrement, user_id]
    );

    const { rows: plans } = await pool.query(
        `SELECT * FROM plans WHERE user_id = $1 AND exercise_name = $2`,
        [user_id, workout_name]
    );

    if (plans.length !== 0) {
        const { deadline } = plans[0];
        removePlans(user_id, workout_name, deadline);
    }

    return 'Stats updated successfully.';
}

async function removePlans(user_id, workout_name, deadline) {
    const currentDate = new Date();

    // Check if the deadline has passed or the plan is fulfilled
    const isDeadlineOverdue = new Date(deadline) <= currentDate;

    if (isDeadlineOverdue) {
        await pool.query(
            `DELETE FROM plans WHERE user_id = $1 AND exercise_name = $2`,
            [user_id, workout_name]
        );
        console.log(`Plan for workout '${workout_name}' removed for user '${user_id}'.`);
    } else {
        console.log(`Plan for workout '${workout_name}' is still active.`);
    }
}

function getStatsCategory(aspect) {
    const mapping = {
        arm_strength: 'strength',
        back_strength: 'strength',
        chest_strength: 'strength',
        foot_agility: 'agility',
        leg_speed: 'agility',
        heart_vitality: 'vitality',
        body_flexibility: 'flexibility',
        core_stability: 'stability'
    };

    return mapping[aspect] || null;
}

async function getStats(req_body) {
    const { user_id } = req_body;

    let result = await pool.query(
        'SELECT * FROM stats WHERE user_id = $1', [user_id]
    );

    if (result.rows.length == 0) {
        await pool.query(
            'INSERT INTO stats(user_id, strength, agility, vitality, flexibility, stability) VALUES($1, 0, 0, 0, 0, 0)', [user_id]
        );

        result = await pool.query(
            'SELECT * FROM stats WHERE user_id = $1', [user_id]
        );
    }
    return result.rows[0];
}

async function getAspects(req_body) {
    const { user_id } = req_body;

    let result = await pool.query(
        'SELECT * FROM aspects WHERE user_id = $1', [user_id]
    );

    if (result.rows.length == 0) {
        await pool.query(
            'INSERT INTO aspects(user_id, arm_strength, back_strength, chest_strength, foot_agility, leg_speed, heart_vitality, body_flexibility, core_stability) VALUES($1, 0, 0, 0, 0, 0, 0, 0, 0)', [user_id]
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