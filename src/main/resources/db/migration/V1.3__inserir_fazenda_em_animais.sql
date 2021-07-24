ALTER TABLE animal
    ADD COLUMN fazenda_id INTEGER;
ALTER TABLE animal
    ADD CONSTRAINT fk_fazenda_id
        FOREIGN KEY (fazenda_id)
            REFERENCES fazenda (id);