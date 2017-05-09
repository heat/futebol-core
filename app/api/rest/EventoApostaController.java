package api.rest;

import actions.TenantAction;
import api.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.apostas.EventoApostaAtualizarProcessador;
import dominio.processadores.apostas.EventoApostaInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.Apostavel;
import models.apostas.EventoAposta;
import models.apostas.Taxa;
import models.eventos.Campeonato;
import models.eventos.Evento;
import models.seguranca.RegistroAplicativo;
import models.vo.Chave;
import models.vo.Tenant;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repositories.EventoApostaRepository;
import repositories.EventoRepository;
import repositories.TenantRepository;
import repositories.ValidadorRepository;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@With(TenantAction.class)
public class EventoApostaController extends ApplicationController {

    EventoApostaRepository eventoApostaRepository;
    EventoApostaInserirProcessador inserirProcessador;
    EventoApostaAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;
    EventoRepository eventoRepository;
    TenantRepository tenantRepository;

    @Inject
    public EventoApostaController(PlaySessionStore playSessionStore, EventoApostaRepository eventoApostaRepository,
                                  EventoApostaInserirProcessador inserirProcessador, EventoApostaAtualizarProcessador atualizarProcessador,
                                  ValidadorRepository validadorRepository, EventoRepository eventoRepository, TenantRepository tenantRepository) {
        super(playSessionStore);
        this.eventoApostaRepository = eventoApostaRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
        this.eventoRepository = eventoRepository;
        this.tenantRepository = tenantRepository;
    }


    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        JsonNode json = request().body().asJson();
        Long eventoId = json.findPath("evento").asLong();
        Optional<Evento> eventoOptional = eventoRepository.buscar(getTenant(), eventoId);
        if(!eventoOptional.isPresent())
            return badRequest("Evento não encontrado");
        EventoAposta eventoAposta = new EventoAposta();
        eventoAposta.setEvento(eventoOptional.get());
        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoApostaInserirProcessador.REGRA);
        try {
            inserirProcessador.executar(getTenant(), eventoAposta, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        ApostaJson apostaJson = ApostaJson.of(eventoAposta);
        JsonNode jsonNode= ObjectJson.build(ApostaJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT)
                .comEntidade(apostaJson)
                .build();
        return created(jsonNode);
    }

    @Deprecated
    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {

        JsonNode json = request().body().asJson();
        String situacao = json.findPath("situacao").asText();
        Optional<EventoAposta> eventoApostaOptional = eventoApostaRepository.buscar(getTenant(), id);
        if(!eventoApostaOptional.isPresent())
            return badRequest("Aposta não encontrada");
        EventoAposta eventoAposta = eventoApostaOptional.get();
        eventoAposta.setSituacao(Apostavel.Situacao.valueOf(situacao));
        List<Validador> validadores = validadorRepository.todos(getTenant(), EventoApostaAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, eventoAposta, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        ApostaJson apostaJson = ApostaJson.of(eventoAposta);
        JsonNode jsonNode= ObjectJson.build(ApostaJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT)
                .comEntidade(apostaJson)
                .build();

        return ok(jsonNode);
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        Optional<String> appKeyOptional = Optional.ofNullable(request().getHeader("X-AppCode"));

        if (!appKeyOptional.isPresent()){
            return badRequest("Key not found.");
        }

        Optional<RegistroAplicativo> registroAplicativoOptional = tenantRepository.buscar(appKeyOptional.get());

        if (!registroAplicativoOptional.isPresent()){
            return notFound("Aplicativo não registrado.");
        }

        Tenant tenant = Tenant.of(registroAplicativoOptional.get().getTenant());

        List<EventoAposta> todos = eventoApostaRepository.todos(tenant);
        ObjectJson.JsonBuilder<ApostaJson> builder = ObjectJson.build(ApostaJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);

        todos.forEach(eventoAposta -> {
            builder.comEntidade(ApostaJson.of(eventoAposta));
            builder.comRelacionamento(EventoJson.TIPO, EventoJson.of(eventoAposta.getEvento()));
        });

        return ok(builder.build());
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        Optional<String> appKeyOptional = Optional.ofNullable(request().getHeader("X-AppCode"));

        if (!appKeyOptional.isPresent()){
            return badRequest("Key not found.");
        }

        Optional<RegistroAplicativo> registroAplicativoOptional = tenantRepository.buscar(appKeyOptional.get());

        if (!registroAplicativoOptional.isPresent()){
            return notFound("Aplicativo não registrado.");
        }

        Tenant tenant = Tenant.of(registroAplicativoOptional.get().getTenant());

        Optional<EventoAposta> eventoAposta = eventoApostaRepository.buscar(tenant, id);

        if (!eventoAposta.isPresent()) {
            return notFound("Aposta não encontrada!");
        }

        EventoAposta aposta = eventoAposta.get();

        ObjectJson.JsonBuilder<ApostaJson> builder = ObjectJson.build(ApostaJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);

        ApostaJson apostaJson = ApostaJson.of(aposta);
        builder.comEntidade(apostaJson)
                .comLink(TaxaJson.TIPO, TaxaJson.TIPO + "?aposta=" + apostaJson.id)
                .comRelacionamento(EventoJson.TIPO, EventoJson.of(aposta.getEvento()));

        return ok(builder.build());
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result cancelar(Long id) {
        try {
            eventoApostaRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando exclui uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }

}
