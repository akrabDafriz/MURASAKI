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

    // Calculate the actual increment based on sets completed
    const incrementFactor = sets_number;
    let totalExpIncrement = 0; //exp implement

    // Update each aspect based on the mappings
    for (const { aspect, increment } of mappings) {
        const adjustedIncrement = increment * incrementFactor;
        totalExpIncrement += adjustedIncrement; //exp implement
        console.log("Exp increment: +="+totalExpIncrement);

        // Update the aspect in the aspects table
        await pool.query(
            `UPDATE aspects 
            SET ${aspect} = ${aspect} + $1 
            WHERE user_id = $2`,
            [adjustedIncrement, user_id]
        );

        // Update the corresponding stats category (if applicable)
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
    console.log(totalExpIncrement);

    return 'Stats updated successfully.';

    // return result.rows[0];
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