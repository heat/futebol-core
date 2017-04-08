# Bilhete domain schema

# --- !Ups

CREATE SEQUENCE public.contas_conta_id_seq;

CREATE TABLE public.contas (
                conta_id INTEGER NOT NULL DEFAULT nextval('public.contas_conta_id_seq'),
                usuario_id INTEGER NOT NULL,
                CONSTRAINT contas_pk PRIMARY KEY (conta_id)
);

ALTER TABLE public.contas ADD CONSTRAINT contas_usuarios_fk
FOREIGN KEY (usuario_id)
REFERENCES public.usuarios (usuario_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE SEQUENCE public.lancamentos_lancamento_id_seq;

CREATE TABLE public.lancamentos (
                lancamento_id INTEGER NOT NULL DEFAULT nextval('public.lancamentos_lancamento_id_seq'),
                conta_id INTEGER NOT NULL,
                valor DECIMAL(10,2) NOT NULL,
                saldo DECIMAL(10,2) NOT NULL,
                dtype VARCHAR(31) NOT NULL,
                criado_em TIMESTAMP NOT NULL,
		origem_bilhete INTEGER,
		origem_comissao INTEGER,
		origem_solicitacao_saldo INTEGER,
		origem_transferencia_credito INTEGER,
		origem_transferencia_debito INTEGER,
                CONSTRAINT lancamentos_pk PRIMARY KEY (lancamento_id)
);

ALTER TABLE public.lancamentos ADD CONSTRAINT lancamentos_contas_fk
FOREIGN KEY (conta_id)
REFERENCES public.contas (conta_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;


# --- !Downs

DROP TABLE IF EXISTS lancamentos CASCADE;

DROP TABLE IF EXISTS contas CASCADE;

DROP SEQUENCE IF EXISTS public.lancamentos_lancamento_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.contas_conta_id_seq CASCADE;

