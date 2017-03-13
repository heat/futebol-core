package api.rest;

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
import models.vo.Tenant;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import repositories.EventoApostaRepository;
import repositories.TenantRepository;
import repositories.ValidadorRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PinController extends ApplicationController {

    ValidadorRepository validadorRepository;
    TenantRepository tenantRepository;
    EventoApostaRepository eventoApostaRepository;
    PinInserirProcessador inserirProcessador;

    @Inject
    public PinController(PlaySessionStore playSessionStore, ValidadorRepository validadorRepository,
            TenantRepository tenantRepository, EventoApostaRepository eventoApostaRepository,
                         PinInserirProcessador inserirProcessador) {
        super(playSessionStore);
        this.validadorRepository = validadorRepository;
        this.tenantRepository = tenantRepository;
        this.eventoApostaRepository = eventoApostaRepository;
        this.inserirProcessador = inserirProcessador;
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        JsonNode json = request().body().asJson();
        PinJson pinJson = Json.fromJson(json.get("pins"), PinJson.class);

        Optional<String> appKeyOptional = Optional.ofNullable(request().getHeader("X-AppCode"));

        if (!appKeyOptional.isPresent()){
            return badRequest("Key not found.");
        }

        Optional<RegistroAplicativo> registroAplicativoOptional = tenantRepository.buscar(appKeyOptional.get());

        if (!registroAplicativoOptional.isPresent()){
            return notFound("Aplicativo não registrado.");
        }

        Tenant tenant = Tenant.of(registroAplicativoOptional.get().getTenant());

        Pin pin = new Pin();
        pin.setCliente(pinJson.cliente);
        pin.setCriadoEm(Calendar.getInstance());
        pin.setExpiraEm(Calendar.getInstance());
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

        ((ObjectNode) json.path("pins")).removeAll()
                .put("id", UUID.randomUUID().toString());

        return ok(json);

    }
}
