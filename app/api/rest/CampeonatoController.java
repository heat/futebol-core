package api.rest;

import controllers.ApplicationController;
import dominio.processadores.eventos.CampeonatoAtualizarProcessador;
import dominio.processadores.eventos.CampeonatoInserirProcessador;
import models.eventos.Campeonato;
import models.vo.Chave;
import models.vo.Tenant;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.CampeonatoRepository;
import repositories.ValidadorRepository;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class CampeonatoController extends ApplicationController {


    CampeonatoRepository campeonatoRepository;
    CampeonatoInserirProcessador inserirProcessador;
    CampeonatoAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;

    @Inject
    public CampeonatoController(CampeonatoRepository campeonatoRepository, PlaySessionStore playSessionStore,
                                CampeonatoInserirProcessador inserirProcessador, CampeonatoAtualizarProcessador atualizarProcessador,
                                ValidadorRepository validadorRepository) {
        super(playSessionStore);
        this.campeonatoRepository = campeonatoRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        Campeonato campeonato = Json.fromJson(Controller.request()
                .body()
                .asJson(), Campeonato.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), CampeonatoInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant(), campeonato, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        return created(Json.toJson(campeonato));
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        Campeonato campeonato = Json.fromJson(Controller.request()
                .body()
                .asJson(), Campeonato.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), CampeonatoAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, campeonato, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        return ok("Campeonato " + campeonato.getNome() + " atualizado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {
        List todos = campeonatoRepository.todos(getTenant());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {
        Optional<Campeonato> campeonato = campeonatoRepository.buscar(getTenant(), id);

        if (!campeonato.isPresent()) {
            return notFound("Campeonato não encontrado!");
        }
        return ok(Json.toJson(campeonato));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {
        try {
            campeonatoRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando exclui uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }
}