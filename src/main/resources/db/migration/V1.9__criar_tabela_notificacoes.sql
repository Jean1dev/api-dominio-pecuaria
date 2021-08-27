CREATE TABLE public.notificacao
(
    id         serial    NOT NULL,
    descricao  varchar   NOT NULL,
    tenant     int4      NOT NULL,
    created_at timestamp NOT NULL DEFAULT now(),
    updated_at timestamp NOT NULL DEFAULT now(),
    CONSTRAINT "PK_4151d9593d8680e206b52c8808e1" PRIMARY KEY (id)
);

ALTER TABLE public.notificacao
    ADD CONSTRAINT notificacao_tenant FOREIGN KEY (tenant) REFERENCES tenant (id) ON UPDATE CASCADE ON DELETE SET NULL;