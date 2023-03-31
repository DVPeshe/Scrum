insert into roles (name)
values ('ROLE_MANAGER'),
       ('ROLE_SUPERADMIN');

insert into users_roles (user_id, role_id)
values (5, 3),
       (2, 1),
       (4, 1),
       (4, 3),
       (4, 4),
       (6, 2),
       (6, 3)