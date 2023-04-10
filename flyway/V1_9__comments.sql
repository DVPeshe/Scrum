create table comments
(
    id          bigserial primary key,
    username    varchar(255)                    not null,
    product     varchar(255)                    not null,
    description text                            not null,
    visible     boolean                         not null,
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);

insert into comments (username, product, description, visible)
values  ('bob', 'Coca-Cola', 'очень крутой товар1!', true),
        ('john', 'Coca-Cola', 'очень крутой товар2!', true),
        ('artur', 'Coca-Cola', 'очень крутой товар3!', true),
        ('max', 'Coca-Cola', 'очень крутой товар4!', true),
        ('bob', 'Cтиральный порошок', 'очень крутой товар5!', true),
        ('max', 'Cтиральный порошок', 'очень крутой товар6!', true),
        ('ethan', 'Sprite', 'очень крутой товар7!', true),
        ('bob', 'Sprite', 'очень крутой товар8!', true),
        ('john', 'Sprite', 'очень крутой товар9!', true),
        ('bob', 'Бублики', 'очень крутой товар10!', true),
        ('artur', 'Бублики', 'очень крутой товар11!', true),
        ('john', 'Бублики', 'очень крутой товар12!', true);
