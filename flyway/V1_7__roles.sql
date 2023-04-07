begin;

alter table roles
    add title varchar(50);

update roles
set title = 'покупатель'
where name = 'ROLE_USER';

update roles
set title = 'администратор'
where name = 'ROLE_ADMIN';

update roles
set title = 'менеджер'
where name = 'ROLE_MANAGER';

update roles
set title = 'генеральный директор'
where name = 'ROLE_SUPERADMIN';

commit;