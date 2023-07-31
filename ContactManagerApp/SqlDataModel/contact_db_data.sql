--------------------------------------------------------------------------------------
-- Project	       : Contact Manager
--------------------------------------------------------------------------------------
USE contact_db;
--------------------------------------------------------
--  contact_db
--------------------------------------------------------
-- insert user
insert into user values(null, 'John', 'Doe', 'johndoe6@gmail.com', 'john', 'Barcelona');
select * from user;

--------------------------------------------------------
-- insert contacts

insert into contacts values(null, 1, "John Doe", "johndoe6@gmail.com", "123-123-1234", "");
insert into contacts values(null, 1, "Sean Bryson", "sean@gmail.com", "123-123-1235", "");
insert into contacts values(null, 1, "Vikram Verma", "vik@gmail.com", "123-123-1236", "");
insert into contacts values(null, 1, "Bill Bob", "bill1@gmail.com", "123-123-1237", "");
insert into contacts values(null, 1, "Lionel Messi", "goat@gmail.com", "123-123-1238", "");
insert into contacts values(null, 1, "Tony Stark", "ironman@gmail.com", "123-123-1239", "");

select * from contacts;
-- where user_id = 1;
-- order by name;


--------------------------------------------------------
-- insert user_movie
