CREATE TABLE IF NOT EXISTS public.peso_animal
(
    id            SERIAL NOT NULL,
    data_pesagem  DATE NOT NULL,
    peso          INTEGER,
    idade_em_dias INTEGER,
        PRIMARY KEY (id)
);