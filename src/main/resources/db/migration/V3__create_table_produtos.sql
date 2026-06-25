CREATE TABLE produtos
(
    id           bigserial PRIMARY KEY,
    nome         VARCHAR(100)   NOT NULL,
    descricao    VARCHAR(300)   NOT NULL,
    preco        NUMERIC(10, 2) NOT NULL,
    estoque      INTEGER        NOT NULL,
    categoria_id BIGINT         NOT NULL,

    CONSTRAINT fk_produto_categoria_id
        FOREIGN KEY (categoria_id) REFERENCES categorias (id)
);