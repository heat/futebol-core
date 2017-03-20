package api.rest;

import api.json.PinJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.bilhetes.PinInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.PalpitePin;
import models.bilhetes.Pin;
import models.vo.Tenant;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import repositories.*;
import java.util.Calendar;
import java.util.List;

public class ImportacaoController extends ApplicationController {

    ValidadorRepository validadorRepository;
    TenantRepository tenantRepository;
    EventoApostaRepository eventoApostaRepository;
    PinInserirProcessador inserirProcessador;
    UsuarioRepository usuarioRepository;
    PinRepository pinRepository;

    @Inject
    public ImportacaoController(PlaySessionStore playSessionStore, ValidadorRepository validadorRepository,
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

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        /*F.Promise<JsonNode> jsonPromise = ws.url("http://bets79.com/json/jogos/abertos")
                .get().map( res -> res.asJson());

        JsonNode jsonResponse = jsonPromise.get(30000L);

        JsonNode json = request().body().asJson();
        PinJson pinJson = Json.fromJson(json.get("pin"), PinJson.class);

        Tenant tenant = getTenantAppCode();

        Pin pin = new Pin();
        pin.setCliente(pinJson.cliente);
        pin.setCriadoEm(Calendar.getInstance());
        pin.setExpiraEm(Calendar.getInstance());
        pin.getExpiraEm().add(Calendar.MINUTE, 60);
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

        return ok(json);*/
        return null;

    }

}
