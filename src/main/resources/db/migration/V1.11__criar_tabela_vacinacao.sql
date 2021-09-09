CREATE TABLE IF NOT EXISTS public.processo_vacinacao
(
    id serial NOT NULL,
    processo_revertido bool DEFAULT false,
    total_animais_vacinados bigint NOT NULL,
    animais_id_lista TEXT NOT NULL,
    data_processo DATE NOT NULL,
    medicamento INTEGER NOT NULL,
    tenant INTEGER NOT NULL,
    CONSTRAINT "PK_PROCESSO_VACINACAO" PRIMARY KEY (id)
);

ALTER TABLE public.processo_vacinacao ADD CONSTRAINT processo_vacinacao_tenant FOREIGN KEY (tenant) REFERENCES tenant(id) ON UPDATE CASCADE ON DELETE SET NULL;
ALTER TABLE public.processo_vacinacao ADD CONSTRAINT processo_vacinacao_medicamento FOREIGN KEY (medicamento) REFERENCES medicamento(id);