CREATE TABLE account(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    username varchar(30) NOT NULL UNIQUE,
    email varchar(320) NOT NULL UNIQUE, 
    password text NOT NULL
);

