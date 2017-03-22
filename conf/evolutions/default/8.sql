# --- !Ups

CREATE SEQUENCE public.registro_aplicativo_id_seq;

CREATE TABLE public.registro_aplicativo
(
  registro_aplicativo_id INTEGER NOT NULL DEFAULT nextval('public.registro_aplicativo_id_seq'),
  app_key VARCHAR(255) NOT NULL,
  nome VARCHAR(255) NOT NULL,
  criado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  autor VARCHAR(255),
  email VARCHAR(255),
  telefone VARCHAR(255),
  descricao VARCHAR(255),
  tenant_id INTEGER NOT NULL,
  scope VARCHAR(255),
  CONSTRAINT registro_aplicativo_pk PRIMARY KEY (registro_aplicativo_id)
);
-- CNLFJKE2SQCTSNKQBN8TG6M86FW2UC8C demo front
-- 4VX796TEPA8K7CXGBRNCTN2FXNNMM2C6 demo admin
--

INSERT INTO public.registro_aplicativo (app_key, nome, autor, email, telefone, tenant_id, scope) VALUES
  ('CNLFJKE2SQCTSNKQBN8TG6M86FW2UC8C', 'SYSBET APP APOSTA', 'Kley', 'kley@sysbet.in', '123451', 1, 'aposta:static'),
  ('4VX796TEPA8K7CXGBRNCTN2FXNNMM2C6', 'SYSBET APP ADMIN', 'Kley', 'kley@sysbet.in', '123451', 1, 'aposta:session');

CREATE SEQUENCE public.pins_pin_id_seq;

CREATE TABLE public.pins
(
  pin_id INTEGER NOT NULL DEFAULT nextval('public.pins_pin_id_seq'),
  tenant_id INTEGER NOT NULL,
  cliente VARCHAR(255) NOT NULL,
  valor_aposta DECIMAL(10,2) NOT NULL,
  criado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expira_em TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pins_pk PRIMARY KEY (pin_id)
);

CREATE SEQUENCE public.palpites_pin_id_seq;

CREATE TABLE public.palpites_pin
(
  palpite_pin_id INTEGER NOT NULL DEFAULT nextval('public.palpites_pin_id_seq'),
  taxa_id INTEGER NOT NULL,
  pin_id INTEGER NOT NULL,
  CONSTRAINT palpites_pin_pk PRIMARY KEY (palpite_pin_id),
  CONSTRAINT palpites_pin_taxas_fk FOREIGN KEY (taxa_id)
  REFERENCES public.taxas (taxa_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT palpites_pin_pins_fk FOREIGN KEY (pin_id)
  REFERENCES public.pins (pin_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE public.importacao_id_seq;

CREATE TABLE public.importacao
(
  importacao_id INTEGER NOT NULL DEFAULT nextval('public.importacao_id_seq'),
  chave VARCHAR(255) NOT NULL,
  criado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  tenant_id INTEGER NOT NULL,
  situacao CHAR(1) NOT NULL,
  variacao DECIMAL(10,2) NOT NULL,
  evento_id INTEGER NOT NULL,
  CONSTRAINT importacao_pk PRIMARY KEY (importacao_id),
  CONSTRAINT importacao_eventos_fk FOREIGN KEY (evento_id)
  REFERENCES public.eventos (evento_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

# --- !Downs

DROP TABLE IF EXISTS registro_aplicativo CASCADE;

DROP TABLE IF EXISTS pins CASCADE;

DROP TABLE IF EXISTS palpites_pin CASCADE;

DROP TABLE IF EXISTS importacao CASCADE;

DROP SEQUENCE IF EXISTS public.importacao_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.registro_aplicativo_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.pins_pin_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.palpites_pin_id_seq CASCADE;
