import dominio.processadores.bilhetes.BilheteCancelarProcessador;
import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.processadores.eventos.*;
import dominio.processadores.usuarios.PerfilAtualizarProcessador;
import dominio.validadores.bilhete.*;
import dominio.validadores.eventos.*;
import dominio.validadores.usuarios.PerfilLocalidadeValidador;
import models.apostas.*;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoExatoMercado;
import models.apostas.mercado.ResultadoFinalMercado;
import models.apostas.odd.resultados.dupla.CasaEmpateApostaDuplaOdd;
import models.apostas.odd.resultados.dupla.CasaForaApostaDuplaOdd;
import models.apostas.odd.resultados.dupla.EmpateForaApostaDuplaOdd;
import models.apostas.odd.resultados.termino.CasaResultadoFinalOdd;
import models.apostas.odd.resultados.termino.EmpateResultadoFinalOdd;
import models.apostas.odd.resultados.termino.ForaResultadoFinalOdd;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.eventos.Time;
import models.financeiro.Conta;
import models.financeiro.comissao.ParametroComissao;
import models.financeiro.comissao.PlanoComissaoBilhete;
import models.seguranca.Usuario;
import models.vo.Parametro;
import models.vo.Tenant;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        if(!app.isDev()) {
            return;
        }

        final JPAApi jpaApi = app.injector().instanceOf(JPAApi.class);

        jpaApi.withTransaction((em) -> {

            //dummyData(em);
            em.createQuery("DELETE FROM Validador v").executeUpdate();
            em.createQuery("DELETE FROM Importacao t").executeUpdate();
            em.createQuery("DELETE FROM Lancamento t").executeUpdate();
            em.createQuery("DELETE FROM Comissao t").executeUpdate();
            em.createQuery("DELETE FROM Conta t").executeUpdate();
            em.createQuery("DELETE FROM Palpite t").executeUpdate();
            em.createQuery("DELETE FROM Bilhete t").executeUpdate();
            em.createQuery("DELETE FROM PalpitePin t").executeUpdate();
            em.createQuery("DELETE FROM Pin t").executeUpdate();
            em.createQuery("DELETE FROM Taxa t").executeUpdate();
            em.createQuery("DELETE FROM EventoAposta t").executeUpdate();
            em.createQuery("DELETE FROM Evento t").executeUpdate();
            em.createQuery("DELETE FROM Time t").executeUpdate();
            em.createQuery("DELETE FROM Campeonato t").executeUpdate();
            em.createQuery("UPDATE Usuario t SET t.planoComissao.id = NULL ").executeUpdate();
            em.createQuery("DELETE FROM ParametroComissao t").executeUpdate();
            em.createQuery("DELETE FROM PlanoComissao t").executeUpdate();
            em.createQuery("DELETE FROM OddConfiguracao v").executeUpdate();
            em.createQuery("DELETE FROM Odd v").executeUpdate();
            // é apenas uam capa pois a delegação da validação esta para o StringRegexValidador
            CampeonatoNomeValidator campeonatoNomeValidator = new CampeonatoNomeValidator(Tenant.SYSBET.get(),
                    CampeonatoInserirProcessador.REGRA,
                    null,
                    true,  // utilizamos o campo logico para indicar se é necessário validar
                    null,
                    ".+"); // utilizamos o campo string para montar um Regex de validação
            em.persist(campeonatoNomeValidator);
            TimeStringValidador timeInserirStringValidador = new TimeStringValidador(Tenant.SYSBET.get(),
                    TimeInserirProcessador.REGRA,
                    null,
                    true,
                    null,
                    ".+");
            em.persist(timeInserirStringValidador);
            TimeStringValidador timeAtualizarStringValidador = new TimeStringValidador(Tenant.SYSBET.get(),
                    TimeAtualizarProcessador.REGRA,
                    null,
                    true,
                    null,
                    ".+");
            em.persist(timeAtualizarStringValidador);
            EventoDataValidador eventoInserirDataValidador = new EventoDataValidador(Tenant.SYSBET.get(),
                    // Esse é um exemplo onde a mesma regra está para dois processadores diferentes
                    EventoInserirProcessador.REGRA,
                    null,
                    true,
                    null,
                    null
                    );
            em.persist(eventoInserirDataValidador);
            EventoDataValidador eventoAtualizarDataValidador = new EventoDataValidador(Tenant.SYSBET.get(),
                    EventoAtualizarProcessador.REGRA,
                    null,
                    true,
                    null,
                    null
            );
            em.persist(eventoAtualizarDataValidador);
            EventoTimesDiferenteValidador eventoTimesDiferenteValidador = new EventoTimesDiferenteValidador(Tenant.SYSBET.get(),
                    EventoInserirProcessador.REGRA,
                    null,
                    true,
                    null,
                    null);
            em.persist(eventoTimesDiferenteValidador);
            VerificaTodosMomentosValidador verificaTodosMomentosValidador =
                    new VerificaTodosMomentosValidador(Tenant.SYSBET.get(),
                    FinalizarEventoProcessador.REGRA,
                    null,
                    true,
                    null,
                    null);
            em.persist(verificaTodosMomentosValidador);
            CodigoBilhetePolitica codigoBilhetePolitica =
                    new CodigoBilhetePolitica( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            true,
                            null,
                            "xxx-xxxx-xxx-00");
            em.persist(codigoBilhetePolitica);

            PerfilLocalidadeValidador perfilLocalidadeValidador =
                    new PerfilLocalidadeValidador( Tenant.SYSBET.get(),
                            PerfilAtualizarProcessador.REGRA,
                            null,
                            true,
                            null,
                            null);
            em.persist(perfilLocalidadeValidador);

            TempoCancelamentoValidador tempoCancelamentoValidador =
                    new TempoCancelamentoValidador( Tenant.SYSBET.get(),
                            BilheteCancelarProcessador.REGRA,
                            0L,
                            null,
                            null,
                            null);
            em.persist(tempoCancelamentoValidador);

            ValorMinimoApostaValidador valorMinimoApostaValidador =
                    new ValorMinimoApostaValidador( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            null,
                            BigDecimal.TEN,
                            null);
            em.persist(valorMinimoApostaValidador);

            HabilitadoApostasValidador habilitadoApostasValidador =
                    new HabilitadoApostasValidador( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            true,
                            null,
                            null);
            em.persist(habilitadoApostasValidador);

            ValorMaximoApostaValidador valorMaximoApostaValidador =
                    new ValorMaximoApostaValidador( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            null,
                            BigDecimal.valueOf(1000),
                            null);
            em.persist(valorMaximoApostaValidador);

            QtdMinimaPalpitesValidador qtdMinimaPalpitesValidador =
                    new QtdMinimaPalpitesValidador( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            1L,
                            null,
                            null,
                            null);
            em.persist(qtdMinimaPalpitesValidador);

            QtdMaximaPalpitesValidador qtdMaximaPalpitesValidador =
                    new QtdMaximaPalpitesValidador( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            10L,
                            null,
                            null,
                            null);
            em.persist(qtdMaximaPalpitesValidador);

            HabilitadoUsuarioApostasVal habilitadoUsuarioApostasValidador =
                    new HabilitadoUsuarioApostasVal( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            true,
                            null,
                            null);
            em.persist(habilitadoUsuarioApostasValidador);

            HabilitadoRevendedorApostasVal habilitadoRevendedorApostasValidador =
                    new HabilitadoRevendedorApostasVal( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            true,
                            null,
                            null);
            em.persist(habilitadoRevendedorApostasValidador);

            HabilitadoAdminApostasVal habilitadoAdministradorApostasValidador =
                    new HabilitadoAdminApostasVal( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            true,
                            null,
                            null);
            em.persist(habilitadoAdministradorApostasValidador);

            HabilitadoSupervisorApostasVal habilitadoSupervisorApostasValidador =
                    new HabilitadoSupervisorApostasVal( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            true,
                            null,
                            null);
            em.persist(habilitadoSupervisorApostasValidador);

            TempoLimiteApostasValidador tempoLimiteApostasValidador =
                    new TempoLimiteApostasValidador( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            0L,
                            null,
                            null,
                            null);
            em.persist(tempoLimiteApostasValidador);

            PremioMaximoPolitica premioMaximoPolitica =
                    new PremioMaximoPolitica( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            true,
                            BigDecimal.valueOf(10000),
                            null);
            em.persist(premioMaximoPolitica);

            TaxaMaximaApostaPolitica taxaMaximaApostaPolitica =
                    new TaxaMaximaApostaPolitica( Tenant.SYSBET.get(),
                            BilheteInserirProcessador.REGRA,
                            null,
                            true,
                            BigDecimal.valueOf(100),
                            null);
            em.persist(taxaMaximaApostaPolitica);

            CasaResultadoFinalOdd casaResultadoFinalOdd =
                    new CasaResultadoFinalOdd("resultado-final.casa");
            em.persist(casaResultadoFinalOdd);

            EmpateResultadoFinalOdd empateResultadoFinalOdd =
                    new EmpateResultadoFinalOdd("resultado-final.empate");
            em.persist(empateResultadoFinalOdd);

            ForaResultadoFinalOdd foraResultadoFinalOdd =
                    new ForaResultadoFinalOdd("resultado-final.fora");
            em.persist(foraResultadoFinalOdd);

            CasaEmpateApostaDuplaOdd casaEmpateApostaDuplaOdd =
                    new CasaEmpateApostaDuplaOdd("aposta-dupla.casa_empate");
            em.persist(casaEmpateApostaDuplaOdd);

            EmpateForaApostaDuplaOdd empateForaApostaDuplaOdd =
                    new EmpateForaApostaDuplaOdd("aposta-dupla.empate_fora");
            em.persist(empateForaApostaDuplaOdd);

            CasaForaApostaDuplaOdd casaForaApostaDuplaOdd =
                    new CasaForaApostaDuplaOdd("aposta-dupla.casa_fora");
            em.persist(casaForaApostaDuplaOdd);


            dummyData(em);

            return jpaApi;
        });


    }

    private void dummyData(EntityManager em) {

        Time palmeiras = new Time(Tenant.SYSBET.get(), "Palmeiras");
        Time coritiba = new Time(Tenant.SYSBET.get(), "Coritiba");
        em.persist(palmeiras);
        em.persist(coritiba);

        //Não é possível inserir dois campeonatos com o mesmo nome
        Campeonato campeonato = new Campeonato(Tenant.SYSBET.get(), "Brasileirao", Campeonato.Situacao.A);

        em.persist(campeonato);

        Random random = new Random(System.currentTimeMillis());

        Evento evento = new Evento();
        evento.setTenant(Tenant.SYSBET.get());
        evento.setCasa(palmeiras);
        evento.setFora(coritiba);
        evento.setCampeonato(campeonato);
        evento.setSituacao(Evento.Situacao.A);
        Calendar dataJogo = Calendar.getInstance();
        dataJogo.add(Calendar.HOUR, random.nextInt(10));
        evento.setDataEvento(dataJogo);

        em.persist(evento);

        List<Odd> odds = em.createQuery("SELECT o FROM Odd o ").getResultList();

        EventoAposta eventoAposta = new EventoAposta();
        eventoAposta.setEvento(evento);
        eventoAposta.setPermitir(true);
        eventoAposta.setSituacao(Apostavel.Situacao.A);
        eventoAposta.setTenant(Tenant.SYSBET.get());

        em.persist(eventoAposta);

        eventoAposta.addTaxa(new Taxa(Tenant.SYSBET.get(), odds.get(0), BigDecimal.ONE, BigDecimal.ZERO));

        em.merge(eventoAposta);

        Bilhete bilhete = new Bilhete();
        bilhete.setCliente("Sysbet");
        bilhete.setCodigo("ERT-4G12-RF3");
        bilhete.setSituacao(Bilhete.Situacao.A);
        bilhete.setTenant(Tenant.SYSBET.get());
        bilhete.setValorAposta(BigDecimal.TEN);
        bilhete.setValorPremio(BigDecimal.valueOf(1000L));
        Usuario usuario = em.find(Usuario.class, 4L);
        bilhete.setUsuario(usuario);
        bilhete.setAlteradoEm(Calendar.getInstance());
        bilhete.setCriadoEm(Calendar.getInstance());

        em.persist(bilhete);

        Palpite palpite = new Palpite();
        palpite.setTenant(Tenant.SYSBET.get());
        palpite.setStatus(Palpite.Status.A);
        palpite.setTaxa(eventoAposta.getTaxas().get(0));
        palpite.setValorTaxa(BigDecimal.TEN);
        List<Palpite> palpites = new ArrayList<>();
        palpites.add(palpite);
        bilhete.setPalpites(palpites);

        em.merge(bilhete);

        Conta conta = new Conta();
        conta.setProprietario(usuario);

        em.persist(conta);

        PlanoComissaoBilhete planoComissaoBilhete = new PlanoComissaoBilhete();
        planoComissaoBilhete.setNome("ComissaoBilhete");

        ParametroComissao parametroComissao = new ParametroComissao();
        parametroComissao.setParametro(new Parametro(1L));
        parametroComissao.setValor(5L);

        planoComissaoBilhete.addParametros(parametroComissao);

        em.persist(planoComissaoBilhete);

        usuario.setPlanoComissao(planoComissaoBilhete);


        odds.forEach(o -> {
            OddConfiguracao oddConfiguracao = new OddConfiguracao(Tenant.SYSBET.get(), o, true, BigDecimal.ZERO, 1L);
            em.persist(oddConfiguracao);
        });



    }
}
