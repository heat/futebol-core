import dominio.processadores.bilhetes.BilheteCancelarProcessador;
import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.processadores.eventos.*;
import dominio.processadores.usuarios.PerfilAtualizarProcessador;
import dominio.validadores.Validador;
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
import models.apostas.odd.resultados.exato.ResultadoExatoOdd;
import models.apostas.odd.resultados.gols.AbaixoNumeroGolsOdd;
import models.apostas.odd.resultados.gols.AcimaNumeroGolsOdd;
import models.apostas.odd.resultados.handicap.CasaHandicapAsiaticoOdd;
import models.apostas.odd.resultados.handicap.ForaHandicapAsiaticoOdd;
import models.apostas.odd.resultados.termino.CasaResultadoFinalOdd;
import models.apostas.odd.resultados.termino.EmpateResultadoFinalOdd;
import models.apostas.odd.resultados.termino.ForaResultadoFinalOdd;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.eventos.Resultado;
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
            em.createQuery("DELETE FROM Resultado v").executeUpdate();
            em.createQuery("DELETE FROM Importacao t").executeUpdate();
            em.createQuery("DELETE FROM Lancamento t").executeUpdate();
            em.createQuery("DELETE FROM Comissao t").executeUpdate();
            em.createQuery("DELETE FROM DocumentoTransferencia t").executeUpdate();
            em.createQuery("DELETE FROM SolicitacaoSaldo t").executeUpdate();
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

            CasaHandicapAsiaticoOdd casaHandicapAsiaticoOdd =
                    new CasaHandicapAsiaticoOdd("handicap-asiatico.casa");
            em.persist(casaHandicapAsiaticoOdd);

            ForaHandicapAsiaticoOdd foraHandicapAsiaticoOdd =
                    new ForaHandicapAsiaticoOdd("handicap-asiatico.fora");
            em.persist(foraHandicapAsiaticoOdd);

            AcimaNumeroGolsOdd acimaNumeroGolsOdd =
                    new AcimaNumeroGolsOdd("numero-gols.acima");
            em.persist(acimaNumeroGolsOdd);

            AbaixoNumeroGolsOdd abaixoNumeroGolsOdd =
                    new AbaixoNumeroGolsOdd("numero-gols.abaixo");
            em.persist(abaixoNumeroGolsOdd);

            ResultadoExatoOdd resultadoExatoOdd0x0 =
                    new ResultadoExatoOdd("resultado-exato.c0x0", ResultadoExatoMercado.Posicao.c0x0);
            em.persist(resultadoExatoOdd0x0);

            ResultadoExatoOdd resultadoExatoOdd0x1 =
                    new ResultadoExatoOdd("resultado-exato.c0x1", ResultadoExatoMercado.Posicao.c0x1);
            em.persist(resultadoExatoOdd0x1);

            ResultadoExatoOdd resultadoExatoOdd0x2 =
                    new ResultadoExatoOdd("resultado-exato.c0x2", ResultadoExatoMercado.Posicao.c0x2);
            em.persist(resultadoExatoOdd0x2);

            ResultadoExatoOdd resultadoExatoOdd0x3 =
                    new ResultadoExatoOdd("resultado-exato.c0x3", ResultadoExatoMercado.Posicao.c0x3);
            em.persist(resultadoExatoOdd0x3);

            ResultadoExatoOdd resultadoExatoOdd0x4 =
                    new ResultadoExatoOdd("resultado-exato.c0x4", ResultadoExatoMercado.Posicao.c0x4);
            em.persist(resultadoExatoOdd0x4);

            ResultadoExatoOdd resultadoExatoOdd0x5 =
                    new ResultadoExatoOdd("resultado-exato.c0x5", ResultadoExatoMercado.Posicao.c0x5);
            em.persist(resultadoExatoOdd0x5);

            ResultadoExatoOdd resultadoExatoOdd1x0 =
                    new ResultadoExatoOdd("resultado-exato.c1x0", ResultadoExatoMercado.Posicao.c1x0);
            em.persist(resultadoExatoOdd1x0);

            ResultadoExatoOdd resultadoExatoOdd1x1 =
                    new ResultadoExatoOdd("resultado-exato.c1x1", ResultadoExatoMercado.Posicao.c1x1);
            em.persist(resultadoExatoOdd1x1);

            ResultadoExatoOdd resultadoExatoOdd1x2 =
                    new ResultadoExatoOdd("resultado-exato.c1x2", ResultadoExatoMercado.Posicao.c1x2);
            em.persist(resultadoExatoOdd1x2);

            ResultadoExatoOdd resultadoExatoOdd1x3 =
                    new ResultadoExatoOdd("resultado-exato.c1x3", ResultadoExatoMercado.Posicao.c1x3);
            em.persist(resultadoExatoOdd1x3);

            ResultadoExatoOdd resultadoExatoOdd1x4 =
                    new ResultadoExatoOdd("resultado-exato.c1x4", ResultadoExatoMercado.Posicao.c1x4);
            em.persist(resultadoExatoOdd1x4);

            ResultadoExatoOdd resultadoExatoOdd1x5 =
                    new ResultadoExatoOdd("resultado-exato.c1x5", ResultadoExatoMercado.Posicao.c1x5);
            em.persist(resultadoExatoOdd1x5);

            ResultadoExatoOdd resultadoExatoOdd2x0 =
                    new ResultadoExatoOdd("resultado-exato.c2x0", ResultadoExatoMercado.Posicao.c2x0);
            em.persist(resultadoExatoOdd2x0);

            ResultadoExatoOdd resultadoExatoOdd2x1 =
                    new ResultadoExatoOdd("resultado-exato.c2x1", ResultadoExatoMercado.Posicao.c2x1);
            em.persist(resultadoExatoOdd2x1);

            ResultadoExatoOdd resultadoExatoOdd2x2 =
                    new ResultadoExatoOdd("resultado-exato.c2x2", ResultadoExatoMercado.Posicao.c2x2);
            em.persist(resultadoExatoOdd2x2);

            ResultadoExatoOdd resultadoExatoOdd2x3 =
                    new ResultadoExatoOdd("resultado-exato.c2x3", ResultadoExatoMercado.Posicao.c2x3);
            em.persist(resultadoExatoOdd2x3);

            ResultadoExatoOdd resultadoExatoOdd2x4 =
                    new ResultadoExatoOdd("resultado-exato.c2x4", ResultadoExatoMercado.Posicao.c2x4);
            em.persist(resultadoExatoOdd2x4);

            ResultadoExatoOdd resultadoExatoOdd2x5 =
                    new ResultadoExatoOdd("resultado-exato.c2x5", ResultadoExatoMercado.Posicao.c2x5);
            em.persist(resultadoExatoOdd2x5);

            ResultadoExatoOdd resultadoExatoOdd3x0 =
                    new ResultadoExatoOdd("resultado-exato.c3x0", ResultadoExatoMercado.Posicao.c3x0);
            em.persist(resultadoExatoOdd3x0);

            ResultadoExatoOdd resultadoExatoOdd3x1 =
                    new ResultadoExatoOdd("resultado-exato.c3x1", ResultadoExatoMercado.Posicao.c3x1);
            em.persist(resultadoExatoOdd3x1);

            ResultadoExatoOdd resultadoExatoOdd3x2 =
                    new ResultadoExatoOdd("resultado-exato.c3x2", ResultadoExatoMercado.Posicao.c3x2);
            em.persist(resultadoExatoOdd3x2);

            ResultadoExatoOdd resultadoExatoOdd3x3 =
                    new ResultadoExatoOdd("resultado-exato.c3x3", ResultadoExatoMercado.Posicao.c3x3);
            em.persist(resultadoExatoOdd3x3);

            ResultadoExatoOdd resultadoExatoOdd3x4 =
                    new ResultadoExatoOdd("resultado-exato.c3x4", ResultadoExatoMercado.Posicao.c3x4);
            em.persist(resultadoExatoOdd3x4);

            ResultadoExatoOdd resultadoExatoOdd3x5 =
                    new ResultadoExatoOdd("resultado-exato.c3x5", ResultadoExatoMercado.Posicao.c3x5);
            em.persist(resultadoExatoOdd3x5);

            ResultadoExatoOdd resultadoExatoOdd4x0 =
                    new ResultadoExatoOdd("resultado-exato.c4x0", ResultadoExatoMercado.Posicao.c4x0);
            em.persist(resultadoExatoOdd4x0);

            ResultadoExatoOdd resultadoExatoOdd4x1 =
                    new ResultadoExatoOdd("resultado-exato.c4x1", ResultadoExatoMercado.Posicao.c4x1);
            em.persist(resultadoExatoOdd4x1);

            ResultadoExatoOdd resultadoExatoOdd4x2 =
                    new ResultadoExatoOdd("resultado-exato.c4x2", ResultadoExatoMercado.Posicao.c4x2);
            em.persist(resultadoExatoOdd4x2);

            ResultadoExatoOdd resultadoExatoOdd4x3 =
                    new ResultadoExatoOdd("resultado-exato.c4x3", ResultadoExatoMercado.Posicao.c4x3);
            em.persist(resultadoExatoOdd4x3);

            ResultadoExatoOdd resultadoExatoOdd4x4 =
                    new ResultadoExatoOdd("resultado-exato.c4x4", ResultadoExatoMercado.Posicao.c4x4);
            em.persist(resultadoExatoOdd4x4);

            ResultadoExatoOdd resultadoExatoOdd4x5 =
                    new ResultadoExatoOdd("resultado-exato.c4x5", ResultadoExatoMercado.Posicao.c4x5);
            em.persist(resultadoExatoOdd4x5);

            ResultadoExatoOdd resultadoExatoOdd5x0 =
                    new ResultadoExatoOdd("resultado-exato.c5x0", ResultadoExatoMercado.Posicao.c5x0);
            em.persist(resultadoExatoOdd5x0);

            ResultadoExatoOdd resultadoExatoOdd5x1 =
                    new ResultadoExatoOdd("resultado-exato.c5x1", ResultadoExatoMercado.Posicao.c5x1);
            em.persist(resultadoExatoOdd5x1);

            ResultadoExatoOdd resultadoExatoOdd5x2 =
                    new ResultadoExatoOdd("resultado-exato.c5x2", ResultadoExatoMercado.Posicao.c5x2);
            em.persist(resultadoExatoOdd5x2);

            ResultadoExatoOdd resultadoExatoOdd5x3 =
                    new ResultadoExatoOdd("resultado-exato.c5x3", ResultadoExatoMercado.Posicao.c5x3);
            em.persist(resultadoExatoOdd5x3);

            ResultadoExatoOdd resultadoExatoOdd5x4 =
                    new ResultadoExatoOdd("resultado-exato.c5x4", ResultadoExatoMercado.Posicao.c5x4);
            em.persist(resultadoExatoOdd5x4);

            ResultadoExatoOdd resultadoExatoOdd5x5 =
                    new ResultadoExatoOdd("resultado-exato.c5x5", ResultadoExatoMercado.Posicao.c5x5);
            em.persist(resultadoExatoOdd5x5);

            dummyData(em);

            return jpaApi;
        });


    }

    private void dummyData(EntityManager em) {

        Time palmeiras = new Time(Tenant.SYSBET.get(), "Palmeiras");
        Time coritiba = new Time(Tenant.SYSBET.get(), "Coritiba");
        Time gremio = new Time(Tenant.SYSBET.get(), "Gremio");
        Time inter = new Time(Tenant.SYSBET.get(), "Inter");
        Time santos = new Time(Tenant.SYSBET.get(), "Santos");
        Time vasco = new Time(Tenant.SYSBET.get(), "Vasco");
        em.persist(palmeiras);
        em.persist(coritiba);
        em.persist(gremio);
        em.persist(inter);
        em.persist(santos);
        em.persist(vasco);

        //Não é possível inserir dois campeonatos com o mesmo nome
        Campeonato campeonato = new Campeonato(Tenant.SYSBET.get(), "Brasileirao");

        em.persist(campeonato);

        Random random = new Random(System.currentTimeMillis());

        Evento evento = new Evento();
        evento.setTenant(Tenant.SYSBET.get());
        evento.setCasa(palmeiras);
        evento.setFora(coritiba);
        evento.setCampeonato(campeonato);
        evento.setSituacao(Evento.Situacao.A);
        Calendar dataJogo = Calendar.getInstance();
        dataJogo.add(Calendar.HOUR, 10);
        evento.setDataEvento(dataJogo);
        evento.setModalidade(Evento.Modalidade.FUTEBOL);


        em.persist(evento);

        evento.addResultado(new Resultado(Tenant.SYSBET.get(), Resultado.Momento.I, 0L, palmeiras));
        evento.addResultado(new Resultado(Tenant.SYSBET.get(), Resultado.Momento.I, 0L, coritiba));
        evento.addResultado(new Resultado(Tenant.SYSBET.get(), Resultado.Momento.F, 0L, palmeiras));
        evento.addResultado(new Resultado(Tenant.SYSBET.get(), Resultado.Momento.F, 0L, coritiba));
        List<Odd> odds = em.createQuery("SELECT o FROM Odd o ").getResultList();

        EventoAposta eventoAposta = new EventoAposta(evento);
        eventoAposta.setPermitir(true);
        eventoAposta.setSituacao(Apostavel.Situacao.A);
        eventoAposta.setTenant(Tenant.SYSBET.get());

        em.persist(eventoAposta);

        eventoAposta.addTaxa(new Taxa(Tenant.SYSBET.get(), odds.get(0), BigDecimal.valueOf(2.23), BigDecimal.ZERO));

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

        Usuario usuario1 = em.find(Usuario.class, 3L);

        Conta conta1 = new Conta();
        conta1.setProprietario(usuario1);

        em.persist(conta1);

        PlanoComissaoBilhete planoComissaoBilhete = new PlanoComissaoBilhete();
        planoComissaoBilhete.setNome("ComissaoBilhete");

        ParametroComissao parametroComissao = new ParametroComissao();
        parametroComissao.setParametro(new Parametro(1L));
        parametroComissao.setValor(5L);

        planoComissaoBilhete.addParametros(parametroComissao);

        em.persist(planoComissaoBilhete);

        usuario.setPlanoComissao(planoComissaoBilhete);

        List<String> favoritas = Arrays.asList("resultado-final.casa", "resultado-final.empate", "resultado-final.fora");
        odds.forEach(o -> {
            boolean favorita = favoritas.contains(o.getCodigo());
            OddConfiguracao oddConfiguracao = new OddConfiguracao(Tenant.SYSBET.get(), o, favorita, BigDecimal.ZERO, 1L);
            em.persist(oddConfiguracao);
        });



    }
}
