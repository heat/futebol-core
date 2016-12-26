# Eventos domain schema

# --- !Ups

CREATE SEQUENCE public.times_time_id_seq;

CREATE TABLE public.times (
                time_id INTEGER NOT NULL DEFAULT nextval('public.times_time_id_seq'),
                nome VARCHAR(200) NOT NULL,
                tenant_id INTEGER NOT NULL,
                CONSTRAINT times_pk PRIMARY KEY (time_id)
);
COMMENT ON TABLE public.times IS 'tabela de times disponiveis';


ALTER SEQUENCE public.times_time_id_seq OWNED BY public.times.time_id;

CREATE SEQUENCE public.campeonatos_campeonato_id_seq;

CREATE TABLE public.campeonatos (
                campeonato_id INTEGER NOT NULL DEFAULT nextval('public.campeonatos_campeonato_id_seq'),
                nome VARCHAR(200) NOT NULL,
                tenant_id INTEGER NOT NULL,
                CONSTRAINT campeonatos_pk PRIMARY KEY (campeonato_id)
);
COMMENT ON TABLE public.campeonatos IS 'Lista de campeonatos';


ALTER SEQUENCE public.campeonatos_campeonato_id_seq OWNED BY public.campeonatos.campeonato_id;

CREATE UNIQUE INDEX campeonatos_nome_idx
 ON public.campeonatos
 ( nome, tenant_id );

CREATE SEQUENCE public.eventos_evento_id_seq;

CREATE TABLE public.eventos (
                evento_id INTEGER NOT NULL DEFAULT nextval('public.eventos_evento_id_seq'),
                tenant_id INTEGER NOT NULL,
                campeonato_id INTEGER NOT NULL,
                time_id_casa INTEGER NOT NULL,
                time_id_fora INTEGER NOT NULL,
                casa VARCHAR(255),
                data_evento TIMESTAMP NOT NULL,
                fora VARCHAR(255),
                CONSTRAINT eventos_pk PRIMARY KEY (evento_id)
);


ALTER SEQUENCE public.eventos_evento_id_seq OWNED BY public.eventos.evento_id;

CREATE SEQUENCE public.resultados_resultado_id_seq;

CREATE TABLE public.resultados (
                resultado_id INTEGER NOT NULL DEFAULT nextval('public.resultados_resultado_id_seq'),
                tenant_id INTEGER NOT NULL,
                evento_id INTEGER NOT NULL,
                time_id INTEGER NOT NULL,
                momento CHAR(2) NOT NULL,
                pontos INTEGER NOT NULL,
                CONSTRAINT resultados_pk PRIMARY KEY (resultado_id)
);
COMMENT ON TABLE public.resultados IS 'tabela de resultados';
COMMENT ON COLUMN public.resultados.momento IS 'Momento do registro do resultado
- FT primeiro tempo do futebol
- FR resultado final do futebol';
COMMENT ON COLUMN public.resultados.pontos IS 'quantidade de pontos marcados';


ALTER SEQUENCE public.resultados_resultado_id_seq OWNED BY public.resultados.resultado_id;

ALTER TABLE public.eventos ADD CONSTRAINT times_eventos_fk
FOREIGN KEY (time_id_casa)
REFERENCES public.times (time_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.eventos ADD CONSTRAINT times_eventos_fk1
FOREIGN KEY (time_id_fora)
REFERENCES public.times (time_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.resultados ADD CONSTRAINT times_resultados_fk
FOREIGN KEY (time_id)
REFERENCES public.times (time_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.eventos ADD CONSTRAINT campeonatos_eventos_fk
FOREIGN KEY (campeonato_id)
REFERENCES public.campeonatos (campeonato_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.resultados ADD CONSTRAINT eventos_resultados_fk
FOREIGN KEY (evento_id)
REFERENCES public.eventos (evento_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

# --- !Downs

DROP TABLE IF EXISTS resultados CASCADE;

DROP TABLE IF EXISTS eventos CASCADE;

DROP TABLE IF EXISTS times CASCADE;

DROP TABLE IF EXISTS campeonatos CASCADE;

DROP SEQUENCE IF EXISTS public.resultados_resultado_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.eventos_evento_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.campeonatos_campeonato_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.times_time_id_seq CASCADE;