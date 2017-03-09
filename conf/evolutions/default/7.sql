# --- !Ups

CREATE SEQUENCE public.planos_comissao_id_seq;

CREATE TABLE public.plano_comissao
(
  plano_comissao_id INTEGER NOT NULL,
  nome VARCHAR(255) NOT NULL,
  dtype VARCHAR(31) NOT NULL,
  CONSTRAINT plano_comissao_pkey PRIMARY KEY (plano_comissao_id)
);

CREATE SEQUENCE public.comissao_id_seq;

CREATE TABLE public.comissao
(
  comissao_id INTEGER NOT NULL,
  criado_em TIMESTAMP NOT NULL,
  evento_comissao INTEGER NOT NULL,
  valor DECIMAL(10,2) NOT NULL,
  conta_id INTEGER NOT NULL,
  CONSTRAINT comissao_pk PRIMARY KEY (comissao_id)
);

ALTER TABLE public.comissao ADD CONSTRAINT comissao_contas_fk
FOREIGN KEY (conta_id)
REFERENCES public.contas (conta_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE public.plano_comissao_parametro_id_seq;

CREATE TABLE public.plano_comissao_parametro
(
  plano_comissao_parametro_id INTEGER NOT NULL,
  valor DECIMAL(10,2) NOT NULL,
  parametro_valor DECIMAL(10,2) NOT NULL,
  plano_comissao_id INTEGER NOT NULL,
  CONSTRAINT plano_comissao_parametro_pk PRIMARY KEY (plano_comissao_parametro_id)
);

ALTER TABLE public.plano_comissao_parametro ADD CONSTRAINT parametro_comissao_fk
FOREIGN KEY (plano_comissao_id)
REFERENCES public.plano_comissao (plano_comissao_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.usuarios ADD CONSTRAINT usuarios_plano_comissao_fk
FOREIGN KEY (plano_comissao_id)
REFERENCES public.plano_comissao (plano_comissao_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

# --- !Downs

DROP TABLE IF EXISTS plano_comissao_parametro CASCADE;

DROP TABLE IF EXISTS plano_comissao CASCADE;

DROP TABLE IF EXISTS comissao CASCADE;

DROP SEQUENCE IF EXISTS public.planos_comissao_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.comissao_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.plano_comissao_parametro_id_seq;

