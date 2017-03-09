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

# --- !Downs

DROP TABLE IF EXISTS registro_aplicativo CASCADE;

DROP SEQUENCE IF EXISTS public.registro_aplicativo_id_seq CASCADE;
