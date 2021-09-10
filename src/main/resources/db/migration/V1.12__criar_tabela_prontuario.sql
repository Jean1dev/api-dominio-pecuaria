CREATE TABLE IF NOT EXISTS public.prontuario
(
    id serial NOT NULL,
    data_ultima_atualizacao DATE NOT NULL,
    data_criacao DATE NOT NULL,
    observacoes TEXT,
    lista_processos_vacinacao TEXT,
    tenant INTEGER NOT NULL,
    animal INTEGER NOT NULL,
    CONSTRAINT "PK_PRONTUARIO" PRIMARY KEY (id)
);

ALTER TABLE public.prontuario ADD CONSTRAINT prontuario_tenant FOREIGN KEY (tenant) REFERENCES tenant(id) ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE public.prontuario ADD CONSTRAINT prontuario_animal FOREIGN KEY (animal) REFERENCES animal(id);