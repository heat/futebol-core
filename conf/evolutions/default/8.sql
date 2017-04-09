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
-- CNLFJKE2SQCTSNKQBN8TG6M86FW2UC8C demo front
-- 4VX796TEPA8K7CXGBRNCTN2FXNNMM2C6 demo admin
--

INSERT INTO public.registro_aplicativo (app_key, nome, autor, email, telefone, tenant_id, scope) VALUES
  ('CNLFJKE2SQCTSNKQBN8TG6M86FW2UC8C', 'SYSBET APP APOSTA', 'Kley', 'kley@sysbet.in', '123451', 1, 'aposta:static'),
  ('4VX796TEPA8K7CXGBRNCTN2FXNNMM2C6', 'SYSBET APP ADMIN', 'Kley', 'kley@sysbet.in', '123451', 1, 'aposta:session');

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

CREATE SEQUENCE public.importacao_id_seq;

CREATE TABLE public.importacao
(
  importacao_id INTEGER NOT NULL DEFAULT nextval('public.importacao_id_seq'),
  chave VARCHAR(255) NOT NULL,
  criado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  alterado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  tenant_id INTEGER NOT NULL,
  situacao CHAR(1) NOT NULL,
  variacao DECIMAL(10,2) NOT NULL,
  evento_id INTEGER NOT NULL,
  CONSTRAINT importacao_pk PRIMARY KEY (importacao_id),
  CONSTRAINT importacao_eventos_fk FOREIGN KEY (evento_id)
  REFERENCES public.eventos (evento_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE public.solicitacao_saldo_id_seq;

CREATE TABLE public.solicitacao_saldo
(
  solicitacao_saldo_id INTEGER NOT NULL DEFAULT nextval('public.solicitacao_saldo_id_seq'),
  conta_id INTEGER NOT NULL,
  criado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valor DECIMAL(10,2) NOT NULL,
  CONSTRAINT solicitacao_saldo_pk PRIMARY KEY (solicitacao_saldo_id),
  CONSTRAINT solicitacao_saldo_conta_fk FOREIGN KEY (conta_id)
  REFERENCES public.contas (conta_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE public.transferencia_saldo_id_seq;

CREATE TABLE public.transferencia_saldo
(
  transferencia_saldo_id INTEGER NOT NULL DEFAULT nextval('public.transferencia_saldo_id_seq'),
  conta_origem INTEGER NOT NULL,
  conta_destino INTEGER NOT NULL,
  criado_em TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  valor DECIMAL(10,2) NOT NULL,
  CONSTRAINT transferencia_saldo_pk PRIMARY KEY (transferencia_saldo_id),
  CONSTRAINT transferencia_saldo_conta_origem_fk FOREIGN KEY (conta_origem)
  REFERENCES public.contas (conta_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT transferencia_saldo_conta_destino_fk FOREIGN KEY (conta_destino)
  REFERENCES public.contas (conta_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

INSERT INTO odds VALUES (1, 'resultado-final.casa', 'CasaResultadoFinalOdd', NULL);
INSERT INTO odds VALUES (2, 'resultado-final.empate', 'EmpateResultadoFinalOdd', NULL);
INSERT INTO odds VALUES (3, 'resultado-final.fora', 'ForaResultadoFinalOdd', NULL);
INSERT INTO odds VALUES (4, 'aposta-dupla.casa_empate', 'CasaEmpateApostaDuplaOdd', NULL);
INSERT INTO odds VALUES (5, 'aposta-dupla.empate_fora', 'EmpateForaApostaDuplaOdd', NULL);
INSERT INTO odds VALUES (6, 'aposta-dupla.casa_fora', 'CasaForaApostaDuplaOdd', NULL);
INSERT INTO odds VALUES (7, 'handicap-asiatico.casa', 'CasaHandicapAsiaticoOdd', NULL);
INSERT INTO odds VALUES (8, 'handicap-asiatico.fora', 'ForaHandicapAsiaticoOdd', NULL);
INSERT INTO odds VALUES (9, 'numero-gols.acima', 'AcimaNumeroGolsOdd', NULL);
INSERT INTO odds VALUES (10, 'numero-gols.abaixo', 'AbaixoNumeroGolsOdd', NULL);

INSERT INTO configuracao_odd VALUES (1, 1, 1, true, '0', 0.00, 1);
INSERT INTO configuracao_odd VALUES (2, 1, 2, true, '0', 0.00, 1);
INSERT INTO configuracao_odd VALUES (3, 1, 3, true, '0', 0.00, 1);
INSERT INTO configuracao_odd VALUES (4, 1, 4, true, '0', 0.00, 1);
INSERT INTO configuracao_odd VALUES (5, 1, 5, true, '0', 0.00, 1);
INSERT INTO configuracao_odd VALUES (6, 1, 6, true, '0', 0.00, 1);
INSERT INTO configuracao_odd VALUES (7, 1, 7, true, '0', 0.00, 1);
INSERT INTO configuracao_odd VALUES (8, 1, 8, true, '0', 0.00, 1);
INSERT INTO configuracao_odd VALUES (9, 1, 9, true, '0', 0.00, 1);
INSERT INTO configuracao_odd VALUES (10, 1, 10, true, '0', 0.00, 1);

# --- !Downs

DROP TABLE IF EXISTS registro_aplicativo CASCADE;

DROP TABLE IF EXISTS pins CASCADE;

DROP TABLE IF EXISTS palpites_pin CASCADE;

DROP TABLE IF EXISTS importacao CASCADE;

DROP TABLE IF EXISTS solicitacao_saldo CASCADE;

DROP TABLE IF EXISTS transferencia_saldo CASCADE;

DROP SEQUENCE IF EXISTS public.transferencia_saldo_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.solicitacao_saldo_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.importacao_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.registro_aplicativo_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.pins_pin_id_seq CASCADE;

DROP SEQUENCE IF EXISTS public.palpites_pin_id_seq CASCADE;
