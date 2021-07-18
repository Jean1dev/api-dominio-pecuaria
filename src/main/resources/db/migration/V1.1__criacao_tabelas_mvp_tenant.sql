CREATE TABLE IF NOT EXISTS public.fazenda
(
    id serial NOT NULL,
    cap_maxima_gado bigint,
    cod_estab character varying,
    endereco character varying,
    metragem character varying,
    tenant int4 NOT NULL,
    marca_produtor character varying,
    nome character varying NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.fazenda
    ADD CONSTRAINT fazenda_tenant
        FOREIGN KEY (tenant)
            REFERENCES tenant(id)
            ON UPDATE CASCADE
            ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS public.funcionario
(
    id serial NOT NULL,
    nome character varying NOT NULL,
    tenant int4 NOT NULL,
    cargo character varying,
    PRIMARY KEY (id)
);

ALTER TABLE public.funcionario
    ADD CONSTRAINT funcionario_tenant
        FOREIGN KEY (tenant)
            REFERENCES tenant(id)
            ON UPDATE CASCADE
            ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS public.medicamento
(
    id serial NOT NULL,
    nome character varying NOT NULL,
    tenant int4 NOT NULL,
    descricao character varying,
    data_validade date,
    PRIMARY KEY (id)
);

ALTER TABLE public.medicamento
    ADD CONSTRAINT medicamento_tenant
        FOREIGN KEY (tenant)
            REFERENCES tenant(id)
            ON UPDATE CASCADE
            ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS public.animal
(
    id serial NOT NULL,
    numero bigint NOT NULL,
    raca character varying,
    apelido character varying,
    tenant int4 NOT NULL,
    data_nascimento date,
    numero_crias integer,
    estado_atual character varying,
    data_ultimo_parto date,
    descarte_futuro boolean default false,
    justificativa_descarte_futuro character varying,
    PRIMARY KEY (id)
);

ALTER TABLE public.animal
    ADD CONSTRAINT animal_tenant
        FOREIGN KEY (tenant)
            REFERENCES tenant(id)
            ON UPDATE CASCADE
            ON DELETE SET NULL;

ALTER TABLE public.funcionario
    ADD FOREIGN KEY (id)
        REFERENCES public.fazenda (id)
    NOT VALID;

END;