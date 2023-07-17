------------------------------------------------------------------------------------
-- Project     : Expense Tracking
------------------------------------------------------------------------------------
-- initialize the database for expense tracking project
------------------------------------------------------------------------------------

drop database if exists expense_db;

CREATE DATABASE expense_db;
USE expense_db;

------------------------------------------------------------------------------------
-- execute the following statements to create tables
------------------------------------------------------------------------------------
CREATE TABLE user (
    user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    username VARCHAR(20) UNIQUE,
    password VARCHAR(20)
);


CREATE TABLE expense (
    expense_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category VARCHAR(100),
    initial_date DATE NOT NULL,
    amount DOUBLE NOT NULL,
    recurring BOOLEAN NOT NULL,
    FOREIGN KEY (user_id)
        REFERENCES user (user_id)
);

CREATE TABLE yearly_budget (
    yearly_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    year INT NOT NULL,
    yearly_goal INT,
    FOREIGN KEY (user_id)
        REFERENCES user (user_id)
);

CREATE TABLE monthly_budget (
    monthly_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    yearly_id INT,
    month VARCHAR(50),
    monthly_goal INT,
    FOREIGN KEY (user_id)
        REFERENCES user (user_id),
    FOREIGN KEY (yearly_id)
        REFERENCES yearly_budget (yearly_id)
);

select * from user;

-- delete from user where user_id = 3;