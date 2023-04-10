create table avatars
(
    id         bigint primary key references users (id),
    avatar     bytea,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into avatars (id, avatar)
values (1, pg_read_binary_file(current_setting('data_directory') || '/bob.jpg')::bytea),
       (2, pg_read_binary_file(current_setting('data_directory') || '/john.jpg')::bytea),
       (3, pg_read_binary_file(current_setting('data_directory') || '/artur.jpg')::bytea),
       (4, null),
       (5, null),
       (6, null);
