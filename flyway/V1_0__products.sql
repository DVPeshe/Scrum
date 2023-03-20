create table categories
(
    id         bigserial primary key,
    title      varchar(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table products
(
    id          bigserial primary key,
    title       varchar(255) unique               not null,
    price       numeric(8, 2)                     not null,
    category_id bigint references categories (id) not null,
    visible     boolean                           not null,
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);

insert into categories (title)
values ('Еда');
insert into categories (title)
values ('Хозтовары');
insert into categories (title)
values ('Напитки');

insert into products (title, price, category_id, visible)
values ('Молоко', 100.00, 1, true),
       ('Хлеб', 80.00, 1, true),
       ('Сыр', 90.00, 1, true),
       ('Масло', 320.00, 1, true),
       ('Бублики', 50.00, 1, true),
       ('Пирожок', 30.00, 1, true),
       ('Огурцы', 140.00, 1, true),
       ('Помидоры', 160.00, 1, true),
       ('Лимонад', 100.00, 3, true),
       ('Подсолнечное масло', 110.00, 1, true),
       ('Мороженное', 70.00, 1, true),
       ('Coca-Cola', 87.00, 3, true),
       ('Sprite', 89.00, 3, true),
       ('Моющее средство', 50.00, 2, true),
       ('Губки для мытья посуды', 20.00, 2, true),
       ('Мыло', 10.00, 2, true),
       ('Хоз. перчатки', 15.00, 2, true),
       ('Cтиральный порошок', 40.00, 2, true);

create table orders
(
    id          bigserial primary key,
    username    varchar(255),
    total_price numeric(8, 2),
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);

create table orders_items
(
    id                bigserial primary key,
    order_id          bigint references orders (id),
    product_id        bigint references products (id),
    price_per_product numeric(8, 2),
    quantity          int,
    price             numeric(8, 2),
    created_at        timestamp default current_timestamp,
    updated_at        timestamp default current_timestamp
);
