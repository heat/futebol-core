package repositories;

import com.google.inject.Inject;
import models.eventos.Campeonato;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.List;

public class CampeonatoRepository {

    private Long empresa;

    @Inject
    JPAApi jpaApi;

    private CampeonatoRepository(Long empresa) {
        this.empresa = empresa;
    }

    public List<Campeonato> todos() {
        return jpaApi.em().createQuery("select c from Campeonato c")
                .getResultList();
    }

    public static CampeonatoRepository of(Long empresa) {
        return new CampeonatoRepository(empresa);
    }
}
