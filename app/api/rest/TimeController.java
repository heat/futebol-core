package api.rest;

import api.json.Jsonable;
import api.json.ObjectJson;
import api.json.TimeJson;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationController;
import dominio.processadores.eventos.TimeAtualizarProcessador;
import dominio.processadores.eventos.TimeInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Time;
import models.vo.Chave;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Result;
import repositories.EventoRepository;
import repositories.TimeRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TimeController extends ApplicationController{

    TimeRepository timeRepository;
    TimeInserirProcessador inserirProcessador;
    TimeAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;
    EventoRepository eventoRepository;

    @Inject
    public TimeController(TimeRepository timeRepository, PlaySessionStore playSessionStore,
                          TimeInserirProcessador inserirProcessador, TimeAtualizarProcessador atualizarProcessador,
                          ValidadorRepository validadorRepository, EventoRepository eventoRepository) {
        super(playSessionStore);
        this.timeRepository = timeRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
        this.eventoRepository = eventoRepository;

    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() throws IOException {

        Optional<Time> timeOptional = requestJson();
        if(!timeOptional.isPresent())
            badRequest("Parâmetro ausente");
        Time time = timeOptional.get();

        List<Validador> validadores = validadorRepository.todos(getTenant(), TimeInserirProcessador.REGRA);
        try {
            inserirProcessador.executar(getTenant(), time,  validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }
        TimeJson timeJson = TimeJson.of(time);
        return created(ObjectJson.toJson(timeJson));
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {

        Optional<Time> timeOptional = requestJson();
        if(!timeOptional.isPresent())
            badRequest("Parâmetro ausente");
        Time time = timeOptional.get();

        List<Validador> validadores = validadorRepository.todos(getTenant(), TimeAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, time, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return ok(validadorExcpetion.getMessage());
        }

        TimeJson timeJson = TimeJson.of(timeRepository.buscar(getTenant(), id).get());
        return ok(ObjectJson.toJson(timeJson));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        List<Time> times = timeRepository.todos(getTenant());
        List<Jsonable> jsons =  TimeJson.of(times);

        return ok(ObjectJson.toJson(TimeJson.tipoLista, jsons));

    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        Optional<Time> times = timeRepository.buscar(getTenant(), id);
        if (!times.isPresent()) {
            return notFound("Time não encontrado!");
        }
        TimeJson json = TimeJson.of(times.get());
        return ok(ObjectJson.toJson(json));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {

        try {
            timeRepository.excluir(getTenant(), id);
            return noContent();
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }

    private Optional<Time> requestJson(){

        JsonNode json = request().body().asJson();
        String nome = json.findPath("nome").textValue();
        return Optional.ofNullable(new Time(getTenant().get(), nome));
    }
}
