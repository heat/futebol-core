package controllers;

import com.google.inject.Inject;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.CampeonatoRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class CampeonatoController extends Controller {



    @Transactional
    public Result todos() {


        Long empresa = 0L;
        List todos = CampeonatoRepository.of(empresa)
                .todos();

        return ok(Json.toJson(todos));
    }
}
