package api.rest;

import actions.TenantAction;
import api.json.ImportacaoJson;
import api.json.PinJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.ApplicationController;
import dominio.processadores.Importacao.ImportacaoInserirProcessador;
import dominio.processadores.apostas.EventoApostaInserirProcessador;
import dominio.processadores.apostas.TaxaInserirProcessador;
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
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.play.PlayWebContext;
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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
    TaxaInserirProcessador taxaInserirProcessador;

    WSClient ws;
    JPAApi jpaApi;
    OddRepository oddRepository;
    EventoApostaInserirProcessador eventoApostaInserirProcessador;
    ImportacaoInserirProcessador importacaoInserirProcessador;


    @Inject
    HttpExecutionContext ec;

    @Inject
    public ImportacaoController(PlaySessionStore playSessionStore, ValidadorRepository validadorRepository, TenantRepository tenantRepository, EventoApostaRepository eventoApostaRepository, PinInserirProcessador inserirProcessador, UsuarioRepository usuarioRepository, PinRepository pinRepository, TimeInserirProcessador timeInserirProcessador, CampeonatoInserirProcessador campeonatoInserirProcessador, ImportacaoRepository importacaoRepository, EventoInserirProcessador eventoInserirProcessador, TaxaInserirProcessador taxaInserirProcessador, WSClient ws, JPAApi jpaApi, OddRepository oddRepository, EventoApostaInserirProcessador eventoApostaInserirProcessador, ImportacaoInserirProcessador importacaoInserirProcessador) {
        super(playSessionStore);
        this.validadorRepository = validadorRepository;
        this.tenantRepository = tenantRepository;
        this.eventoApostaRepository = eventoApostaRepository;
        this.inserirProcessador = inserirProcessador;
        this.usuarioRepository = usuarioRepository;
        this.pinRepository = pinRepository;
        this.timeInserirProcessador = timeInserirProcessador;
        this.campeonatoInserirProcessador = campeonatoInserirProcessador;
        this.importacaoRepository = importacaoRepository;
        this.eventoInserirProcessador = eventoInserirProcessador;
        this.taxaInserirProcessador = taxaInserirProcessador;
        this.ws = ws;
        this.jpaApi = jpaApi;
        this.oddRepository = oddRepository;
        this.eventoApostaInserirProcessador = eventoApostaInserirProcessador;
        this.importacaoInserirProcessador = importacaoInserirProcessador;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> importar() {

        final Tenant tenant = getTenant();

        final Executor ex = ec.current();
        String chave = "";

        Optional<Importacao> importacao = importacaoRepository.buscar(tenant, chave);

        return null;
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

        final ConversorJsonImportacao conversor = new ConversorJsonImportacao(oddsConfiguracaos, tenant);

        CompletionStage<JsonNode> promise = request.get().thenApplyAsync(WSResponse::asJson, ex);

        CompletionStage<Result> result = promise
                //pega a lista de importacoes
                .thenApply( j -> j.get("data"))
                .thenApply(this::asStream)
                //filtra
                .thenApply( stream ->
                    stream.filter( node -> {
                        return node.get("codigo").asText().equals(chave);
                    }).findFirst()
                )
                //verifica se encontrou
                .thenApply( node -> {
                    if(!node.isPresent())
                        throw new RejectedExecutionException("Sem jogo para importar");
                    return node.get();
                })
                .thenApplyAsync(conversor::fromJson, ex)
                .<Importacao>thenComposeAsync( reg -> jpaApi.withTransaction( em -> {
                    Optional<Importacao> importacao = importacaoRepository.buscar(tenant, reg.codigo);
                    if(!importacao.isPresent()) {
                        return this.inserirEvento(reg)
                                    .thenCompose(this.atualizaTaxa(tenant, reg))
                                    .thenApply((ap) -> {
                                        return new Importacao(tenant.get(), chave, variacao, ap.getEvento().getId());
                                    });
                    }
                    Importacao importacaoPresente  = importacao.get();
                    Optional<EventoAposta> aposta = eventoApostaRepository.buscarPorEvento(tenant, importacaoPresente.getEvento());

                    if(!aposta.isPresent())
                        throw new RejectedExecutionException("Não encontrou a aposta da importacao");
                    return CompletableFuture.completedFuture(aposta.get())
                            .thenApply(this.atualizaTaxa(tenant, reg))
                            .thenApply((ap) -> { return importacaoPresente; });
                }), ex)
                .thenComposeAsync( importacao ->  {
                    return jpaApi.withTransaction( () ->
                            importacaoInserirProcessador.executar(tenant, importacao, validadorRepository.todos(tenant, importacaoInserirProcessador.REGRA)));
                }, ex)
                // response
                .handleAsync( (r, e) -> {

                     Optional<Importacao> _r = Optional.ofNullable(r);
                    Optional<Throwable> _e = Optional.ofNullable(e);
                    if(_e.isPresent()) {
                        e.printStackTrace();
                        return internalServerError(e.getMessage());
                    }

                    return created(Json.toJson(r));
        }, ex);
        return result;
    }

    private Function<EventoAposta, CompletableFuture<EventoAposta>> atualizaTaxa(Tenant tenant, ImportacaoJson json) {

        return (ap) -> {
            return jpaApi.withTransaction( em-> {
                json.getTaxas().forEach( taxa -> {

                    ap.addTaxa(taxa, (other) -> {
                        return taxa.getOdd().getCodigo().equals(other.getOdd().getCodigo())
                                &&  taxa.getLinha().setScale(2).equals(other.getLinha().setScale(2));
                    });
                });
               return taxaInserirProcessador.executar(tenant, ap, validadorRepository.todos(tenant, taxaInserirProcessador.REGRA));
            });
        };
    }


    class ConversorJsonImportacao {

        final List<Odd> odds;
        final Tenant tenant;

        public ConversorJsonImportacao(List<Odd> odds, Tenant tenant) {
            this.odds = odds;
            this.tenant = tenant;
        }

        public ImportacaoJson fromJson(JsonNode json) {

            ConversorOdd conversorOdd = new ConversorOdd(odds);

            ImportacaoJson j = Json.fromJson(json, ImportacaoJson.class);


            json.fields().forEachRemaining( n -> {
                String oddName = n.getKey();

                Optional<Odd> odd = conversorOdd.from(oddName);
                //insere a odd na taxa
                if(odd.isPresent()) {
                    Odd oddPresente = odd.get();
                    BigDecimal valorTaxa = BigDecimal.valueOf(n.getValue().asDouble());
                    Taxa t = new Taxa(tenant.get(), oddPresente, valorTaxa, conversorOdd.linha(oddName) );
                    j.addTaxa(t);
                }
            });
            return j;
        }
    }


    protected CompletionStage<EventoAposta> inserirEvento(ImportacaoJson j) {
            Tenant tenant = getTenant();
            EventoAposta aposta = jpaApi.withTransaction( (EntityManager em) -> {
                Evento.EventoBuilder builder = Evento.builder(tenant)
                        .em(j.dataJogo)
                        .comModalidade(Evento.Modalidade.FUTEBOL);
                try {
                    EventoAposta _a = CompletableFuture.allOf(
                            //insere casa
                            timeInserirProcessador.
                                    executar(tenant, Time.of(j.time1), validadorRepository.todos(tenant, timeInserirProcessador.REGRA))
                                    .thenAccept( t -> builder.comTimeCasa(t)),
                            //insere fora
                            timeInserirProcessador
                                    .executar(tenant, Time.of(j.time2), validadorRepository.todos(tenant, timeInserirProcessador.REGRA))
                                    .thenAccept( f -> builder.comTimeFora(f)),
                            campeonatoInserirProcessador
                                    .executar(tenant, Campeonato.of(j.campeonato), validadorRepository.todos(tenant, campeonatoInserirProcessador.REGRA))
                                    .thenAccept(c -> builder.comCampeonato(c)))
                            .thenApply( v -> builder.build())
                            //insere um evento
                            .thenCompose( e -> {
                                return eventoInserirProcessador.executar(tenant, e, validadorRepository.todos(tenant, eventoInserirProcessador.REGRA));
                            })
                            //evento salvo
                            .thenCompose( (ev) ->
                                    {
                                        EventoAposta _ap = new EventoAposta(ev);
                                    return eventoApostaInserirProcessador.executar(tenant, _ap,
                                            validadorRepository.todos(tenant, eventoApostaInserirProcessador.REGRA));
                                    })
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
