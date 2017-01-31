package api.rest;

import api.json.ObjectJson;
import api.json.OddJson;
import api.json.TaxaJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.apostas.TaxaAtualizarProcessador;
import dominio.processadores.apostas.TaxaInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.vo.Chave;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.EventoApostaRepository;
import repositories.OddRepository;
import repositories.TaxaRepository;
import repositories.ValidadorRepository;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TaxaController extends ApplicationController {

    TaxaRepository taxaRepository;
    TaxaInserirProcessador inserirProcessador;
    TaxaAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;
    EventoApostaRepository eventoApostaRepository;
    OddRepository oddRepository;

    @Inject

    public TaxaController(PlaySessionStore playSessionStore, TaxaRepository taxaRepository,
                          TaxaInserirProcessador inserirProcessador, TaxaAtualizarProcessador atualizarProcessador,
                          ValidadorRepository validadorRepository, EventoApostaRepository eventoApostaRepository,
                          OddRepository oddRepository  )
    {
        super(playSessionStore);
        this.taxaRepository = taxaRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
        this.eventoApostaRepository = eventoApostaRepository;
        this.oddRepository = oddRepository;

    }


    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() throws IOException {

        List<Taxa> taxas = requestJson();
        List<Odd> odds = taxas.stream()
                .map(taxa -> taxa.getOdd())
                .distinct()
                .collect(Collectors.toList());

        List<Validador> validadores = validadorRepository.todos(getTenant(), TaxaInserirProcessador.REGRA);

        try {
            //inserirProcessador.executar(getTenant(), taxa, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        ObjectJson.JsonBuilder<TaxaJson> builder = ObjectJson.build(TaxaJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        // adiciona as taxas
        taxas.forEach(taxa -> builder.comEntidade(TaxaJson.of(taxa)) );
        // adiciona os relacionamentos
        odds.forEach(odd -> builder.comRelacionamento(OddJson.TIPO, OddJson.of(odd)));

        return created(builder.build());
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        Taxa taxa = Json.fromJson(Controller.request()
                .body()
                .asJson(), Taxa.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), TaxaAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, taxa, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        return ok("Taxa atualizada! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {
        List todos = taxaRepository.todos(getTenant());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long idEvento) {

        Optional<EventoAposta> eventoAposta = eventoApostaRepository.buscar(getTenant(), idEvento);

        if (!eventoAposta.isPresent()) {
            return notFound("Aposta não encontrada!");
        }
        return ok(Json.toJson(eventoAposta.get().getTaxas()));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {
        try {
            taxaRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando exclui uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }

    private List<Taxa> requestJson() throws IOException {

        JsonNode json = request().body().asJson();
        List<JsonNode> jsonNodes = json.findParents("taxas");
        List<Taxa> taxas = new ArrayList<Taxa>();
        for(JsonNode jsonNode: jsonNodes){
            Long idOdd = json.findPath("odd").asLong();
            BigDecimal taxa = json.findPath("taxa").decimalValue();
            BigDecimal linha = json.findPath("linha").decimalValue();
            Optional<Odd> oddOptional = oddRepository.buscar(getTenant(), idOdd);
            if(!oddOptional.isPresent())
                throw new NoResultException("Odd não encontrada! ");
            taxas.add(new Taxa(getTenant().get(),oddOptional.get(),taxa, linha, null, null));
        }
        return taxas;
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
