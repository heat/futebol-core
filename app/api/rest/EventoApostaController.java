package api.rest;

import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.apostas.EventoApostaAtualizarProcessador;
import dominio.processadores.apostas.EventoApostaInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.vo.Chave;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.EventoApostaRepository;
import repositories.ValidadorRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class EventoApostaController extends ApplicationController {

    EventoApostaRepository eventoApostaRepository;
    EventoApostaInserirProcessador inserirProcessador;
    EventoApostaAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;

    @Inject

    public EventoApostaController(PlaySessionStore playSessionStore, EventoApostaRepository eventoApostaRepository,
                                  EventoApostaInserirProcessador inserirProcessador, EventoApostaAtualizarProcessador atualizarProcessador,
                                  ValidadorRepository validadorRepository) {
        super(playSessionStore);
        this.eventoApostaRepository = eventoApostaRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
    }


    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        EventoAposta eventoAposta = Json.fromJson(Controller.request()
                .body()
                .asJson(), EventoAposta.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoApostaInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant(), eventoAposta, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        return created(Json.toJson(eventoAposta));
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        EventoAposta eventoAposta = Json.fromJson(Controller.request()
                .body()
                .asJson(), EventoAposta.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoApostaAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, eventoAposta, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        return ok("Aposta atualizada! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {
        List todos = eventoApostaRepository.todos(getTenant());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {
        Optional<EventoAposta> eventoAposta = eventoApostaRepository.buscar(getTenant(), id);

        if (!eventoAposta.isPresent()) {
            return notFound("Aposta não encontrada!");
        }
        return ok(Json.toJson(eventoAposta));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {
        try {
            eventoApostaRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando exclui uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }
    
    


}
