CREATE TABLE usuarios
(
    id    bigserial primary key,
    nome  varchar(100) not null,
    email varchar(250) not null,
    senha varchar(250) not null,
    tipo  varchar(100) not null,
    ativo boolean      not null default true
);