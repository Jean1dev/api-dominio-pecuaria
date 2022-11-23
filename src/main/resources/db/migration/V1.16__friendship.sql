ALTER TABLE public.usuario_acesso
    ADD COLUMN conta_publica BOOLEAN;

CREATE TABLE IF NOT EXISTS public.convite_amizade
(
    id serial NOT NULL,
    data_solicitacao DATE NOT NULL,
    mensagem TEXT,
    aceito BOOLEAN,
    usuario_solicitante INTEGER NOT NULL,
    usuario_requisitado INTEGER NOT NULL,
    CONSTRAINT "PK_AMIZADE" PRIMARY KEY (id)
);

ALTER TABLE public.convite_amizade ADD CONSTRAINT usuario_solicitante_user
    FOREIGN KEY (usuario_solicitante) REFERENCES usuario_acesso(id) ON UPDATE CASCADE ON DELETE SET NULL;

ALTER TABLE public.convite_amizade ADD CONSTRAINT usuario_requisitado_user
    FOREIGN KEY (usuario_requisitado) REFERENCES usuario_acesso(id) ON UPDATE CASCADE ON DELETE SET NULL;