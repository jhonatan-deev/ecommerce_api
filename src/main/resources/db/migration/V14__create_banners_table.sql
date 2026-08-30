CREATE TABLE banners
(
    id           bigserial primary key,
    titulo       varchar(150) not null,
    imagem_url   varchar(500) not null,
    ordem        integer      not null default 0,
    ativo        boolean      not null default true,
    categoria_id bigint references categorias (id)
);