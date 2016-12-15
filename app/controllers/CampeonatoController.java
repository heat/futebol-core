package controllers;

import models.vo.Tenant;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.CampeonatoRepository;

import javax.inject.Inject;
import java.util.List;

public class CampeonatoController extends Controller {


    CampeonatoRepository campeonatoRepository;

    @Inject
    public CampeonatoController(CampeonatoRepository campeonatoRepository) {
        this.campeonatoRepository = campeonatoRepository;
    }

    @Transactional
    public Result todos() {


        Long empresa = 0L;
        List todos = campeonatoRepository.todos(Tenant.of(1L));

        return ok(Json.toJson(todos));
    }
}
