package api.rest;

import actions.TenantAction;
import api.json.ObjectJson;
import api.json.PinJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.bilhetes.PinInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.bilhetes.PalpitePin;
import models.bilhetes.Pin;
import models.financeiro.Conta;
import models.seguranca.RegistroAplicativo;
import models.seguranca.Usuario;
import models.vo.Tenant;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repositories.*;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@With(TenantAction.class)
public class PinController extends ApplicationController {

    ValidadorRepository validadorRepository;
    TenantRepository tenantRepository;
    EventoApostaRepository eventoApostaRepository;
    PinInserirProcessador inserirProcessador;
    UsuarioRepository usuarioRepository;
    PinRepository pinRepository;

    @Inject
    public PinController(PlaySessionStore playSessionStore, ValidadorRepository validadorRepository,
            TenantRepository tenantRepository, EventoApostaRepository eventoApostaRepository,
                         PinInserirProcessador inserirProcessador, UsuarioRepository usuarioRepository,
                         PinRepository pinRepository) {
        super(playSessionStore);
        this.validadorRepository = validadorRepository;
        this.tenantRepository = tenantRepository;
        this.eventoApostaRepository = eventoApostaRepository;
        this.inserirProcessador = inserirProcessador;
        this.usuarioRepository = usuarioRepository;
        this.pinRepository = pinRepository;
    }

    @Transactional
    @With(TenantAction.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        JsonNode json = request().body().asJson();
        PinJson pinJson = Json.fromJson(json.get("pin"), PinJson.class);

        Tenant tenant = getTenantAppCode();

        Pin pin = new Pin();
        pin.setCliente(pinJson.cliente);
        pin.setValorAposta(pinJson.valorAposta);

        pinJson.palpites.forEach(p -> pin.addPalpitesPin(new PalpitePin(p)));

//        List<EventoAposta> eventosAposta = eventoApostaRepository.buscar(getTenant().get(), pinJson.palpites);
        //TODO: Calcular a menor data dos jogos e setar em ExpiraEm

        try {
            List<Validador> validadores = validadorRepository.todos(tenant, PinInserirProcessador.REGRA);

            inserirProcessador.executar(tenant, pin, validadores);

        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        } catch (Exception ex){
            return status(Http.Status.UNPROCESSABLE_ENTITY, ex.getMessage());
        }

        ((ObjectNode) json.path("pin")).removeAll()
                .put("id", pin.getId());

        return ok(json);

    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        CommonProfile profile = getProfile().get();
        Optional<Usuario> usuarioOptional = usuarioRepository.buscar(getTenant(), Long.parseLong(profile.getId()));
        if (!usuarioOptional.isPresent())
            return notFound("Usuário não encontrado!");
        final Usuario usuario = usuarioOptional.get();

        Tenant tenant = Tenant.of(usuario.getIdTenant());

        Optional<Pin> pinOptional = pinRepository.buscar(tenant, id);
        if (!pinOptional.isPresent())
            return notFound("PIN não encontrado ou não é válido.");
        final Pin pin = pinOptional.get();

        PinJson pinJson = PinJson.of(pin);

        ObjectJson.JsonBuilder<PinJson> builder = ObjectJson.build(PinJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);
        builder.comEntidade(pinJson);
        JsonNode retorno = builder.build();

        return ok(retorno);

    }
}
