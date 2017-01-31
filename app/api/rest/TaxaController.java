package api.rest;

import api.json.TaxaJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.apostas.TaxaAtualizarProcessador;
import dominio.processadores.apostas.TaxaInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
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
import repositories.TaxaRepository;
import repositories.ValidadorRepository;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaxaController extends ApplicationController {

    TaxaRepository taxaRepository;
    TaxaInserirProcessador inserirProcessador;
    TaxaAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;

    @Inject

    public TaxaController(PlaySessionStore playSessionStore, TaxaRepository taxaRepository,
                          TaxaInserirProcessador inserirProcessador, TaxaAtualizarProcessador atualizarProcessador,
                          ValidadorRepository validadorRepository) {
        super(playSessionStore);
        this.taxaRepository = taxaRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
    }


    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir(Long aposta) {

        JsonNode body = Controller.request()
                .body()
                .asJson();

        List<Taxa> taxas = new ArrayList<>();

        body.get("taxas").forEach( taxaJson -> {
            TaxaJson t = Json.fromJson(taxaJson, TaxaJson.class);
            t.aposta = aposta;
            taxas.add(t.to());
            System.out.println(t);
        });

        List<Validador> validadores = validadorRepository.todos(getTenant(), TaxaInserirProcessador.REGRA);

        try {
            taxas.forEach( taxa -> {
                inserirProcessador.executar(getTenant(), taxa, validadores);
            });

        } catch (ValidadorExcpetion ex) {
            return internalServerError("definir melhor o erro");
        }

        return ok();
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
    public Result buscar(Long id) {
        Optional<Taxa> taxa = taxaRepository.buscar(getTenant(), id);

        if (!taxa.isPresent()) {
            return notFound("Taxa não encontrada!");
        }
        return ok(Json.toJson(taxa));
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
