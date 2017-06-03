package api.rest.financeiro;

import actions.TenantAction;
import api.json.FechamentoJson;
import api.json.ObjectJson;
import api.json.SolicitacaoSaldoJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.ApplicationController;
import dominio.processadores.financeiro.AdicionarSaldoProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.financeiro.Conta;
import models.financeiro.Lancamento;
import models.financeiro.SolicitacaoSaldo;
import models.financeiro.TipoLancamento;
import models.financeiro.credito.EmprestimoSaldoCredito;
import models.financeiro.credito.FechamentoCredito;
import models.seguranca.Usuario;
import models.vo.Chave;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repositories.ContaRepository;
import repositories.LancamentoRepository;
import repositories.TenantRepository;
import repositories.ValidadorRepository;
import services.DataService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@With(TenantAction.class)
public class FechamentoController extends ApplicationController {


    ValidadorRepository validadorRepository;
    ContaRepository contaRepository;

    @Inject
    public FechamentoController(PlaySessionStore playSessionStore, ValidadorRepository validadorRepository,
                                ContaRepository contaRepository) {
        super(playSessionStore);
        this.validadorRepository = validadorRepository;
        this.contaRepository = contaRepository;
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result simulacao(String id) {


        String dataFechamentoString = id.substring(0, 10);
        Calendar dataFechamento = DataService.toCalendar(dataFechamentoString);

        Long usuario = Long.parseLong(id.substring(11));

        Optional<Conta> contaOptional = contaRepository.buscar(getTenant(), usuario);

        if (!contaOptional.isPresent()){
            return badRequest("Conta não encontrada.");
        }

        Conta conta = contaOptional.get();

        if (conta.getLancamentos().isEmpty()){
            return badRequest("Não existe lancamentos");
        }

        Lancamento lancamento = conta.getLancamentos().stream().sorted( (v, d) -> d.getDataLancamento().compareTo(v.getDataLancamento())).findFirst().get();

        BigDecimal valorFechamento = lancamento.getSaldo().getFechamento();

        Map<String, String> fechamentos = new HashMap();
        fechamentos.put("id", UUID.randomUUID().toString());
        fechamentos.put("valor", valorFechamento.toString());

        return created(Json.toJson(fechamentos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result fechamentos() {


        CommonProfile profile = getProfile().get();

        Optional<Conta> contaOptional = contaRepository.buscar(getTenant(), Long.parseLong(profile.getId()));

        if (!contaOptional.isPresent()){
            return badRequest("Conta não encontrada.");
        }

        Conta conta = contaOptional.get();
        if (conta.getLancamentos().isEmpty()){
            return badRequest("Não existe lancamentos");
        }
        List<Lancamento> lancamentos = conta.getLancamentos().stream().filter(l -> l instanceof FechamentoCredito).collect(Collectors.toList());

        ObjectJson.JsonBuilder<FechamentoJson> builder = ObjectJson.build(FechamentoJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        lancamentos.forEach(lancamento ->{
            builder.comEntidade(FechamentoJson.of(lancamento));
        });

        JsonNode retorno = builder.build();

        return ok(retorno);

    }
}