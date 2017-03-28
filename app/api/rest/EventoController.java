package api.rest;

import actions.TenantAction;
import api.json.CampeonatoJson;
import api.json.EventoJson;
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
import play.mvc.With;
import repositories.CampeonatoRepository;
import repositories.EventoRepository;
import repositories.TimeRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@With(TenantAction.class)
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
        evento.setSituacao(Evento.Situacao.A);
        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoInserirProcessador.REGRA);

        try {
            evento = inserirProcessador.executar(getTenant(), evento, validadores)
                .get();
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        } catch (InterruptedException e) {
            badRequest(e.getMessage());
        } catch (ExecutionException e) {
            badRequest(e.getMessage());
        }
        EventoJson eventoJson = EventoJson.of(evento);

        CampeonatoJson campeonatoJson = CampeonatoJson.of(evento.getCampeonato());
        ObjectJson.JsonBuilder<EventoJson> builder = ObjectJson.build(EventoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);

        JsonNode retorno = builder.comEntidade(eventoJson)
                .comRelacionamento(CampeonatoJson.TIPO, campeonatoJson)
                .build();
        return created(retorno);
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

        List<Campeonato> campeonatos = eventos.stream()
                .map(evento -> evento.getCampeonato())
                .distinct()
                .collect(Collectors.toList());

        ObjectJson.JsonBuilder<EventoJson> builder = ObjectJson.build(EventoJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        // adiciona os eventos

        eventos.stream().map(EventoJson::of).forEach(builder::comEntidade);
        // adiciona os relacionamentos
        campeonatos.stream().map(CampeonatoJson::of).forEach(builder.<CampeonatoJson>comRelacionamento(CampeonatoJson.TIPO)::comEntidade);
        campeonatos.forEach(campeonato -> builder.comRelacionamento(CampeonatoJson.TIPO, CampeonatoJson.of(campeonato)));

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
        Long casa = json.findPath("casa").asLong();
        Long fora = json.findPath("fora").asLong();
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

        return Optional.of(new Evento(
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
