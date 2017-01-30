package api.rest;

import api.json.CampeonatoJson;
import api.json.EventoJson;
import api.json.Jsonable;
import api.json.ObjectJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.ApplicationController;
import dominio.processadores.eventos.EventoAtualizarProcessador;
import dominio.processadores.eventos.EventoInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.Apostavel;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.eventos.Time;
import models.vo.Chave;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import repositories.CampeonatoRepository;
import repositories.EventoRepository;
import repositories.TimeRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventoController extends ApplicationController{

    EventoRepository eventoRepository;
    EventoInserirProcessador inserirProcessador;
    EventoAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;
    CampeonatoRepository campeonatoRepository;
    TimeRepository timeRepository;
    @Inject
    public EventoController(EventoRepository eventoRepository, PlaySessionStore playSessionStore,
                            EventoInserirProcessador inserirProcessador, EventoAtualizarProcessador atualizarProcessador,
                            ValidadorRepository validadorRepository, CampeonatoRepository campeonatoRepository,
                            TimeRepository timeRepository) {
        super(playSessionStore);

        this.eventoRepository = eventoRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
        this.campeonatoRepository = campeonatoRepository;
        this.timeRepository = timeRepository;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() throws IOException {

        Optional<Evento> eventoOptional = requestJson();
        if(!eventoOptional.isPresent())
            badRequest("Parâmetro ausente");
        Evento evento = eventoOptional.get();
        evento.setSituacao(Apostavel.Situacao.ABERTO);
        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant(), evento, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
       return created(Json.newObject());
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) throws IOException {

        Optional<Evento> eventoOptional = requestJson();
        if(!eventoOptional.isPresent())
            badRequest("Parâmetro ausente");
        Evento evento = eventoOptional.get();

        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoInserirProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, evento, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        EventoJson eventoJson = EventoJson.of(eventoRepository.buscar(getTenant(), id).get());
        return ok(Json.newObject());
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        List<Evento> eventos = eventoRepository.todos(getTenant());
        List<Campeonato> campeonatos = new ArrayList<>();
        for(Evento evento: eventos){
            campeonatos.add(evento.getCampeonato());
        }
        List<Jsonable> jsonsCampeonatos = CampeonatoJson.of(campeonatos);
        List<Jsonable> jsonsEventos =  EventoJson.of(eventos);
        return ok(Json.newObject());
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        Optional<Evento> eventoOptional = eventoRepository.buscar(getTenant(), id);
        if (!eventoOptional.isPresent()) {
            return notFound("Evento não encontrado!");
        }
        Evento evento = eventoOptional.get();
        EventoJson jsonEvento = EventoJson.of(evento);
        CampeonatoJson jsonCampeonato = CampeonatoJson.of(evento.getCampeonato());

        ObjectNode root = Json.newObject();
        root.set("evento", Json.toJson(jsonEvento));
        root.set("campeonato", Json.toJson(jsonCampeonato));
        return ok(root);
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

    private Optional<Evento> requestJson() throws IOException {


        JsonNode json = request().body().asJson();
        String casa = json.findPath("casa").textValue();
        String fora = json.findPath("fora").textValue();
        Long idCampeonato = json.findPath("campeonato").asLong();
        Calendar dataEvento = deserializeCalendar( json.findPath("dataEvento").asText());
        Optional<Time> timeCasaOptional = timeRepository.buscar(getTenant(), casa);
        if(!timeCasaOptional.isPresent())
            throw new NoResultException("Time da casa não encontrado");
        Optional<Time> timeForaOptional = timeRepository.buscar(getTenant(), fora);
        if(!timeForaOptional.isPresent())
            throw new NoResultException("Time de fora não encontrado");
        Optional<Campeonato> campeonatoOptional =  campeonatoRepository.buscar(getTenant(), idCampeonato);
        if(!campeonatoOptional.isPresent())
            throw new NoResultException("Campeonato não encontrado");

        return Optional.ofNullable(new Evento(
                getTenant().get(),
                timeCasaOptional.get(),
                timeForaOptional.get(),
                dataEvento,
                campeonatoOptional.get(),
                null,
                null
                )
        );
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
