package api.rest;

import actions.TenantAction;
import api.json.*;
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
import models.eventos.Campeonato;
import models.seguranca.RegistroAplicativo;
import models.vo.Tenant;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import repositories.*;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@With(TenantAction.class)
public class JogoController extends ApplicationController {

    TaxaRepository taxaRepository;
    TaxaInserirProcessador inserirProcessador;
    TaxaAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;
    EventoApostaRepository eventoApostaRepository;
    OddRepository oddRepository;
    TenantRepository tenantRepository;

    @Inject

    public JogoController(PlaySessionStore playSessionStore, TaxaRepository taxaRepository,
                          TaxaInserirProcessador inserirProcessador, TaxaAtualizarProcessador atualizarProcessador,
                          ValidadorRepository validadorRepository, EventoApostaRepository eventoApostaRepository,
                          OddRepository oddRepository, TenantRepository tenantRepository) {
        super(playSessionStore);
        this.taxaRepository = taxaRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
        this.eventoApostaRepository = eventoApostaRepository;
        this.oddRepository = oddRepository;
        this.tenantRepository = tenantRepository;

    }

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
        ObjectJson.JsonBuilder<JogoJson> builder = ObjectJson.build(JogoJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);

        todos.forEach(eventoAposta -> {
            builder.comEntidade(JogoJson.of(eventoAposta));
            eventoAposta.getTaxasAtivas().stream()
                    .filter(p -> p.isFavorita()).collect(Collectors.toList())
                    .forEach(taxaAposta -> builder.comRelacionamento(TaxaJogoJson.TIPO, TaxaJogoJson.of(taxaAposta, eventoAposta.getId())));
        });

        List<Campeonato> campeonatos = todos.stream()
                .map(eventoAposta -> eventoAposta.getEvento().getCampeonato())
                .distinct()
                .collect(Collectors.toList());

        // adiciona os relacionamentos
        campeonatos.forEach(campeonato -> builder.comRelacionamento(CampeonatoJson.TIPO, CampeonatoJson.of(campeonato)));

        return ok(builder.build());
    }

    @Transactional
    public Result buscar(Long evento) {

        Optional<String> appKeyOptional = Optional.ofNullable(request().getHeader("X-AppCode"));

        if (!appKeyOptional.isPresent()){
            return badRequest("Key not found.");
        }

        Optional<RegistroAplicativo> registroAplicativoOptional = tenantRepository.buscar(appKeyOptional.get());

        if (!registroAplicativoOptional.isPresent()){
            return notFound("Aplicativo não registrado.");
        }

        Tenant tenant = Tenant.of(registroAplicativoOptional.get().getTenant());

        Optional<EventoAposta> eventoApostaOptional = eventoApostaRepository.buscar(tenant, evento);

        if(!eventoApostaOptional.isPresent())
            return badRequest("Aposta não encontrada!");

        EventoAposta eventoAposta = eventoApostaOptional.get();
        // usa o builder
        ObjectJson.JsonBuilder<TaxaJogoJson> builder = ObjectJson.build(TaxaJogoJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        //adiciona as entidades
        eventoAposta.getTaxasAtivas().forEach( taxa -> builder.comEntidade(TaxaJogoJson.of(taxa, evento)));
        JsonNode retorno = builder.build();
        return created(retorno);

    }
}
