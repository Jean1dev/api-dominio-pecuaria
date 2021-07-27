CREATE TABLE IF NOT EXISTS public.peso_animal
(
    id            SERIAL  NOT NULL,
    data_pesagem  DATE    NOT NULL,
    peso          INTEGER,
    idade_em_dias INTEGER,
    animal_id     INTEGER NOT NULL,
    CONSTRAINT "PK_PESO_ANIMAL" PRIMARY KEY (id),
    FOREIGN KEY (animal_id) REFERENCES animal (id)
);

ALTER TABLE public.peso_animal ADD CONSTRAINT animal_peso FOREIGN KEY (animal_id) REFERENCES animal(id) ON UPDATE CASCADE ON DELETE SET NULL;