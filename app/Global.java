import dominio.processadores.eventos.CampeonatoInserirProcessador;
import dominio.processadores.eventos.TimeInserirProcessador;
import models.vo.Tenant;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPAApi;
import validators.Validador;
import validators.eventos.CampeonatoStringValidador;
import validators.eventos.EventoCampeonatoValidador;
import validators.eventos.TimeStringValidador;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        if(!app.isDev()) {
            return;
        }

        final JPAApi jpaApi = app.injector().instanceOf(JPAApi.class);

        jpaApi.withTransaction((em) -> {

            em.createQuery("DELETE FROM Validador v").executeUpdate();
            CampeonatoStringValidador campeonatoStringValidador = new CampeonatoStringValidador(Tenant.SYSBET.get(),
                    CampeonatoInserirProcessador.REGRA,
                    null,
                    true,
                    null,
                    ".+");
            em.persist(campeonatoStringValidador);
            TimeStringValidador timeStringValidador = new TimeStringValidador(Tenant.SYSBET.get(),
                    TimeInserirProcessador.REGRA,
                    null,
                    true,
                    null,
                    ".+");
            em.persist(timeStringValidador);
            return jpaApi;
        });
    }
}
