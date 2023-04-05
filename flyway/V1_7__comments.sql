create table comments
(
    id          bigserial primary key,
    user_id     bigint references users (id)    not null,
    product_id  bigint references products (id) not null,
    description text                            not null,
    visible     boolean                         not null,
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);

insert into comments (user_id, product_id, description, visible)
values  (1, 12, 'очень крутой товар1!', true),
        (2, 12, 'очень крутой товар2!', true),
        (3, 12, 'очень крутой товар3!', true),
        (5, 12, 'очень крутой товар4!', true),
        (1, 18, 'очень крутой товар5!', true),
        (5, 18, 'очень крутой товар6!', true),
        (4, 13, 'очень крутой товар7!', true),
        (1, 13, 'очень крутой товар8!', true),
        (2, 13, 'очень крутой товар9!', true),
        (1, 5, 'очень крутой товар10!', true),
        (3, 5, 'очень крутой товар11!', true),
        (2, 5, 'очень крутой товар12!', true);
