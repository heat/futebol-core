package api.rest;

import dominio.processadores.eventos.CampeonatoAtualizarProcessador;
import dominio.processadores.eventos.CampeonatoInserirProcessador;
import models.eventos.Campeonato;
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
import repositories.CampeonatoRepository;
import repositories.ValidadorRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class CampeonatoRest extends Controller {


    CampeonatoRepository campeonatoRepository;

    PlaySessionStore playSessionStore;

    CampeonatoInserirProcessador campeonatoInserirProcessador;

    ValidadorRepository validatorRepository;

    @Inject
    public CampeonatoRest(CampeonatoRepository campeonatoRepository, PlaySessionStore playSessionStore,
                          CampeonatoInserirProcessador campeonatoInserirProcessador, ValidadorRepository validatorRepository) {
        this.campeonatoRepository = campeonatoRepository;
        this.playSessionStore = playSessionStore;
        this.campeonatoInserirProcessador = campeonatoInserirProcessador;
        this.validatorRepository = validatorRepository;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        if (!getProfile().isPresent()) {
            return forbidden();
        }

        Campeonato campeonato = Json.fromJson(Controller.request().body().asJson(), Campeonato.class);

        List<Validador> validators = validatorRepository.todos(getTenant().get(), CampeonatoInserirProcessador.REGRA);
        CampeonatoInserirProcessador processadorInserir = new CampeonatoInserirProcessador(campeonatoRepository);

        try {
            processadorInserir.executar(getTenant().get(), campeonato, validators);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        return ok("Campeonato " + campeonato.getNome() + " cadastrado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {

        if (!getProfile().isPresent()) {
            return forbidden();
        }

        Campeonato campeonato = Json.fromJson(Controller.request().body().asJson(), Campeonato.class);

        List<Validador> validators = validatorRepository.todos(getTenant().get(), CampeonatoAtualizarProcessador.REGRA);
        CampeonatoAtualizarProcessador processadorAtualizar = new CampeonatoAtualizarProcessador(campeonatoRepository, id);

        try {
            processadorAtualizar.executar(getTenant().get(), campeonato, validators);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        return ok("Campeonato " + campeonato.getNome() + " atualizado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        if (!getProfile().isPresent()) {
            return forbidden();
        }
        List todos = campeonatoRepository.todos(getTenant().get());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        Optional<CommonProfile> commonProfile = getProfile();

        if (!getProfile().isPresent()) {
            return forbidden();
        }
        Optional<Campeonato> todos = (Optional<Campeonato>) campeonatoRepository.buscar(getTenant().get(), id);

        if (!todos.isPresent()) {
            return notFound("Campeonato não encontrado!");
        }
        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {

        try {

            if (!getProfile().isPresent()) {
                return forbidden();
            }

            campeonatoRepository.excluir(getTenant().get(), id);
            return ok("Campeonado excluído!");
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }

    private Optional<Tenant> getTenant() {

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