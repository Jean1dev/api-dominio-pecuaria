CREATE TABLE IF NOT EXISTS public.peso_animal
(
    id            SERIAL  NOT NULL,
    data_pesagem  DATE    NOT NULL,
    peso          INTEGER,
    idade_em_dias INTEGER,
    animal_id     INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (animal_id) REFERENCES animal (id)
);

ALTER TABLE animal
    ADD COLUMN peso_animal_id INTEGER;
ALTER TABLE animal
    ADD CONSTRAINT fk_peso_animal_id
        FOREIGN KEY (peso_animal_id)
            REFERENCES peso_animal (id);