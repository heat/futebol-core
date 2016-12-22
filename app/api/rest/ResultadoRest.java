package api.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dominio.processadores.eventos.ResultadoAtualizarProcessador;
import dominio.processadores.eventos.ResultadoInserirProcessador;
import models.eventos.Evento;
import models.eventos.Resultado;
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
import repositories.ResultadoRepository;
import repositories.ValidatorRepository;
import validators.Validator;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ResultadoRest extends Controller{

    ResultadoRepository resultadoRepository;

    PlaySessionStore playSessionStore;

    ResultadoInserirProcessador inserirProcessador;

    ResultadoAtualizarProcessador atualizarProcessador;
    ValidatorRepository validatorRepository;

    EventoRepository eventoRepository;



    @Inject
    public ResultadoRest(ResultadoRepository resultadoRepository, PlaySessionStore playSessionStore,
                         ResultadoInserirProcessador inserirProcessador, ResultadoAtualizarProcessador atualizarProcessador,
                         ValidatorRepository validatorRepository, EventoRepository eventoRepository) {
        this.resultadoRepository = resultadoRepository;
        this.playSessionStore = playSessionStore;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validatorRepository = validatorRepository;
        this.eventoRepository = eventoRepository;

    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir(Long idEvento) throws IOException {

        if (!getProfile().isPresent()) {
            return forbidden();
        }

        JsonNode json = Controller.request().body().asJson();
        ObjectMapper mapper = new ObjectMapper();

        Resultado[] resultado = mapper.readValue(json.toString(), Resultado[].class);

        //Melhorar
        if(resultado.length == 0)
        {
            return notFound("Lista de resultados não pode ser vazia!");
        }

        List<Validator> validators = validatorRepository.todos(getTenant().get(), ResultadoInserirProcessador.REGRA);
        inserirProcessador = new ResultadoInserirProcessador(resultadoRepository);

        Optional<Evento> evento = eventoRepository.buscar(getTenant().get(), idEvento);
        if(!evento.isPresent()){
            return notFound("Evento não encontrado");
        }
        try {
            inserirProcessador.executar(getTenant().get(), resultado, evento.get(), validators);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        return ok("Resultado cadastrado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {

        if (!getProfile().isPresent()) {
            return forbidden();
        }

        Resultado resultado = Json.fromJson(Controller.request().body().asJson(), Resultado.class);
        List<Validator> validators = validatorRepository.todos(getTenant().get(), ResultadoInserirProcessador.REGRA);
        atualizarProcessador = new ResultadoAtualizarProcessador(resultadoRepository);

        try {
            atualizarProcessador.executar(getTenant().get(), resultado, validators, id);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        return ok("Resultado atualizado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        if (!getProfile().isPresent()) {
            return forbidden();
        };

        List todos = resultadoRepository.todos(getTenant().get());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        if (!getProfile().isPresent()) {
            return forbidden();
        }

        Optional<Resultado> todos = (Optional<Resultado>) resultadoRepository.buscar(getTenant().get(), id);

        if (!todos.isPresent()) {
            return notFound("Resultado não encontrado!");
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
            resultadoRepository.excluir(getTenant().get(), id);
            return ok("Resultado excluído!");
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
