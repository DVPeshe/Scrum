begin;

alter table users
    add full_name varchar(255) not null default '';

update users
set full_name = 'Боб'
where username = 'bob';

update users
set full_name = 'Джон'
where username = 'john';

update users
set full_name = 'Артур'
where username = 'artur';

update users
set full_name = 'Итан'
where username = 'ethan';

update users
set full_name = 'Максим'
where username = 'max';

update users
set full_name = 'Морган'
where username = 'morgan';

commit;