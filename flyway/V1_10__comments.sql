begin;

alter table comments
    add estimation smallint;

update comments
set estimation = 3
where id = 1;

update comments
set estimation = 4
where id = 2;

update comments
set estimation = 5
where id = 3;

update comments
set estimation = 3
where id = 4;

update comments
set estimation = 4
where id = 5;

update comments
set estimation = 5
where id = 6;

update comments
set estimation = 3
where id = 7;

update comments
set estimation = 4
where id = 8;

update comments
set estimation = 5
where id = 9;

update comments
set estimation = 3
where id = 10;

update comments
set estimation = 4
where id = 11;

update comments
set estimation = 5
where id = 12;

commit;


