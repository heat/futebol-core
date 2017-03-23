package api.rest;

import api.json.ImportacaoJson;
import api.json.PinJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.Importacao.ImportacaoInserirProcessador;
import dominio.processadores.apostas.EventoApostaInserirProcessador;
import dominio.processadores.bilhetes.PinInserirProcessador;
import dominio.processadores.eventos.CampeonatoInserirProcessador;
import dominio.processadores.eventos.EventoInserirProcessador;
import dominio.processadores.eventos.TimeInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.Importacao.ConversorOdd;
import models.Importacao.Importacao;
import models.apostas.EventoAposta;
import models.apostas.Odd;
import models.apostas.OddConfiguracao;
import models.apostas.Taxa;
import models.bilhetes.PalpitePin;
import models.bilhetes.Pin;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.eventos.Time;
import models.vo.Chave;
import models.vo.Tenant;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import repositories.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ImportacaoController extends ApplicationController {

    ValidadorRepository validadorRepository;
    TenantRepository tenantRepository;
    EventoApostaRepository eventoApostaRepository;
    PinInserirProcessador inserirProcessador;
    UsuarioRepository usuarioRepository;
    PinRepository pinRepository;
    TimeInserirProcessador timeInserirProcessador;
    CampeonatoInserirProcessador campeonatoInserirProcessador;
    ImportacaoRepository importacaoRepository;
    EventoInserirProcessador eventoInserirProcessador;
    WSClient ws;
    JPAApi jpaApi;
    OddRepository oddRepository;
    EventoApostaInserirProcessador eventoApostaInserirProcessador;
    ImportacaoInserirProcessador importacaoInserirProcessador;


    @Inject
    public ImportacaoController(PlaySessionStore playSessionStore, ValidadorRepository validadorRepository,
                                TenantRepository tenantRepository, EventoApostaRepository eventoApostaRepository,
                                PinInserirProcessador inserirProcessador, UsuarioRepository usuarioRepository,
                                PinRepository pinRepository, WSClient ws, TimeInserirProcessador timeInserirProcessador,
                                CampeonatoInserirProcessador campeonatoInserirProcessador,
                                JPAApi jpaApi, ImportacaoRepository importacaoRepository,
                                EventoInserirProcessador eventoInserirProcessador, OddRepository oddRepository,
                                EventoApostaInserirProcessador eventoApostaInserirProcessador, ImportacaoInserirProcessador importacaoInserirProcessador) {
        super(playSessionStore);
        this.validadorRepository = validadorRepository;
        this.tenantRepository = tenantRepository;
        this.eventoApostaRepository = eventoApostaRepository;
        this.inserirProcessador = inserirProcessador;
        this.usuarioRepository = usuarioRepository;
        this.pinRepository = pinRepository;
        this.ws = ws;
        this.timeInserirProcessador = timeInserirProcessador;
        this.campeonatoInserirProcessador = campeonatoInserirProcessador;
        this.jpaApi = jpaApi;
        this.importacaoRepository = importacaoRepository;
        this.eventoInserirProcessador = eventoInserirProcessador;
        this.oddRepository = oddRepository;
        this.eventoApostaInserirProcessador = eventoApostaInserirProcessador;
        this.importacaoInserirProcessador = importacaoInserirProcessador;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        JsonNode jsonNode = request().body().asJson();
        String chave = jsonNode.findPath("chave").textValue();
        BigDecimal variacao = jsonNode.findPath("variacao").decimalValue();

        WSRequest request = ws.url("http://oddfeed.app.sysbet.in/api/odds")
                .setHeader("X-SysBetKey", "DEMO")
                .setContentType("application/json");


        CompletionStage<JsonNode> promise = request.get().thenApply(WSResponse::asJson);
        Tenant tenant = getTenant();
        List<Validador> timesValidadores = validadorRepository.todos(getTenant(), TimeInserirProcessador.REGRA);
        List<Validador> campeonatosValidadores = validadorRepository.todos(getTenant(), CampeonatoInserirProcessador.REGRA);
        List<Validador> eventosValidadores = validadorRepository.todos(getTenant(), EventoInserirProcessador.REGRA);
        List<Validador> eventosApostasValidadores = validadorRepository.todos(getTenant(), EventoApostaInserirProcessador.REGRA);
        List<Validador> importacaoValidadores = validadorRepository.todos(getTenant(), ImportacaoInserirProcessador.REGRA);

        promise.thenApply(p -> {
            jpaApi.withTransaction(em -> {
                p.path("data").forEach(d -> {
                    ImportacaoJson json = Json.fromJson(d, ImportacaoJson.class);

                    if (json.codigo.equals(chave)){
                        try {

                            Optional<Importacao> importacaoOptional = importacaoRepository.buscar(tenant, chave);

                            if (!importacaoOptional.isPresent()){
                                Time casa = timeInserirProcessador.executar(tenant, new Time(tenant.get(), json.time1), timesValidadores).get();
                                Time fora = timeInserirProcessador.executar(tenant, new Time(tenant.get(), json.time2), timesValidadores).get();
                                Campeonato campeonato = campeonatoInserirProcessador.executar(tenant, new Campeonato(tenant.get(), json.campeonato), campeonatosValidadores).get();

                                Evento evento = new Evento();
                                evento.setTenant(tenant.get());
                                evento.setCasa(casa);
                                evento.setFora(fora);
                                evento.setCampeonato(campeonato);
                                evento.setDataEvento(json.dataJogo);

                                evento = eventoInserirProcessador.executar(tenant, evento, eventosValidadores).get();

                                EventoAposta eventoAposta = new EventoAposta();
                                eventoAposta.setEvento(evento);
                                eventoAposta.setTenant(tenant.get());


                                List<OddConfiguracao> oddsConfiguracaos = oddRepository.todosConfiguracao(tenant);
                                List<Odd> odds = oddsConfiguracaos.stream().map(o -> o.getOdd()).collect(Collectors.toList());

                                ConversorOdd conversorOdd = new ConversorOdd(odds);
                                //for each para as taxas
                                d.fields().forEachRemaining( (n) -> {
                                    String oddName = n.getKey();
                                    Optional<Odd> odd = conversorOdd.from(oddName);
                                    if(odd.isPresent()) {
                                        Odd oddPresente = odd.get();
                                        BigDecimal valorTaxa = BigDecimal.valueOf(n.getValue().asDouble());
                                        Taxa t = new Taxa(tenant.get(), oddPresente, valorTaxa, conversorOdd.linha(oddName) );
                                        eventoAposta.addTaxa(t);
                                    }
                                });

                                eventoApostaInserirProcessador.executar(tenant, eventoAposta, eventosApostasValidadores);

                                Importacao importacao = new Importacao(tenant.get(), chave, variacao, evento.getId());
                                importacaoInserirProcessador.executar(tenant, importacao, importacaoValidadores);
                            } else {

                                Importacao importacao = importacaoOptional.get();
                                ConversorOdd conversorOdd = new ConversorOdd();
                                Optional<EventoAposta> eventoApostaOptional = eventoApostaRepository.buscarPorEvento(tenant, importacao.getEvento());

                                if (eventoApostaOptional.isPresent()){

                                    EventoAposta eventoAposta = eventoApostaOptional.get();

                                    eventoAposta.getTaxas().forEach(t -> {
                                        d.fields().forEachRemaining( (n) -> {
                                            if (t.getOdd().getCodigo().equals(conversorOdd.getKeyFromValue(n.getKey()))){
                                                BigDecimal valorTaxa = BigDecimal.valueOf(n.getValue().asDouble());
                                                t.setTaxa(valorTaxa);
                                            }
                                        });
                                    });

                                }
                                importacao.setAlteradoEm(Calendar.getInstance());
                                importacao.setSituacao(Importacao.Situacao.A);
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                });
                return null;
            });

            return p;
        });

        return null;

    }

}
