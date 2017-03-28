package api.rest;

import actions.TenantAction;
import api.json.ImportacaoJson;
import api.json.PinJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repositories.*;

import javax.annotation.processing.Completion;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@With(TenantAction.class)
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
    HttpExecutionContext ec;

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

    public CompletionStage<Result> testar() {

        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello world";
        }, ec.current())
        .thenCompose( b -> {
            return jpaApi.withTransaction( em -> {
               return CompletableFuture.completedFuture(em);
            });
        }).thenApply( em -> {
            //TODO fazer alguma coisa o o em
            return ok("teste");
        });
    }


    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> inserir() {

        JsonNode jsonNode = request().body().asJson();
        String chave = jsonNode.findPath("chave").textValue();
        BigDecimal variacao = jsonNode.findPath("variacao").decimalValue();

        //TODO transformar isso em um repositorio para poder usar o cache
        WSRequest request = ws.url("http://oddfeed.app.sysbet.in/api/odds")
                .setHeader("X-SysBetKey", "DEMO")
                .setContentType("application/json");


        final Tenant tenant = getTenant();

        final Executor ex = ec.current();

        final List<Odd> oddsConfiguracaos = oddRepository.todosConfiguracao(tenant).stream()
                .filter( c -> c.isVisivel())
                .map( c -> c.getOdd())
                .collect(Collectors.toList());

        CompletionStage<JsonNode> promise = request.get().thenApplyAsync(WSResponse::asJson, ex);

        CompletionStage<Result> result = promise
                //pega a lista de importacoes
                .thenApply( j -> j.get("data"))
                .thenApply(this::asStream)
                .thenApplyAsync( v -> fromJson(v, oddsConfiguracaos, i -> v.equals(chave)), ex)
                .thenApply(importacoes -> importacoes.findAny())
                .thenApply( j -> {
                    if(!j.isPresent())
                        throw new RejectedExecutionException("Sem jogo para importar");
                    return j.get();
                })
                .thenComposeAsync(this::iserirEvento, ex)
                //insere evento
                .thenComposeAsync( ev -> jpaApi.withTransaction( em -> eventoApostaInserirProcessador.
                        executar(tenant, ev, validadorRepository.todos(tenant, eventoApostaInserirProcessador.REGRA))), ex)
                .thenApplyAsync( ev -> {
                    Importacao importacao = new Importacao(tenant.get(), chave, variacao, ev.getId());
                    return importacaoInserirProcessador
                            //inserir ou atualizar
                            .executar(tenant, importacao, validadorRepository.todos(tenant, importacaoInserirProcessador.REGRA));
                }, ex)
                // response
                .handleAsync( (r, e) -> {
                    Optional _r = Optional.ofNullable(r);
                    Optional _e = Optional.ofNullable(r);
                    if(_e.isPresent())
                        return internalServerError(e.getMessage());
                    return ok(Json.toJson(r));
        }, ex);
        return result;
    }

    private Stream<ImportacaoJson> fromJson(Stream<JsonNode> stream, List<Odd> odds, Predicate<ImportacaoJson> f) {
        Tenant tenant = getTenant();
        ConversorOdd conversorOdd = new ConversorOdd(odds);
        return  stream.map( i ->  {
            ImportacaoJson j = Json.fromJson(i, ImportacaoJson.class);
            if(f.test(j))

            i.fields().forEachRemaining( (n) -> {
                String oddName = n.getKey();
                Optional<Odd> odd = conversorOdd.from(oddName);
                if(odd.isPresent()) {
                    Odd oddPresente = odd.get();
                    BigDecimal valorTaxa = BigDecimal.valueOf(n.getValue().asDouble());
                    Taxa t = new Taxa(tenant.get(), oddPresente, valorTaxa, conversorOdd.linha(oddName) );
                    j.addTaxa(t);
                }
            });
            return j;
        }).filter(f);
    }

    private CompletionStage<EventoAposta> iserirEvento(ImportacaoJson j) {
            Tenant tenant = getTenant();
            EventoAposta aposta = jpaApi.withTransaction( em -> {
                Evento.EventoBuilder builder = Evento.builder(tenant)
                        .em(j.dataJogo);
                try {
                    EventoAposta _a = CompletableFuture.allOf(
                            //insere casa
                            timeInserirProcessador.
                                    executar(tenant, Time.of(j.time1), validadorRepository.todos(tenant, timeInserirProcessador.REGRA))
                                    .thenApply( t -> builder.comTimeCasa(t)),
                            //insere fora
                            timeInserirProcessador
                                    .executar(tenant, Time.of(j.time2), validadorRepository.todos(tenant, timeInserirProcessador.REGRA))
                                    .thenApply( f -> builder.comTimeFora(f)),
                            campeonatoInserirProcessador
                                    .executar(tenant, Campeonato.of(j.campeonato), validadorRepository.todos(tenant, campeonatoInserirProcessador.REGRA))
                                    .thenAccept(c -> builder.comCampeonato(c)))
                            .thenApply( v -> builder.build())
                            //insere um evento
                            .thenCompose( e -> eventoInserirProcessador.executar(tenant, e, validadorRepository.todos(tenant, eventoInserirProcessador.REGRA)))
                            //evento salvo
                            .thenApply( e -> EventoAposta.of(e))
                            .get();
                    return _a;
                } catch (InterruptedException e) {
                    throw new RejectedExecutionException(e);
                } catch (ExecutionException e) {
                    throw new RejectedExecutionException(e);
                }
            });
            aposta.setTaxas(j.getTaxas());
        return CompletableFuture.completedFuture(aposta);
    }

    private Stream<JsonNode> asStream(JsonNode jsonNode) {
        return StreamSupport.stream(jsonNode.spliterator(), false);
    }
}
