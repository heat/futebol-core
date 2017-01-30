import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.processadores.eventos.*;
import dominio.validadores.bilhete.CodigoBilhetePolitica;
import dominio.validadores.eventos.*;
import models.apostas.Apostavel;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.eventos.Time;
import models.vo.Tenant;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.Calendar;
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
            return jpaApi;
        });
    }

    private void dummyData(EntityManager em) {
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
        evento.setSituacao(Apostavel.Situacao.A);
        Calendar dataJogo = Calendar.getInstance();
        dataJogo.add(Calendar.HOUR, random.nextInt(10));
        evento.setDataEvento(dataJogo);

        em.persist(evento);
    }
}
