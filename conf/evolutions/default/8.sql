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

INSERT INTO public.registro_aplicativo (app_key, nome, autor, email, telefone, tenant_id) VALUES ('c3lzYmV0d2ViLXN5c2JldA==', 'Sysbet', 'Kley', 'kley@sysbet.in', '123451', 1);

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

# --- !Downs

DROP TABLE IF EXISTS registro_aplicativo CASCADE;

DROP TABLE IF EXISTS pins CASCADE;

DROP TABLE IF EXISTS palpites_pin CASCADE;

DROP SEQUENCE IF EXISTS public.registro_aplicativo_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.pins_pin_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.palpites_pin_id_seq CASCADE;
