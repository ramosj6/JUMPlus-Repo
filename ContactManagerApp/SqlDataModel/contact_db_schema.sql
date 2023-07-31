------------------------------------------------------------------------------------
-- Project     : Contact Manager 
------------------------------------------------------------------------------------
-- initialize the database for contact manager database
------------------------------------------------------------------------------------

DROP DATABASE IF EXISTS contact_db;

CREATE DATABASE contact_db;

USE contact_db;

------------------------------------------------------------------------------------
-- execute the following statements to create tables
------------------------------------------------------------------------------------
-- 
CREATE TABLE user (
    user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(30) NOT NULL UNIQUE,
    phone_number VARCHAR(50) NOT NULL UNIQUE,
    address VARCHAR(255)
);

CREATE TABLE contacts (
    contact_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(50),
    address VARCHAR(255),
    FOREIGN KEY (user_id)
        REFERENCES user (user_id)
);

CREATE TABLE inbox (
    inbox_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    FOREIGN KEY (user_id)
        REFERENCES user (user_id)
);

CREATE TABLE message (
    message_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    subject VARCHAR(100),
    content TEXT NOT NULL,
    sent_date_time DATE NOT NULL,
    inbox_id INT NOT NULL,
    FOREIGN KEY (sender_id)
        REFERENCES user (user_id),
    FOREIGN KEY (inbox_id)
        REFERENCES inbox (inbox_id)
);

select * from user;