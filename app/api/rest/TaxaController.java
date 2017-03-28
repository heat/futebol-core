package api.rest;

import actions.TenantAction;
import api.json.ObjectJson;
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
import java.util.List;
import java.util.Optional;

@With(TenantAction.class)
public class TaxaController extends ApplicationController {

    TaxaRepository taxaRepository;
    TaxaInserirProcessador inserirProcessador;
    TaxaAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;
    EventoApostaRepository eventoApostaRepository;
    OddRepository oddRepository;
    TenantRepository tenantRepository;

    @Inject

    public TaxaController(PlaySessionStore playSessionStore, TaxaRepository taxaRepository,
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


    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir(Long aposta) {

        Optional<EventoAposta> eventoApostaOptional = eventoApostaRepository.buscar(getTenant(), aposta);

        if(!eventoApostaOptional.isPresent())
            return badRequest("Aposta não encontrada!");

        EventoAposta eventoAposta = eventoApostaOptional.get();

        JsonNode body = Controller.request()
                .body()
                .asJson();

        body.get("taxas").forEach( taxaJson -> {
            TaxaJson t = Json.fromJson(taxaJson, TaxaJson.class);
            t.evento = aposta;
            Taxa taxa = t.to();
            Optional<Odd> oddOptional = oddRepository.buscar(getTenant(),t.odd);
            if(oddOptional.isPresent()) {
                taxa.setOdd(oddOptional.get());
            }
            eventoAposta.addTaxa(taxa);
            System.out.println(t);
        });

        List<Validador> validadores = validadorRepository.todos(getTenant(), TaxaInserirProcessador.REGRA);

        try {
             inserirProcessador.executar(getTenant(), eventoAposta, validadores);
        } catch (ValidadorExcpetion ex) {
            return internalServerError("definir melhor o erro");
        }

        // usa o builder
        ObjectJson.JsonBuilder<TaxaJson> builder = ObjectJson.build(TaxaJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        //adiciona as entidades
        eventoAposta.getTaxas().forEach( taxa -> builder.comEntidade(TaxaJson.of(taxa,aposta)));
        JsonNode retorno = builder.build();
        return created(retorno);
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long aposta) {

        Optional<String> appKeyOptional = Optional.ofNullable(request().getHeader("X-AppCode"));

        if (!appKeyOptional.isPresent()){
            return badRequest("Key not found.");
        }

        Optional<RegistroAplicativo> registroAplicativoOptional = tenantRepository.buscar(appKeyOptional.get());

        if (!registroAplicativoOptional.isPresent()){
            return notFound("Aplicativo não registrado.");
        }

        Tenant tenant = Tenant.of(registroAplicativoOptional.get().getTenant());


        Optional<EventoAposta> eventoApostaOptional = eventoApostaRepository.buscar(tenant, aposta);

        if(!eventoApostaOptional.isPresent())
            return badRequest("Aposta não encontrada!");

        EventoAposta eventoAposta = eventoApostaOptional.get();
        // usa o builder
        ObjectJson.JsonBuilder<TaxaJson> builder = ObjectJson.build(TaxaJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        //adiciona as entidades
        eventoAposta.getTaxas().forEach( taxa -> builder.comEntidade(TaxaJson.of(taxa, aposta)));
        JsonNode retorno = builder.build();
        return created(retorno);

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

}
