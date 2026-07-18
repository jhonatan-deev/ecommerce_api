CREATE TABLE password_reset_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(255) NOT NULL UNIQUE,
    usuario_id BIGINT       NOT NULL,
    expires_at TIMESTAMP    NOT NULL,
    usado      BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_prt_user
        FOREIGN KEY (usuario_id)
            REFERENCES usuarios (id)
);