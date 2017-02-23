import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.processadores.eventos.*;
import dominio.processadores.usuarios.PerfilAtualizarProcessador;
import dominio.validadores.bilhete.CodigoBilhetePolitica;
import dominio.validadores.eventos.*;
import dominio.validadores.usuarios.PerfilLocalidadeValidador;
import models.apostas.Apostavel;
import models.apostas.EventoAposta;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.eventos.Time;
import models.seguranca.Usuario;
import models.vo.Tenant;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

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

            dummyData(em);

            return jpaApi;
        });


    }

    private void dummyData(EntityManager em) {

        em.createQuery("DELETE FROM Palpite t").executeUpdate();
        em.createQuery("DELETE FROM Bilhete t").executeUpdate();
        em.createQuery("DELETE FROM Taxa t").executeUpdate();
        em.createQuery("DELETE FROM Odd t").executeUpdate();
        em.createQuery("DELETE FROM EventoAposta t").executeUpdate();
        em.createQuery("DELETE FROM Evento t").executeUpdate();
        em.createQuery("DELETE FROM Time t").executeUpdate();
        em.createQuery("DELETE FROM Campeonato t").executeUpdate();

        Time palmeiras = new Time(Tenant.SYSBET.get(), "Palmeiras");
        Time coritiba = new Time(Tenant.SYSBET.get(), "Coritiba");
        em.persist(palmeiras);
        em.persist(coritiba);

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
        dataJogo.add(Calendar.HOUR, random.nextInt(10));
        evento.setDataEvento(dataJogo);

        em.persist(evento);

        Odd odd = new Odd();
        odd.setAbreviacao("casa");
        odd.setDescricao("casa");
        odd.setMercado("casa");
        odd.setNome("casa");
        odd.setPosicao(1L);
        odd.setPrioridade(1L);
        odd.setTipoLinha('N');
        odd.setTenant(Tenant.SYSBET.get());
        odd.setFavorita(true);

        em.persist(odd);

        EventoAposta eventoAposta = new EventoAposta();
        eventoAposta.setEvento(evento);
        eventoAposta.setPermitir(true);
        eventoAposta.setSituacao(Apostavel.Situacao.A);
        eventoAposta.setTenant(Tenant.SYSBET.get());

        em.persist(eventoAposta);

        eventoAposta.addTaxa(new Taxa(Tenant.SYSBET.get(), odd, BigDecimal.ONE, BigDecimal.ZERO));

        em.merge(eventoAposta);

        Bilhete bilhete = new Bilhete();
        bilhete.setCliente("Sysbet");
        bilhete.setCodigo("ERT-4G12-RF3");
        bilhete.setSituacao(Bilhete.Situacao.A);
        bilhete.setTenant(Tenant.SYSBET.get());
        bilhete.setValorAposta(BigDecimal.TEN);
        bilhete.setValorPremio(BigDecimal.valueOf(1000L));
        Usuario usuario = em.find(Usuario.class, 1L);
        bilhete.setUsuario(usuario);
        bilhete.setAlteradoEm(Calendar.getInstance());
        bilhete.setCriadoEm(Calendar.getInstance());

        em.persist(bilhete);

        /*Palpite palpite = new Palpite();
        palpite.setTenant(Tenant.SYSBET.get());
        palpite.setStatus(Palpite.Status.A);
        palpite.setTaxa(eventoAposta.getTaxas().get(0));
        palpite.setValorTaxa(BigDecimal.TEN);

        em.persist(palpite);*/
    }
}
