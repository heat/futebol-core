package controllers;

import com.google.inject.Inject;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.EntityManager;
import java.util.List;

public class CampeonatoController extends Controller {

    JPAApi jpaApi;

    @Inject
    public CampeonatoController(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Transactional
    public Result todos() {
        EntityManager em = jpaApi.em();

        List todos = em.createQuery("select c from Campeonato c")
                .getResultList();

        return ok(Json.toJson(todos));
    }
}
