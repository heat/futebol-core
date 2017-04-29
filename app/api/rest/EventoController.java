package api.rest;

import actions.TenantAction;
import api.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.ApplicationController;
import dominio.processadores.apostas.EventoApostaInserirProcessador;
import dominio.processadores.eventos.EventoAtualizarProcessador;
import dominio.processadores.eventos.EventoInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.Apostavel;
import models.apostas.EventoAposta;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.eventos.Time;
import models.vo.Chave;
import org.hibernate.exception.ConstraintViolationException;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repositories.*;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@With(TenantAction.class)
public class EventoController extends ApplicationController{

    EventoRepository eventoRepository;
    EventoInserirProcessador inserirProcessador;
    EventoApostaInserirProcessador eventoApostaInserirProcessador;
    EventoApostaRepository eventoApostaRepository;
    EventoAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;
    CampeonatoRepository campeonatoRepository;
    TimeRepository timeRepository;
    @Inject
    public EventoController(EventoRepository eventoRepository, PlaySessionStore playSessionStore,
                            EventoInserirProcessador inserirProcessador, EventoAtualizarProcessador atualizarProcessador,
                            ValidadorRepository validadorRepository, CampeonatoRepository campeonatoRepository,
                            TimeRepository timeRepository, EventoApostaRepository eventoApostaRepository,
                            EventoApostaInserirProcessador eventoApostaInserirProcessador) {
        super(playSessionStore);

        this.eventoRepository = eventoRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.timeRepository = timeRepository;
        this.eventoApostaRepository = eventoApostaRepository;
        this.eventoApostaInserirProcessador = eventoApostaInserirProcessador;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() throws IOException {

        Optional<Evento> eventoOptional;

        try {
            EventoJson json = Json.fromJson(request().body().asJson().get("evento"), EventoJson.class);
            eventoOptional =  Optional.ofNullable(json.to());

            if(!eventoOptional.isPresent())
                return badRequest("Parâmetro ausente");
        } catch (RuntimeException e){
            return badRequest("Não foi possível converter.");
        }

        Evento evento = eventoOptional.get();
        evento.setSituacao(Evento.Situacao.A);
        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoInserirProcessador.REGRA);
        List<Validador> validadoresEventoAposta = validadorRepository.todos(getTenant(), EventoApostaInserirProcessador.REGRA);

        try {
            evento = inserirProcessador.executar(getTenant(), evento, validadores)
                    .thenApply( eve -> {
                        EventoAposta eventoAposta = new EventoAposta(eve);
                        eventoApostaInserirProcessador.executar(getTenant(), eventoAposta, validadoresEventoAposta);
                        return eve;
                    })
                .get();

        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        } catch (InterruptedException e) {
           return badRequest(e.getMessage());
        } catch (ExecutionException e) {
            return badRequest(e.getMessage());
        } catch (PersistenceException e) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, e.getMessage());
        }

        EventoJson eventoJson = EventoJson.of(evento);

        CampeonatoJson campeonatoJson = CampeonatoJson.of(evento.getCampeonato());
        ObjectJson.JsonBuilder<EventoJson> builder = ObjectJson.build(EventoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);

        JsonNode retorno = builder.comEntidade(eventoJson)
                .comRelacionamento(CampeonatoJson.TIPO, campeonatoJson)
                .comRelacionamento(TimeJson.TIPO, TimeJson.of(evento.getCasa()))
                .comRelacionamento(TimeJson.TIPO, TimeJson.of(evento.getFora()))
                .build();

        return created(retorno);
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) throws IOException {

        EventoJson json = Json.fromJson(request().body().asJson(), EventoJson.class);
        Optional<Evento> eventoOptional =  Optional.ofNullable(json.to());

        if(!eventoOptional.isPresent())
            return badRequest("Parâmetro ausente");

        Evento evento = eventoOptional.get();

        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoInserirProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, evento, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        EventoJson eventoJson = EventoJson.of(eventoRepository.buscar(getTenant(), id).get());
        //TODO tem que fazer a parte de exibir o evento gerado
        return ok(Json.newObject());
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        List<Evento> eventos = eventoRepository.todos(getTenant());

        List<Campeonato> campeonatos = eventos.stream()
                .map(evento -> evento.getCampeonato())
                .distinct()
                .collect(Collectors.toList());

        ObjectJson.JsonBuilder<EventoJson> builder = ObjectJson.build(EventoJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        // adiciona os eventos

        ObjectJson.RelationalJsonBuilder<TimeJson> timebuilder = builder.comRelacionamento(TimeJson.TIPO);
        ObjectJson.RelationalJsonBuilder<CampeonatoJson> campeonatobuilder = builder.comRelacionamento(CampeonatoJson.TIPO);

        eventos.stream().map(EventoJson::of).forEach(builder::comEntidade);
        // adiciona os relacionamentos
        eventos.stream().forEach( evento -> {
            TimeJson casa = TimeJson.of(evento.getCasa());
            TimeJson fora = TimeJson.of(evento.getFora());
            CampeonatoJson campeonato = CampeonatoJson.of(evento.getCampeonato());

            timebuilder.comEntidade(casa).comEntidade(fora);
            campeonatobuilder.comEntidade(campeonato);
        });

        return ok(builder.build());
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        Optional<Evento> eventoOptional = eventoRepository.buscar(getTenant(), id);
        if (!eventoOptional.isPresent()) {
            return notFound("Evento não encontrado!");
        }
        Evento evento = eventoOptional.get();

        ObjectJson.JsonBuilder<EventoJson> builder = ObjectJson.build(EventoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);
        // adiciona os eventos

        builder.comEntidade(EventoJson.of(evento));
        ObjectJson.RelationalJsonBuilder<TimeJson> timebuilder = builder.comRelacionamento(TimeJson.TIPO);
        ObjectJson.RelationalJsonBuilder<CampeonatoJson> campeonatobuilder = builder.comRelacionamento(CampeonatoJson.TIPO);

            TimeJson casa = TimeJson.of(evento.getCasa());
            TimeJson fora = TimeJson.of(evento.getFora());
            CampeonatoJson campeonato = CampeonatoJson.of(evento.getCampeonato());

            timebuilder.comEntidade(casa).comEntidade(fora);
            campeonatobuilder.comEntidade(campeonato);

        return ok(builder.build());
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {

        try {
        eventoRepository.excluir(getTenant(), id);
            return noContent();
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }

    private Calendar deserializeCalendar(String dateAsString)
            throws IOException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = formatter.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
