package api.rest;

import dominio.processadores.eventos.TimeAtualizarProcessador;
import dominio.processadores.eventos.TimeInserirProcessador;
import models.eventos.Time;
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
import play.mvc.Result;
import repositories.EventoRepository;
import repositories.TimeRepository;
import repositories.ValidadorRepository;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TimeController extends Controller{

    TimeRepository timeRepository;
    PlaySessionStore playSessionStore;
    TimeInserirProcessador inserirProcessador;
    TimeAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;
    EventoRepository eventoRepository;

    @Inject
    public TimeController(TimeRepository timeRepository, PlaySessionStore playSessionStore,
                          TimeInserirProcessador inserirProcessador, TimeAtualizarProcessador atualizarProcessador,
                          ValidadorRepository validadorRepository, EventoRepository eventoRepository) {
        this.timeRepository = timeRepository;
        this.playSessionStore = playSessionStore;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
        this.eventoRepository = eventoRepository;

    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() throws IOException {

        if (!getProfile().isPresent()) return forbidden();

        Time time = Json.fromJson(Controller.request().body().asJson(), Time.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), TimeInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant(), time,  validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        return ok("Time cadastrado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {

        if (!getProfile().isPresent()) return forbidden();

        Time Time = Json.fromJson(Controller.request().body().asJson(), Time.class);
        List<Validador> validadores = validadorRepository.todos(getTenant(), TimeAtualizarProcessador.REGRA);
        atualizarProcessador = new TimeAtualizarProcessador(timeRepository);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, Time, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        return ok("Time atualizado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        if (!getProfile().isPresent()) return forbidden();

        List todos = timeRepository.todos(getTenant());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        if (!getProfile().isPresent()) return forbidden();

        Optional<Time> todos = timeRepository.buscar(getTenant(), id);

        if (!todos.isPresent()) {
            return notFound("Time não encontrado!");
        }
        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {

        try {

            if (!getProfile().isPresent()) return forbidden();

            timeRepository.excluir(getTenant(), id);
            return ok("Time excluído!");
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }

    private Tenant getTenant(){

        Optional<CommonProfile> commonProfile = getProfile();
        CommonProfile profile = commonProfile.get();
        return Tenant.of((Long) profile.getAttribute("TENANT_ID"));
    }

    private Optional<CommonProfile> getProfile() {
        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        return profileManager.get(true);
    }

}
