ALTER TABLE public.usuario_acesso
    ADD COLUMN conta_publica BOOLEAN;

CREATE TABLE IF NOT EXISTS public.convite_amizade
(
    id                  serial  NOT NULL,
    data_solicitacao    DATE    NOT NULL,
    mensagem            TEXT,
    aceito              BOOLEAN,
    rejeitado           BOOLEAN,
    usuario_solicitante INTEGER NOT NULL,
    usuario_requisitado INTEGER NOT NULL,
    CONSTRAINT "PK_AMIZADE" PRIMARY KEY (id)
);

ALTER TABLE public.convite_amizade
    ADD CONSTRAINT usuario_solicitante_user
        FOREIGN KEY (usuario_solicitante) REFERENCES usuario_acesso (id) ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE public.convite_amizade
    ADD CONSTRAINT usuario_requisitado_user
        FOREIGN KEY (usuario_requisitado) REFERENCES usuario_acesso (id) ON UPDATE CASCADE ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS public.relacionamentos_amizades
(
    id           serial  NOT NULL,
    data_criacao DATE    NOT NULL,
    id_owner     INTEGER NOT NULL,
    id_friend    INTEGER NOT NULL,
    CONSTRAINT "PK_RELACIONAMENTOS_AMIZADE" PRIMARY KEY (id)
);

ALTER TABLE public.relacionamentos_amizades
    ADD CONSTRAINT relacionamentos_amizades_id_owner
        FOREIGN KEY (id_owner) REFERENCES usuario_acesso (id) ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE public.relacionamentos_amizades
    ADD CONSTRAINT relacionamentos_amizades_id_friend
        FOREIGN KEY (id_friend) REFERENCES usuario_acesso (id) ON UPDATE CASCADE ON DELETE SET NULL;