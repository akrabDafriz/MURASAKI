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
)