--------------------------------------------------------------------------------------
-- Project	       : Expense Tracker
--------------------------------------------------------------------------------------
USE expense_db;
--------------------------------------------------------
--  expense_db
--------------------------------------------------------
-- insert user
insert into user values(null, 'John Doe', 'johndoe6@gmail.com', 'john', 'Barcelona');
select * from user;

--------------------------------------------------------
-- insert expense

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Food', '2023-07-10', 50, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Transportation', '2023-07-15', 30, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Utilities', '2023-07-17', 100, true);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Entertainment', '2023-07-20', 20, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Shopping', '2023-07-21', 70, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Healthcare', '2023-07-24', 60, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Education', '2023-07-26', 150, true);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Travel', '2023-07-27', 200, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Rent', '2023-07-29', 40, true);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (1, 'Miscellaneous', '2023-08-01', 25, false);




INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Food', '2023-07-10', 50, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Transportation', '2023-07-15', 30, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Utilities', '2023-07-17', 100, true);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Entertainment', '2023-07-20', 20, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Shopping', '2023-07-21', 70, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Healthcare', '2023-06-24', 60, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Education', '2023-08-26', 150, true);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Travel', '2023-07-27', 200, false);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Rent', '2023-08-29', 40, true);

INSERT INTO expense (user_id, category, initial_date, amount, recurring)
VALUES (2, 'Miscellaneous', '2023-08-01', 25, false);

select * from expense
where user_id = 2 and initial_date >= curdate()
limit 5;

select * from expense 
where user_id = 2 and initial_date >= CURDATE()
order by initial_date
limit 5;

--------------------------------------------------------
-- insert monthlyBudget
insert into monthly_budget values(null, 1, 1, "January", 1000);

-------------------------------------------------------
-- insert yearly_budget
insert into yearly_budget values(null, 1, 2023, 12000);

SELECT y.user_id, m.month, y.year, m.monthly_goal, y.yearly_goal
FROM yearly_budget y
INNER JOIN monthly_budget m ON y.yearly_id = m.yearly_id
WHERE y.user_id = m.user_id;

