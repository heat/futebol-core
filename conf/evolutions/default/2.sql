# Seguranca domain schema

# --- !Ups

CREATE SEQUENCE public.usuarios_usuario_id_seq;

CREATE TABLE public.usuarios (
                usuario_id INTEGER NOT NULL DEFAULT nextval('public.usuarios_usuario_id_seq'),
                login VARCHAR(255) NOT NULL,
                senha VARCHAR(255) NOT NULL,
                CONSTRAINT usuario_id_pk PRIMARY KEY (usuario_id)
);
COMMENT ON TABLE usuarios IS 'tabela de usuarios';

# --- !Downs

DROP SEQUENCE public.usuarios_idusuario_seq;

DROP TABLE public.usuarios;