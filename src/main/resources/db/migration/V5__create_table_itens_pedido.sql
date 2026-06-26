CREATE TABLE itens_pedido
(
    id             bigserial PRIMARY KEY,
    pedido_id      BIGINT         NOT NULL,
    produto_id     BIGINT         NOT NULL,
    quantidade     INTEGER        NOT NULL CHECK (quantidade > 0),
    preco_unitario NUMERIC(10, 2) NOT NULL,

    CONSTRAINT fk_itens_pedido_pedido_id
        FOREIGN KEY (pedido_id) REFERENCES pedidos (id),

    CONSTRAINT fk_pedido_produto_id
        FOREIGN KEY (produto_id) REFERENCES produtos (id)
);