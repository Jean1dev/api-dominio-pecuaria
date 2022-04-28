ALTER TABLE public.prontuario
    ADD COLUMN lista_parecer_veterinario TEXT;

CREATE TABLE IF NOT EXISTS public.veterinario
(
    id serial NOT NULL,
    nome TEXT,
    email TEXT NOT NULL,
    CONSTRAINT "PK_VET" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.agendamento_veterinario
(
    id serial NOT NULL,
    data_solicitacao DATE NOT NULL,
    data_agendamento DATE NOT NULL,
    observacoes_veterinario TEXT,
    periodo_dia character varying NOT NULL,
    status_agendamento character varying NOT NULL,
    veterinario INTEGER,
    tenant INTEGER NOT NULL,
    CONSTRAINT "Pk_AGENDAMENTO" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.agendamentos_veterinario_medicamentos
(
    agendamento_veterinario_id serial REFERENCES agendamento_veterinario (id) ON UPDATE CASCADE ON DELETE CASCADE,
    medicamento_id serial REFERENCES medicamento (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT agendamentos_veterinario_medicamentos_pkey PRIMARY KEY (agendamento_veterinario_id, medicamento_id)
);

ALTER TABLE public.agendamento_veterinario ADD CONSTRAINT agendamento_veterinario_tenant FOREIGN KEY (tenant) REFERENCES tenant(id) ON UPDATE CASCADE ON DELETE SET NULL;