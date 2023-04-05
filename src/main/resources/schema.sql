CREATE TABLE IF NOT EXISTS role (
    id UUID DEFAULT gen_random_uuid() NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id UUID DEFAULT gen_random_uuid() NOT NULL,
    email VARCHAR(255) UNIQUE,
    name VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_role (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) references role,
    CONSTRAINT fk_user FOREIGN KEY (user_id) references users
);


INSERT INTO users (id, email, name, password, username)
SELECT '00000000-0000-0000-0000-000000000000', 'root', 'root', 'root', 'root'
WHERE NOT EXISTS (SELECT * FROM users 
                WHERE id = '00000000-0000-0000-0000-000000000000'
                AND name = 'root');

INSERT INTO role (id, name)
SELECT '00000000-0000-0000-0000-000000000000', 'admin'
WHERE NOT EXISTS (SELECT * FROM role 
                WHERE id = '00000000-0000-0000-0000-000000000000'
                AND name = 'admin');

INSERT INTO user_role (user_id, role_id)
SELECT '00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000'
WHERE NOT EXISTS (SELECT * FROM user_role 
                WHERE user_id = '00000000-0000-0000-0000-000000000000'
                AND role_id = '00000000-0000-0000-0000-000000000000');