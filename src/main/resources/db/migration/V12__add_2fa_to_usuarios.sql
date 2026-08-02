ALTER TABLE usuarios
    ADD COLUMN dois_fatores_ativo BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN dois_fatores_segredo VARCHAR(255);