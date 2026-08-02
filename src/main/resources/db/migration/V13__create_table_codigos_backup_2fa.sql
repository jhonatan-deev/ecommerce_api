CREATE TABLE codigos_backup_2fa (
        id BIGSERIAL PRIMARY KEY,
        usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
        codigo_hash VARCHAR(255) NOT NULL,
        usado BOOLEAN NOT NULL DEFAULT FALSE,
        criado_em TIMESTAMP NOT NULL DEFAULT now()
);