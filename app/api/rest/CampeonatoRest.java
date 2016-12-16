package api.rest;

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
import repositories.ValidatorRepository;
import validators.Validator;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class CampeonatoRest extends Controller {


    CampeonatoRepository campeonatoRepository;

    PlaySessionStore playSessionStore;

    CampeonatoInserirProcessador campeonatoInserirProcessador;

    ValidatorRepository validatorRepository;

    @Inject
    public CampeonatoRest(CampeonatoRepository campeonatoRepository, PlaySessionStore playSessionStore,
                          CampeonatoInserirProcessador campeonatoInserirProcessador, ValidatorRepository validatorRepository) {
        this.campeonatoRepository = campeonatoRepository;
        this.playSessionStore = playSessionStore;
        this.campeonatoInserirProcessador = campeonatoInserirProcessador;
        this.validatorRepository = validatorRepository;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        Optional<CommonProfile> commonProfile = getProfile();
        if (!commonProfile.isPresent()) {
            return forbidden();
        }
        CommonProfile profile = commonProfile.get();

        Campeonato campeonato = Json.fromJson(Controller.request().body().asJson(), Campeonato.class);

        List<Validator> validators = validatorRepository.todos(Tenant.of((Long) profile.getAttribute("TENANT_ID")), CampeonatoInserirProcessador.REGRA);
        CampeonatoInserirProcessador processadorInserir = new CampeonatoInserirProcessador(campeonatoRepository);

        try {
            processadorInserir.executar(Tenant.of((Long) profile.getAttribute("TENANT_ID")), campeonato, validators);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        return ok("Campeonato " + campeonato.getNome() + " cadastrado com suscesso! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        Optional<CommonProfile> commonProfile = getProfile();
        if (!commonProfile.isPresent()) {
            return forbidden();
        }
        CommonProfile profile = commonProfile.get();
        List todos = campeonatoRepository.todos(Tenant.of((Long) profile.getAttribute("TENANT_ID")));

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        Optional<CommonProfile> commonProfile = getProfile();

        if (!commonProfile.isPresent()) {
            return forbidden();
        }
        CommonProfile profile = commonProfile.get();
        Optional<Campeonato> todos = (Optional<Campeonato>) campeonatoRepository.buscar(Tenant.of((Long) profile.getAttribute("TENANT_ID")), id);

        if (todos == null) {
            return notFound("Campeonato não encontrado!");
        }
        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {

        try {

            Optional<CommonProfile> commonProfile = getProfile();
            if (!commonProfile.isPresent()) {
                return forbidden();
            }
            CommonProfile profile = commonProfile.get();

            campeonatoRepository.excluir(Tenant.of((Long) profile.getAttribute("TENANT_ID")), id);
            return ok("Campeonado excluído com sucesso!");
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }


    private Optional<CommonProfile> getProfile() {
        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        return profileManager.get(true);
    }

}