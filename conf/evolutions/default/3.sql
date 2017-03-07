# Validadores domain schema

# --- !Ups
CREATE SEQUENCE public.validadores_validador_id_seq START WITH 1000;

CREATE TABLE public.validadores (
                "validador_id" INTEGER NOT NULL DEFAULT nextval('public.validadores_validador_id_seq'),
                "tenant_id" INTEGER NOT NULL,
                "dtype" VARCHAR(31) NOT NULL,
                "regra" VARCHAR(255) NOT NULL,
                "valor_decimal" NUMERIC(10,2),
                "valor_inteiro" INTEGER,
                "valor_texto" VARCHAR(255),
                "valor_logico" BOOLEAN,
                CONSTRAINT "validadores_validador_id_pk" PRIMARY KEY ("validador_id")
);
COMMENT ON COLUMN public.validadores."regra" IS 'descrição das regras';

# --- !Downs

DROP TABLE IF EXISTS public.validadores CASCADE;

DROP SEQUENCE IF EXISTS public.validadores_validador_id_seq CASCADE;