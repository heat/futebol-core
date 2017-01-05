package api.rest;

import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.bilhetes.PalpiteAtualizarProcessador;
import dominio.processadores.bilhetes.PalpiteInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Palpite;
import models.vo.Chave;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.PalpiteRepository;
import repositories.ValidadorRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class PalpiteController extends ApplicationController {

    PalpiteRepository palpiteRepository;
    PalpiteInserirProcessador inserirProcessador;
    PalpiteAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;

    @Inject

    public PalpiteController(PlaySessionStore playSessionStore, PalpiteRepository palpiteRepository,
                             PalpiteInserirProcessador inserirProcessador, PalpiteAtualizarProcessador atualizarProcessador,
                             ValidadorRepository validadorRepository) {
        super(playSessionStore);
        this.palpiteRepository = palpiteRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
    }


    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        Palpite palpite = Json.fromJson(Controller.request()
                .body()
                .asJson(), Palpite.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), PalpiteInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant(), palpite, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        return created(Json.toJson(palpite));
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        Palpite palpite = Json.fromJson(Controller.request()
                .body()
                .asJson(), Palpite.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), PalpiteAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, palpite, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        return ok("Palpite atualizada! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {
        List todos = palpiteRepository.todos(getTenant());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {
        Optional<Palpite> palpite = palpiteRepository.buscar(getTenant(), id);

        if (!palpite.isPresent()) {
            return notFound("Palpite não encontrado!");
        }
        return ok(Json.toJson(palpite));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {
        try {
            palpiteRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando exclui uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }
}
