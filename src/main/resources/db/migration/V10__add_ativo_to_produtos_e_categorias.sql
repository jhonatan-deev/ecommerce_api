ALTER TABLE produtos
    ADD COLUMN ativo boolean NOT NULL DEFAULT true;

ALTER TABLE categorias
    ADD COLUMN ativo boolean NOT NULL DEFAULT true;