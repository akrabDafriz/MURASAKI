CREATE TABLE account(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    username varchar(30) NOT NULL UNIQUE,
    email varchar(320) NOT NULL UNIQUE, 
    password text NOT NULL
);

CREATE TABLE stats(
    user_id uuid NOT NULL,
    strength int,
    agility int,
    vitality int,
    flexibility int,
    stability int
);

CREATE TABLE aspects(
    user_id uuid NOT NULL,
    arm_strength int,
    back_strength int, 
    foot_agility int,
    leg_speed int,
    heart_vitality int,
    body_flexibility int,
    core_stability int
);

ALTER TABLE stats ADD FOREIGN KEY (user_id) REFERENCES "account" (id);
ALTER TABLE aspects ADD FOREIGN KEY (user_id) REFERENCES "account" (id);

CREATE TABLE plans(
    user_id uuid NOT NULL,
    exercise_name varchar(40),
    deadline date
);

ALTER TABLE plans ADD FOREIGN KEY (user_id) REFERENCES "account" (id);

-- user_id, arm_strength, back_strength, foot_agility, leg_speed, heart_vitality, body_flexibility, core_stability