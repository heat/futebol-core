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

CREATE TABLE public."Perfis"
(
  usuario_id integer NOT NULL,
  email character varying(255) NOT NULL,
  primeiro_nome character varying(255) NOT NULL,
  sobre_nome character varying(255) NOT NULL,
  exibicao_nome character varying(255) NOT NULL,
  genero integer NOT NULL,
  localidade character(255) NOT NULL,
  localizacao character varying(255) NOT NULL,
  imagem_url character varying(255) NOT NULL,
  perfil_url character varying(255) NOT NULL,
  CONSTRAINT perfis_perfil_id_pk PRIMARY KEY (usuario_id),
  CONSTRAINT usuarios_perfis_fk FOREIGN KEY (usuario_id)
      REFERENCES public.usuarios (usuario_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE public."Perfis"
  IS 'tabela de perfis';

CREATE TABLE public.papeis
(
  papel_id integer NOT NULL,
  nome character varying(255) NOT NULL,
  CONSTRAINT papeis_papel_id_pk PRIMARY KEY (papel_id)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE public.permissoes
(
  permissao_id integer NOT NULL,
  nome character varying(255) NOT NULL, -- nome da permissao
  descricao character varying(255) NOT NULL, -- descricao infromação da permissao
  CONSTRAINT permissoes_permissao_id_pk PRIMARY KEY (permissao_id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE public.permissoes
  OWNER TO kb5w;
COMMENT ON COLUMN public.permissoes.nome IS 'nome da permissao';
COMMENT ON COLUMN public.permissoes.descricao IS 'descricao infromação da permissao';

-- tabela de relacionamento dos papeis e permissoes
CREATE TABLE public.papeis_has_permissoes
(
  papel_id integer NOT NULL,
  permissao_id integer NOT NULL,
  CONSTRAINT papeis_has_permissoes_pk PRIMARY KEY (papel_id, permissao_id),
  CONSTRAINT papeis_papeis_has_permissoes_fk FOREIGN KEY (papel_id)
      REFERENCES public.papeis (papel_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT permissoes_papeis_has_permissoes_fk FOREIGN KEY (permissao_id)
      REFERENCES public.permissoes (permissao_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

COPY papeis (papel_id, nome) FROM stdin;
1	administrador
2	revendedor
3	operador
4	supervisor
5	usuario
\.

# --- !Downs

DROP TABLE public.perfis;

DROP SEQUENCE public.usuarios_idusuario_seq;

DROP TABLE public.usuarios;

DROP TABLE public.papeis_has_permissoes;

DROP TABLE public.papeis;

DROP TABLE public.permissoes;



