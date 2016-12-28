import dominio.processadores.eventos.CampeonatoInserirProcessador;
import dominio.processadores.eventos.TimeInserirProcessador;
import models.vo.Tenant;
import play.Application;
import play.GlobalSettings;
import play.db.jpa.JPAApi;
import validators.eventos.CampeonatoNomeValidator;
import validators.eventos.TimeStringValidador;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        if(!app.isDev()) {
            return;
        }

        final JPAApi jpaApi = app.injector().instanceOf(JPAApi.class);

        jpaApi.withTransaction((em) -> {

            em.createQuery("DELETE FROM Validador v").executeUpdate();
            CampeonatoNomeValidator campeonatoNomeValidator = new CampeonatoNomeValidator(Tenant.SYSBET.get(),
                    CampeonatoInserirProcessador.REGRA,
                    null,
                    true,
                    null,
                    ".+");
            em.persist(campeonatoNomeValidator);
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
