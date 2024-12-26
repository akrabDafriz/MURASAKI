const pool = require('../../db'); // Import the pool from db.js
const bcrypt = require('bcrypt');

async function hashPassword(password){
    const saltRounds = 10; 
    const hashedPassword = await bcrypt.hash(password, saltRounds);
    return hashedPassword;
}

async function checkPassword(password, hashedPassword){
    const match = await bcrypt.compare(password, hashedPassword);
    return match;
}

async function registerUser(userDetails){
    const { username, email, password } = userDetails;
    if (username.length > 30) throw new Error(`Username length cannot be more than 30`);
    if (password.length > 20) throw new Error(`Password length cannot be more than 20`);
    const emailExists = await pool.query(`SELECT * FROM account WHERE email = $1`, [email]);
    if (emailExists.rows.length !== 0) throw new Error(`Email is unavailable`);
    const hashedPassword = await hashPassword(password);
    const result = await pool.query(
        'INSERT INTO account (username, email, password) VALUES ($1, $2, $3) RETURNING id',
        [username, email, hashedPassword]
    );
    
    return result.rows[0].id;
}

async function loginUser(userDetails){
    console.log(userDetails);
    const { email, password } = userDetails;
    const result = await pool.query(
        'SELECT * FROM account WHERE email = $1',
        [email]
    );
    if (result.rows.length == 0) {
        return {
            id: -1,
            username: " ",
            email: " ",
            password: " "
        };
    }
    const user = result.rows[0];
    const isPasswordValid = await checkPassword(password, user.password);
    if (!isPasswordValid) {
        return {
            id: -2,
            username: " ",
            email: " ",
            password: " "
        };
    }
    return user;
}

async function editProfile(details){
    const {userId, username, email} = details;

    let queryText = `UPDATE account SET `
    let queryParams = [];

    if(username){
        queryText += 'username = $' + (queryParams.length + 1);
        queryParams.push(username);
    }
    if(email){
        queryText += ',email = $' + (queryParams.length + 1);
        queryParams.push(email);
    }

    queryText+= ` WHERE id = $`+ (queryParams.length + 1);
    queryParams.push(userId)
    console.log(queryText);
    console.log(queryParams);
    await pool.query(
        queryText,
        queryParams
    )
    const result = await pool.query(
        `SELECT * FROM account WHERE user_id = $1`,
        [userId]
    )
    console.log(result.rows[0]);
    return result.rows[0];
}

async function getLevel(req_body){
    const {user_id} = req_body;

    let result = await pool.query(
        `SELECT * FROM user_levels WHERE user_id = $1`, //consider using this instead later if it dont work SELECT level, current_exp, max_exp FROM user_levels WHERE id = $1;
        [user_id]
    );

    if (result.rows.length == 0) {
        await pool.query(
            //'INSERT INTO user_levels(user_id, level, current_exp, max_exp) VALUES($1, 1, 0, 300)', 
            'INSERT INTO user_levels(user_id) VALUES($1)', 
            [user_id]
        );

        return {
            user_id,
            level: 1,
            current_exp: 0,
            max_exp: 300,
        };
    }
    
    let {level, current_exp, max_exp} = result.rows[0];

    if(current_exp >= max_exp) {
        level++;
        current_exp -= max_exp;
        max_exp += 50;

        await pool.query(
            `UPDATE user_levels
            SET level = $1, current_exp = $2, max_exp = $3
            WHERE user_id = $4`,
            [level, current_exp, max_exp, user_id]
        );

        return {
            user_id,
            level,
            current_exp,
            max_exp,
        };
    }

    return {
        user_id,
        level,
        current_exp,
        max_exp,
    };
    
    //return result; //result = {user_id, level, current_exp, max_exp}
}

module.exports = {
    registerUser,
    loginUser,
    editProfile,
    getLevel
};