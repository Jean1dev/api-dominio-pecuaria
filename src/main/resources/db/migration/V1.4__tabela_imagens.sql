CREATE TABLE public.imagem (
    id serial NOT NULL,
    url varchar NOT NULL,
    referencia_animal INTEGER NOT NULL,
    CONSTRAINT "PK_IMAGENS" PRIMARY KEY (id)
);

ALTER TABLE public.imagem ADD CONSTRAINT imagens_animal FOREIGN KEY (referencia_animal) REFERENCES animal(id) ON UPDATE CASCADE ON DELETE SET NULL;