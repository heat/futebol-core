package api.rest;

import controllers.ApplicationController;
import dominio.processadores.eventos.EventoAtualizarProcessador;
import dominio.processadores.eventos.EventoInserirProcessador;
import models.eventos.Evento;
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
import repositories.EventoRepository;
import repositories.ValidadorRepository;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class EventoController extends ApplicationController{

    EventoRepository eventoRepository;
    EventoInserirProcessador inserirProcessador;
    EventoAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;

    @Inject
    public EventoController(EventoRepository eventoRepository, PlaySessionStore playSessionStore,
                            EventoInserirProcessador inserirProcessador, EventoAtualizarProcessador atualizarProcessador,
                            ValidadorRepository validadorRepository) {
        super(playSessionStore);

        this.eventoRepository = eventoRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        Evento evento = Json.fromJson(Controller.request().body().asJson(), Evento.class);
        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant(), evento, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        return created(Json.toJson(evento));
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {

        Evento evento = Json.fromJson(Controller.request()
                .body()
                .asJson(), Evento.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoInserirProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, evento, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        return ok("Evento atualizado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        List todos = eventoRepository.todos(getTenant());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        Optional<Evento> eventos = eventoRepository.buscar(getTenant(), id);
        if (!eventos.isPresent()) {
            return notFound("Evento não encontrado!");
        }
        return ok(Json.toJson(eventos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {

        try {
        eventoRepository.excluir(getTenant(), id);
            return noContent();
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }





}
