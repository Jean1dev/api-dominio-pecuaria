CREATE TABLE IF NOT EXISTS public.acesso
(
    id serial NOT NULL,
    data_hora_acesso DATE NOT NULL,
    login TEXT NOT NULL,
    tenant INTEGER NOT NULL,
    CONSTRAINT "PK_ACESSO" PRIMARY KEY (id)
);

ALTER TABLE public.acesso ADD CONSTRAINT acesso_tenant FOREIGN KEY (tenant) REFERENCES tenant(id) ON UPDATE CASCADE ON DELETE SET NULL;