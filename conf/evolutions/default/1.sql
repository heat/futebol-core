# Eventos domain schema

# --- !Ups

CREATE TABLE "times" (
  idtime integer NOT NULL,
  nome character varying(200) NOT NULL
);

COMMENT ON TABLE "times" IS 'tabela de times';

CREATE TABLE campeonatos (
  idcampeonato integer NOT NULL,
  nome character varying(200) NOT NULL
);

COMMENT ON TABLE campeonatos IS 'Registro dos campeonatos';

CREATE TABLE resultados (
  eventos_idevento integer NOT NULL,
  times_idtime integer NOT NULL,
  momento character(2) NOT NULL,
  pontos integer NOT NULL
);

COMMENT ON TABLE resultados IS 'tabela de resultados';

COMMENT ON COLUMN resultados.eventos_idevento IS 'tabela de eventos';

COMMENT ON COLUMN resultados.momento IS 'Momento do registro do resultado
- FT primeiro tempo do futebol
- FR resultado final do futebol';

COMMENT ON COLUMN resultados.pontos IS 'quantidade de pontos marcados';

CREATE TABLE eventos
(
  idevento integer NOT NULL,
  casa character varying(255),
  dataevento bytea,
  fora character varying(255)
);

CREATE SEQUENCE campeonatos_idcampeonato_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE campeonatos_idcampeonato_seq OWNED BY campeonatos.idcampeonato;

CREATE SEQUENCE eventos_idevento_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE eventos_idevento_seq OWNED BY eventos.idevento;

CREATE SEQUENCE times_idtime_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE times_idtime_seq OWNED BY "times".idtime;

ALTER TABLE ONLY "times" ALTER COLUMN idtime SET DEFAULT nextval('times_idtime_seq'::regclass);

ALTER TABLE ONLY campeonatos ALTER COLUMN idcampeonato SET DEFAULT nextval('campeonatos_idcampeonato_seq'::regclass);

ALTER TABLE ONLY eventos ALTER COLUMN idevento SET DEFAULT nextval('eventos_idevento_seq'::regclass);

ALTER TABLE ONLY campeonatos
  ADD CONSTRAINT campeonatos_pk PRIMARY KEY (idcampeonato);

ALTER TABLE ONLY resultados
  ADD CONSTRAINT resultados_pk PRIMARY KEY (eventos_idevento, times_idtime);

ALTER TABLE ONLY "times"
  ADD CONSTRAINT times_pk PRIMARY KEY (idtime);

ALTER TABLE ONLY "eventos"
  ADD CONSTRAINT eventos_pk PRIMARY KEY (idevento);

# --- !Downs

DROP TABLE resultados;

DROP TABLE evento;

DROP TABLE times;

DROP TABLE campeonatos;