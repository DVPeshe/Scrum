begin;

alter table users
    add full_name text not null default '';

create table avatars
(
    id         bigserial primary key,
    user_id    bigint references users (id) not null,
    avatar     bytea,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into avatars (user_id, avatar)
values (1, pg_read_binary_file(current_setting('data_directory') || '/bob.jpg')::bytea),
       (2, pg_read_binary_file(current_setting('data_directory') || '/john.jpg')::bytea),
       (3, pg_read_binary_file(current_setting('data_directory') || '/artur.jpg')::bytea);

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