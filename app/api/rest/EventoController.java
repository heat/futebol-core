package api.rest;

import dominio.processadores.eventos.EventoAtualizarProcessador;
import dominio.processadores.eventos.EventoInserirProcessador;
import models.eventos.Evento;
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
import play.mvc.Result;
import repositories.EventoRepository;
import repositories.ValidadorRepository;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class EventoController extends Controller{

    EventoRepository eventoRepository;
    PlaySessionStore playSessionStore;
    EventoInserirProcessador inserirProcessador;
    EventoAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;

    @Inject
    public EventoController(EventoRepository eventoRepository, PlaySessionStore playSessionStore,
                            EventoInserirProcessador inserirProcessador, EventoAtualizarProcessador atualizarProcessador,
                            ValidadorRepository validadorRepository) {
        this.eventoRepository = eventoRepository;
        this.playSessionStore = playSessionStore;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        if (!getProfile().isPresent()) return forbidden();

        Evento evento = Json.fromJson(Controller.request().body().asJson(), Evento.class);
        List<Validador> validadores = validadorRepository.todos(getTenant().get(), EventoInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant().get(), evento, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        return ok("Evento cadastrado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {

        if (!getProfile().isPresent()) return forbidden();

        Evento evento = Json.fromJson(Controller.request().body().asJson(), Evento.class);

        List<Validador> validadores = validadorRepository.todos(getTenant().get(), EventoInserirProcessador.REGRA);

        try {
            atualizarProcessador.executar(getTenant().get(), evento, validadores, id);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        return ok("Evento atualizado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        if (!getProfile().isPresent()) return forbidden();

        List todos = eventoRepository.todos(getTenant().get());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        if (!getProfile().isPresent()) return forbidden();

        Optional<Evento> todos = eventoRepository.buscar(getTenant().get(), id);

        if (!todos.isPresent()) {
            return notFound("Evento não encontrado!");
        }
        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {

        try {

            if (!getProfile().isPresent()) return forbidden();

            eventoRepository.excluir(getTenant().get(), id);
            return ok("Evento excluído!");
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }

    private Optional<Tenant> getTenant(){

        Optional<CommonProfile> commonProfile = getProfile();
        CommonProfile profile = commonProfile.get();
        return Optional.ofNullable(Tenant.of((Long) profile.getAttribute("TENANT_ID")));
    }

    private Optional<CommonProfile> getProfile() {
        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        return profileManager.get(true);
    }

}
