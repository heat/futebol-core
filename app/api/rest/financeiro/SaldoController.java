package api.rest.financeiro;

import actions.TenantAction;
import api.json.ObjectJson;
import api.json.SolicitacaoSaldoJson;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationController;
import dominio.processadores.financeiro.AdicionarSaldoProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.financeiro.Conta;
import models.financeiro.SolicitacaoSaldo;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@With(TenantAction.class)
public class SaldoController extends ApplicationController {


    ValidadorRepository validadorRepository;
    TenantRepository tenantRepository;
    ContaRepository contaRepository;
    AdicionarSaldoProcessador adicionarSaldoProcessador;

    @Inject
    public SaldoController(PlaySessionStore playSessionStore, ValidadorRepository validadorRepository, TenantRepository tenantRepository,
                           ContaRepository contaRepository, AdicionarSaldoProcessador adicionarSaldoProcessador) {
        super(playSessionStore);
        this.validadorRepository = validadorRepository;
        this.tenantRepository = tenantRepository;
        this.contaRepository = contaRepository;
        this.adicionarSaldoProcessador = adicionarSaldoProcessador;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        JsonNode json = request().body().asJson();
        SolicitacaoSaldoJson solicitacaoSaldoJson = Json.fromJson(json.get("saldo"), SolicitacaoSaldoJson.class);

        Optional<Conta> contaOptional = contaRepository.buscar(getTenant(), solicitacaoSaldoJson.solicitante);

        if (!contaOptional.isPresent()){
            return badRequest("Conta não encontrada.");
        }

        Chave chave = Chave.of(getTenant(), contaOptional.get().getId());

        SolicitacaoSaldo solicitacaoSaldo = solicitacaoSaldoJson.to(chave.getId());

        List<Validador> validadores = validadorRepository.todos(getTenant(), AdicionarSaldoProcessador.REGRA);

        try {
            adicionarSaldoProcessador.executar(chave, solicitacaoSaldo, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        JsonNode retorno = ObjectJson.build(SolicitacaoSaldoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT)
                .comEntidade(SolicitacaoSaldoJson.of(solicitacaoSaldo))
                .build();

        return created(retorno);
    }
}