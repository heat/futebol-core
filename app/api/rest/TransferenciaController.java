package api.rest;

import actions.TenantAction;
import api.json.TransferenciaJson;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationController;
import dominio.processadores.financeiro.TransferenciaProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.vo.Chave;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repositories.ContaRepository;
import repositories.TenantRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

import static play.libs.Json.toJson;

@With(TenantAction.class)
public class TransferenciaController extends ApplicationController {


    ValidadorRepository validadorRepository;
    TenantRepository tenantRepository;
    ContaRepository contaRepository;
    TransferenciaProcessador transferenciaProcessador;

    @Inject
    public TransferenciaController(PlaySessionStore playSessionStore, ValidadorRepository validadorRepository, TenantRepository tenantRepository,
                                   ContaRepository contaRepository, TransferenciaProcessador transferenciaProcessador) {
        super(playSessionStore);
        this.validadorRepository = validadorRepository;
        this.tenantRepository = tenantRepository;
        this.contaRepository = contaRepository;
        this.transferenciaProcessador = transferenciaProcessador;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        JsonNode json = request().body().asJson();
        TransferenciaJson transferencia = Json.fromJson(json.get("transferencia"), TransferenciaJson.class);

        Chave chave = Chave.of(getTenant(), transferencia.origem);

        List<Validador> validadores = validadorRepository.todos(getTenant(), TransferenciaProcessador.REGRA);

        try {
            transferenciaProcessador.executar(chave, transferencia.to(), validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        /*JsonNode json = ObjectJson.build(CampeonatoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT)
                .comEntidade(toJson(solicitacaoSaldo))
                .build();*/
        return created(toJson(transferencia));
    }
}