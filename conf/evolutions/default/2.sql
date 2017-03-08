# Seguranca domain schema

# --- !Ups

CREATE TABLE public.papeis -- papeis de acesso ao sistema
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

COMMENT ON COLUMN public.permissoes.nome IS 'nome da permissao';
COMMENT ON COLUMN public.permissoes.descricao IS 'descricão infromação da permissao';

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

CREATE SEQUENCE public.usuarios_usuario_id_seq START WITH 100;

CREATE TABLE public.usuarios (
                usuario_id INTEGER NOT NULL DEFAULT nextval('public.usuarios_usuario_id_seq'),
                tenant_id INTEGER NOT NULL,
                login VARCHAR(255) NOT NULL,
                senha VARCHAR(255) NOT NULL,
                status VARCHAR(255) NOT NULL,
                papel_id integer NOT NULL, -- papel de acesso do usuario
                plano_comissao_id integer,
                CONSTRAINT usuario_id_pk PRIMARY KEY (usuario_id),
                CONSTRAINT papeis_usuarios_fk FOREIGN KEY (papel_id)
                  REFERENCES public.papeis (papel_id) MATCH SIMPLE
                  ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON TABLE usuarios IS 'tabela de usuarios';

CREATE TABLE public.perfis -- perfis informacoes usuario
(
  usuario_id integer NOT NULL,
  email character varying(255) NOT NULL,
  primeiro_nome character varying(255) NOT NULL,
  sobre_nome character varying(255) NOT NULL,
  exibicao_nome character varying(255) NOT NULL,
  genero integer NOT NULL,
  localidade character varying(255) NOT NULL,
  localizacao character varying(255),
  imagem_url character varying(255),
  perfil_url character varying(255),


  CONSTRAINT perfis_perfil_id_pk PRIMARY KEY (usuario_id),
  CONSTRAINT usuarios_perfis_fk FOREIGN KEY (usuario_id)
      REFERENCES public.usuarios (usuario_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE public.perfis
  IS 'tabela de perfis';

INSERT INTO papeis (papel_id,
                    nome) -- papeis padrão do sistema
VALUES (1,'administrador'), (2,'supervisor'), (3,'operador'), (4,'revendedor'), (5,'usuario');

insert into usuarios (tenant_id,
                    login,
                    papel_id,
                    senha,
                    status,
                    usuario_id) -- inclui administrador padrao
VALUES (1, 'admin.demo', 1, '123456', 'ATIVO', 1),
       (1, 'supervisor.demo', 2, '123456', 'ATIVO', 2),
       (1, 'operador.demo', 3, '123456', 'ATIVO', 3),
       (1, 'revendedor.demo', 4, '12456', 'ATIVO', 4),
       (1, 'usuario.demo', 5, '123456', 'ATIVO', 5);

insert into perfis (email,
                    genero,
                    imagem_url,
                    localidade,
                    localizacao,
                    exibicao_nome,
                    perfil_url,
                    primeiro_nome,
                    sobre_nome,
                    usuario_id)
    values ('admin@sysbet.in', 0, null, 'pt_BR', 'Aracaju', 'Administrador Demo', null, 'Administrador', 'Sysbet', 1),
    ('supervisor@sysbet.in', 0, null, 'pt_BR', 'Aracaju', 'Supervisor Demo', null, 'Supervisor', 'Sysbet', 2),
    ('operador@sysbet.in', 0, null, 'pt_BR', 'Aracaju', 'Operador Demo', null, 'Operador', 'Sysbet', 3),
    ('revendedor@sysbet.in', 0, null, 'pt_BR', 'Aracaju', 'Revendedor Demo', null, 'Revendedor', 'Sysbet', 4),
    ('usuario@sysbet.in', 0, null, 'pt_BR', 'Aracaju', 'Usuario Demo', null, 'Usuario', 'Sysbet', 5);

# --- !Downs

DROP TABLE IF EXISTS public.usuarios CASCADE;

DROP TABLE IF EXISTS public.perfis CASCADE;

DROP SEQUENCE IF EXISTS public.usuarios_usuario_id_seq CASCADE;

DROP TABLE IF EXISTS public.papeis_has_permissoes CASCADE;

DROP TABLE IF EXISTS public.papeis CASCADE;

DROP TABLE IF EXISTS  public.permissoes CASCADE;



