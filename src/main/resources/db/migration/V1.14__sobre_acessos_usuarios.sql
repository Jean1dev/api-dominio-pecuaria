ALTER TABLE public.usuario_acesso
    ADD COLUMN conta_validada bool DEFAULT true;

UPDATE public.usuario_acesso
SET conta_validada = true;

ALTER TABLE public.acesso
    ADD COLUMN ip varchar;

ALTER TABLE public.acesso
    ADD COLUMN dispositivo varchar;

CREATE TABLE public.alteracao_senha
(
    id                serial    NOT NULL,
    chave             varchar   NOT NULL,
    email             varchar   NOT NULL,
    login             varchar   NOT NULL,
    data_hora_criacao TIMESTAMP NOT NULL,
    time_expiracao    BIGINT    NOT NULL,
    CONSTRAINT "PK_ALTERACAO_SENHA" PRIMARY KEY (id),
    CONSTRAINT "UQ_0cbe1fe975ff21cc7afc43289f9sfj9" UNIQUE (login)
);