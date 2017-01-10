package api.rest;

import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.bilhetes.BilheteAtualizarProcessador;
import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.vo.Chave;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.BilheteRepository;
import repositories.ValidadorRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class BilheteController extends ApplicationController {

    BilheteRepository bilheteRepository;
    BilheteInserirProcessador inserirProcessador;
    BilheteAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;

    @Inject

    public BilheteController(PlaySessionStore playSessionStore, BilheteRepository bilheteRepository,
                             BilheteInserirProcessador inserirProcessador, BilheteAtualizarProcessador atualizarProcessador,
                             ValidadorRepository validadorRepository) {
        super(playSessionStore);
        this.bilheteRepository = bilheteRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
    }


    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        Bilhete bilhete = Json.fromJson(Controller.request()
                .body()
                .asJson(), Bilhete.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), BilheteInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant(), bilhete, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        return created(Json.toJson(bilhete));
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        Bilhete bilhete = Json.fromJson(Controller.request()
                .body()
                .asJson(), Bilhete.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), BilheteAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, bilhete, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        return ok("Bilhete atualizada! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {
        List todos = bilheteRepository.todos(getTenant());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {
        Optional<Bilhete> bilhete = bilheteRepository.buscar(getTenant(), id);

        if (!bilhete.isPresent()) {
            return notFound("Bilhete não encontrado!");
        }
        return ok(Json.toJson(bilhete));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {
        try {
            //TODO: o excluir é somente cancelar
            bilheteRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando exclui uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }
    
    


}
