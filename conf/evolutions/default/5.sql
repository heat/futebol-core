# Apostas domain schema

# --- !Ups


CREATE SEQUENCE public.bilhetes_bilhete_id_seq;

CREATE TABLE public.bilhetes
(
  bilhete_id INTEGER NOT NULL DEFAULT nextval('public.bilhetes_bilhete_id_seq'),
  tenant_id INTEGER NOT NULL,
  usuario_id INTEGER NOT NULL,
  codigo VARCHAR(12) NOT NULL,
  status character(1) NOT NULL,
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

ALTER TABLE public.bilhetes
  OWNER TO postgres;
COMMENT ON TABLE public.bilhetes IS 'Tabela de bilhetes';


ALTER TABLE public.bilhetes ADD CONSTRAINT usuarios_bilhetes_fk
FOREIGN KEY (usuario_id)
REFERENCES public.usuarios (usuario_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


CREATE TABLE public.palpites
(
  bilhete_id INTEGER NOT NULL,
  evento_aposta_id INTEGER NOT NULL,
  taxa_id INTEGER NOT NULL,
  tenant_id INTEGER NOT NULL,
  taxa DECIMAL(5,2) NOT NULL,
  status character(1) NOT NULL,
  CONSTRAINT palpites_pkey PRIMARY KEY (bilhete_id, evento_aposta_id)
)
WITH (
   OIDS=FALSE
);

ALTER TABLE public.palpites
  OWNER TO postgres;
COMMENT ON TABLE public.palpites IS 'Tabela de palpites';

ALTER TABLE public.palpites ADD CONSTRAINT evento_apostas_palpites_fk
FOREIGN KEY (evento_aposta_id)
REFERENCES public.evento_apostas (evento_aposta_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


ALTER TABLE public.palpites ADD CONSTRAINT taxas_palpites_fk
FOREIGN KEY (taxa_id)
REFERENCES public.taxas (taxa_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.palpites ADD CONSTRAINT bilhetes_palpites_fk
FOREIGN KEY (bilhete_id)
REFERENCES public.bilhetes (bilhete_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


# --- !Downs

DROP TABLE IF EXISTS palpites CASCADE;

DROP TABLE IF EXISTS bilhetes CASCADE;


DROP SEQUENCE IF EXISTS public.bilhetes_bilhete_id_seq CASCADE;

