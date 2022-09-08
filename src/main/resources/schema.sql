CREATE TABLE IF NOT EXISTS authority (
    id INT PRIMARY KEY AUTO_INCREMENT,
    authority VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS user_authorities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    authority_id INT NOT NULL,
    user_id INT NOT NULL
);

ALTER TABLE user_authorities
    ADD FOREIGN KEY (authority_id) REFERENCES authority(id);

ALTER TABLE user_authorities
    ADD FOREIGN KEY (user_id) REFERENCES app_user(id);

CREATE TABLE IF NOT EXISTS blog_post (
    id INT PRIMARY KEY AUTO_INCREMENT,
    uuid UUID NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    subtitle VARCHAR(200),
    content BLOB NOT NULL,
    creation_timestamp TIMESTAMP NOT NULL,
    last_edited_timestamp TIMESTAMP
);

ALTER TABLE blog_post
    ADD FOREIGN KEY (user_id) REFERENCES app_user(id);