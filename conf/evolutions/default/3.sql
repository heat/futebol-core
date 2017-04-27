# Validadores domain schema

# --- !Ups
CREATE SEQUENCE public.validadores_validador_id_seq START WITH 1000;

CREATE TABLE public.validadores (
                "validador_id" INTEGER NOT NULL DEFAULT nextval('public.validadores_validador_id_seq'),
                "tenant_id" INTEGER NOT NULL,
                "dtype" VARCHAR(31) NOT NULL,
                "regra" VARCHAR(255) NOT NULL,
                "nome" VARCHAR(255) NOT NULL,
                "situacao" CHAR(1) NOT NULL DEFAULT 'R',
                "valor_decimal" NUMERIC(10,2),
                "valor_inteiro" INTEGER,
                "valor_texto" VARCHAR(255),
                "valor_logico" BOOLEAN,
                CONSTRAINT "validadores_validador_id_pk" PRIMARY KEY ("validador_id")
);
COMMENT ON COLUMN public.validadores."regra" IS 'descrição das regras';

INSERT INTO public.validadores (tenant_id, dtype, regra, nome, situacao, valor_decimal, valor_inteiro, valor_texto, valor_logico) VALUES
(1, 'CampeonatoNomeValidator', 'campeonato.inserir', 'Nome Campeonato', 'R', null, null, '.+', true),
(1, 'TimeStringValidador', 'time.inserir', 'Nome Time', 'R', null, null, '.+', true),
(1, 'TimeStringValidador', 'time.atualizar', 'Nome Time', 'R', null, null, '.+', true),
(1, 'EventoDataValidador', 'evento.inserir', 'Data Evento', 'R', null, null, null, true),
(1, 'EventoDataValidador', 'evento.atualizar', 'Data Evento', 'R', null, null, null, true),
(1, 'EventoTimesDiferenteValidador', 'evento.inserir', 'Times Diferentes', 'R', null, null, null, true),
(1, 'VerificaTodosMomentosValidador', 'resultado.inserir', 'Todos os Momentos', 'R', null, null, null, true),
(1, 'CodigoBilhetePolitica', 'bilhete.inserir', 'Codigo Bilhete', 'R', null, null, 'xxx-xxxx-xxx-00', true),
(1, 'PerfilLocalidadeValidador', 'perfil.atualizar', 'Localidade', 'R', null, null, null, true),
(1, 'TempoCancelamentoValidador', 'bilhete.cancelar', 'Tempo Cancelamento Bilhete', 'E', null, 0, null, true),
(1, 'ValorMinimoApostaValidador', 'bilhete.inserir', 'Valor Mínimo Aposta', 'E', 10.0, null, null, true),
(1, 'HabilitadoApostasValidador', 'bilhete.inserir', 'Habilita Apostas', 'E', null, null, null, true),
(1, 'ValorMaximoApostaValidador', 'bilhete.inserir', 'Valor Máximo Aposta', 'E', 10000.0, null, null, true),
(1, 'QtdMinimaPalpitesValidador', 'bilhete.inserir', 'Qtd Mínima Palpites', 'E', null, 1, null, true),
(1, 'QtdMaximaPalpitesValidador', 'bilhete.inserir', 'Qtd Máxima Palpites', 'E', null, 10, null, true),
(1, 'HabilitadoUsuarioApostasVal', 'bilhete.inserir', 'Habilita Aposta Usuário', 'E', null, null, null, true),
(1, 'HabilitadoRevendedorApostasVal', 'bilhete.inserir', 'Habilita Aposta Usuário', 'E', null, null, null, true),
(1, 'HabilitadoAdminApostasVal', 'bilhete.inserir', 'Habilita Aposta Usuário', 'E', null, null, null, true),
(1, 'HabilitadoSupervisorApostasVal', 'bilhete.inserir', 'Habilita Aposta Usuário', 'E', null, null, null, true),
(1, 'TempoLimiteApostasValidador', 'bilhete.inserir', 'Tempo Limite Apostas', 'E', null, 0, null, true),
(1, 'PremioMaximoPolitica', 'bilhete.inserir', 'Premio Máximo', 'R', 10000.0, null, null, true),
(1, 'TaxaMaximaApostaPolitica', 'bilhete.inserir', 'Taxa Máxima de Aposta', 'R', 10.0, null, null, true);


# --- !Downs

DROP TABLE IF EXISTS public.validadores CASCADE;

DROP SEQUENCE IF EXISTS public.validadores_validador_id_seq CASCADE;