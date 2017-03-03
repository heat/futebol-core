# Bilhete domain schema

# --- !Ups

CREATE SEQUENCE public.bilhetes_bilhete_id_seq;

CREATE TABLE public.bilhetes
(
  bilhete_id INTEGER NOT NULL DEFAULT nextval('public.bilhetes_bilhete_id_seq'),
  tenant_id INTEGER NOT NULL,
  usuario_id INTEGER NOT NULL,
  codigo VARCHAR(15) NOT NULL,
  situacao character(1) NOT NULL,
  cliente VARCHAR(50) NOT NULL,
  valor_aposta DECIMAL(10,2) NOT NULL,
  valor_premio DECIMAL(10,2) NOT NULL,
  alterado_em timestamp without time zone NOT NULL,
  criado_em timestamp without time zone NOT NULL,
  CONSTRAINT bilhetes_pkey PRIMARY KEY (bilhete_id)
)
WITH (
   OIDS=FALSE
);

COMMENT ON TABLE public.bilhetes IS 'Tabela de bilhetes';


ALTER TABLE public.bilhetes ADD CONSTRAINT usuarios_bilhetes_fk
FOREIGN KEY (usuario_id)
REFERENCES public.usuarios (usuario_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


CREATE SEQUENCE public.palpites_palpite_id_seq;

CREATE TABLE public.palpites
(
  palpite_id INTEGER NOT NULL DEFAULT nextval('public.palpites_palpite_id_seq'),
  taxa_id INTEGER NOT NULL,
  tenant_id INTEGER NOT NULL,
  bilhete_id INTEGER NOT NULL,
  taxa DECIMAL(5,2) NOT NULL,
  status VARCHAR(1) NOT NULL,
  CONSTRAINT palpites_pkey PRIMARY KEY (palpite_id)
)
WITH (
   OIDS=FALSE
);

COMMENT ON TABLE public.palpites IS 'Tabela de palpites';

ALTER TABLE public.palpites ADD CONSTRAINT bilhetes_palpites_fk
FOREIGN KEY (bilhete_id)
REFERENCES public.bilhetes (bilhete_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.palpites ADD CONSTRAINT taxas_palpites_fk
FOREIGN KEY (taxa_id)
REFERENCES public.taxas (taxa_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE public.contas_conta_id_seq;

CREATE TABLE public.contas (
                conta_id INTEGER NOT NULL DEFAULT nextval('public.contas_conta_id_seq'),
                usuario_id INTEGER NOT NULL,
                CONSTRAINT contas_pk PRIMARY KEY (conta_id)
);

ALTER TABLE public.contas ADD CONSTRAINT contas_usuarios_fk
FOREIGN KEY (usuario_id)
REFERENCES public.usuarios (usuario_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE public.lancamentos_lancamento_id_seq;

CREATE TABLE public.lancamentos (
                lancamento_id INTEGER NOT NULL DEFAULT nextval('public.lancamentos_lancamento_id_seq'),
                conta_id INTEGER NOT NULL,
                valor DECIMAL(10,2) NOT NULL,
                saldo DECIMAL(10,2) NOT NULL,
                dtype VARCHAR(50) NOT NULL,
                criado_em TIMESTAMP NOT NULL,
		origem_bilhete INTEGER,
                CONSTRAINT lancamentos_pk PRIMARY KEY (lancamento_id)
);

ALTER TABLE public.lancamentos ADD CONSTRAINT lancamentos_contas_fk
FOREIGN KEY (conta_id)
REFERENCES public.contas (conta_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


# --- !Downs

DROP TABLE IF EXISTS palpites CASCADE;

DROP TABLE IF EXISTS bilhetes CASCADE;

DROP TABLE IF EXISTS lancamentos CASCADE;

DROP TABLE IF EXISTS contas CASCADE;

DROP SEQUENCE IF EXISTS public.bilhetes_bilhete_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.palpites_palpite_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.lancamentos_lancamento_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.contas_conta_id_seq CASCADE;

