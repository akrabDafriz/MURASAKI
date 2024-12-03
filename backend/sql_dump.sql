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
    chest_strength int,
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

-- user_id, arm_strength, back_strength, chest_strength, foot_agility, leg_speed, heart_vitality, body_flexibility, core_stability

CREATE TABLE workout_aspect_mapping (
    workout_name VARCHAR(50) NOT NULL,
    aspect VARCHAR(50) NOT NULL,
    increment INT DEFAULT 1,
    PRIMARY KEY (workout_name, aspect)
);

INSERT INTO workout_aspect_mapping (workout_name, aspect, increment) VALUES
-- Sports
('Running', 'leg_speed', 3),
('Running', 'heart_vitality', 4),
('Sprinting', 'leg_speed', 4),
('Sprinting', 'foot_agility', 3),
('Cycling', 'heart_vitality', 4),
('Cycling', 'leg_speed', 3),
('Swimming', 'heart_vitality', 4),
('Swimming', 'body_flexibility', 3),
('Ladder Drills', 'foot_agility', 4),
('Ladder Drills', 'leg_speed', 3),

-- Upper Body
('Push-ups', 'arm_strength', 2),
('Push-ups', 'chest_strength', 2),
('Pull-ups', 'arm_strength', 3),
('Pull-ups', 'back_strength', 3),
('Bench Press', 'chest_strength', 3),
('Bench Press', 'arm_strength', 2),
('Incline Bench Press', 'chest_strength', 3),
('Incline Bench Press', 'arm_strength', 2),
('Chest Fly', 'chest_strength', 3),
('Seated Row', 'back_strength', 3),
('Seated Row', 'core_stability', 2),
('Face Pulls', 'back_strength', 2),
('Face Pulls', 'arm_strength', 2),
('Lat Pulldowns', 'back_strength', 3),
('Lat Pulldowns', 'arm_strength', 2),
('Overhead Press', 'arm_strength', 3),
('Overhead Press', 'core_stability', 2),
('Bicep Curls', 'arm_strength', 3),
('Tricep Dips', 'arm_strength', 2),
('Tricep Dips', 'core_stability', 2),
('Tricep Pulls', 'arm_strength', 2),

-- Lower Body
('Jump Squats', 'leg_speed', 3),
('Jump Squats', 'core_stability', 2),
('Barbell Squats', 'leg_speed', 3),
('Barbell Squats', 'core_stability', 3),
('Romanian Deadlifts', 'back_strength', 3),
('Romanian Deadlifts', 'core_stability', 3),
('Leg Press', 'leg_speed', 3),
('Calf Raises', 'foot_agility', 3),
('Calf Raises', 'leg_speed', 2),
('Lunges', 'leg_speed', 3),
('Lunges', 'core_stability', 2),
('High Knees', 'foot_agility', 3),
('High Knees', 'leg_speed', 3),

-- Cardio and Endurance
('Treadmill Running', 'leg_speed', 3),
('Treadmill Running', 'heart_vitality', 4),
('Elliptical Training', 'heart_vitality', 3),
('Elliptical Training', 'body_flexibility', 2),
('Rowing Machine', 'back_strength', 3),
('Rowing Machine', 'heart_vitality', 3),
('Battle Ropes', 'arm_strength', 3),
('Battle Ropes', 'heart_vitality', 3),
('Burpees', 'leg_speed', 3),
('Burpees', 'heart_vitality', 3),

-- Core and Stability
('Planks', 'core_stability', 3),
('Planks', 'body_flexibility', 2),
('Side Planks', 'core_stability', 2),
('Side Planks', 'body_flexibility', 2),
('Ab Wheel Rollouts', 'core_stability', 3),
('Russian Twists', 'core_stability', 2),
('Russian Twists', 'body_flexibility', 2),
('Cable Woodchoppers', 'core_stability', 3),
('Cable Woodchoppers', 'body_flexibility', 2),
('Hanging Leg Raises', 'core_stability', 3),

-- Functional and Dynamic Movements
('Deadlifts', 'back_strength', 4),
('Deadlifts', 'core_stability', 3),
('Kettlebell Swings', 'core_stability', 3),
('Kettlebell Swings', 'back_strength', 2),
('Medicine Ball Slams', 'core_stability', 3),
('Medicine Ball Slams', 'arm_strength', 2),
('Farmers Walk', 'core_stability', 3),
('Farmers Walk', 'back_strength', 2),
('Box Jumps', 'leg_speed', 4),
('Box Jumps', 'core_stability', 3),

-- Flexibility and Recovery
('Yoga', 'body_flexibility', 4),
('Yoga', 'core_stability', 2),
('Foam Rolling', 'body_flexibility', 3),
('Dynamic Stretching', 'body_flexibility', 3),
('Dynamic Stretching', 'core_stability', 2),
('Static Stretching', 'body_flexibility', 3);
