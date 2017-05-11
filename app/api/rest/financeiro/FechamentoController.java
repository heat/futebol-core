package api.rest.financeiro;

import actions.TenantAction;
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
import repositories.LancamentoRepository;
import repositories.TenantRepository;
import repositories.ValidadorRepository;
import services.DataService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

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
        //TODO: Ajustar logica para pegar ultimo lancamento menor igual a data passada
        Lancamento lancamento = conta.getLancamentos().stream().filter(l -> l.getDataLancamento().compareTo(dataFechamento) == 0).findFirst().get();

        BigDecimal valorFechamento = lancamento.getSaldo().getFechamento();

        Map<String, String> fechamentos = new HashMap();
        fechamentos.put("id", UUID.randomUUID().toString());
        fechamentos.put("valor", valorFechamento.toString());

        return created(Json.toJson(fechamentos));
    }
}