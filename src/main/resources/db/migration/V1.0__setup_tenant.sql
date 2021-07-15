CREATE TABLE public.tenant (
    id serial NOT NULL,
    nome varchar NOT NULL,
    ativo bool NULL DEFAULT true,
    CONSTRAINT "PK_79845bedc33ae6f26d05aed76d4" PRIMARY KEY (id)
);

CREATE TABLE public.usuario_acesso (
	id serial NOT NULL,
	login varchar NOT NULL,
	password varchar NOT NULL,
	tenant int4 NOT NULL,
	created_at timestamp NOT NULL DEFAULT now(),
	updated_at timestamp NOT NULL DEFAULT now(),
	ativo bool NULL DEFAULT true,
	CONSTRAINT "PK_4151d9593d8680e206b52c8808e" PRIMARY KEY (id),
	CONSTRAINT "UQ_0cbe1fe975ff21cc7afc4e32788" UNIQUE (login)
);

ALTER TABLE public.usuario_acesso ADD CONSTRAINT usuario_acesso_tenant FOREIGN KEY (tenant) REFERENCES tenant(id) ON UPDATE CASCADE ON DELETE SET NULL;