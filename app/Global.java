import dominio.processadores.eventos.*;
import dominio.validadores.eventos.*;
import models.eventos.Time;
import models.vo.Tenant;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        if(!app.isDev()) {
            return;
        }

        final JPAApi jpaApi = app.injector().instanceOf(JPAApi.class);

        jpaApi.withTransaction((em) -> {

            dummyData(em);
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
            return jpaApi;
        });
    }

    private void dummyData(EntityManager em) {
        Time palmeiras = new Time(Tenant.SYSBET, "Palmeiras");
        Time coritiba = new Time(Tenant.SYSBET, "Coritiba");
        em.persist(palmeiras);
        em.persist(coritiba);
    }
}
