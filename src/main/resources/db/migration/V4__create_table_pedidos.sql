CREATE TABLE pedidos
(
    id                 bigserial PRIMARY KEY,
    usuario_id         BIGINT         NOT NULL,
    data_pedido        TIMESTAMP      NOT NULL,
    status_pedido      VARCHAR(100)   NOT NULL,
    valor_total_pedido NUMERIC(10, 2) NOT NULL,

    CONSTRAINT fk_pedido_usuario_id
        FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);