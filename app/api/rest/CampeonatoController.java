package api.rest;

import api.json.CampeonatoJson;
import api.json.Jsonable;
import api.json.ObjectJson;
import api.json.TimeJson;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationController;
import dominio.processadores.eventos.CampeonatoAtualizarProcessador;
import dominio.processadores.eventos.CampeonatoInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Campeonato;
import models.seguranca.RegistroAplicativo;
import models.vo.Chave;
import models.vo.Tenant;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import repositories.CampeonatoRepository;
import repositories.TenantRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class CampeonatoController extends ApplicationController {


    CampeonatoRepository campeonatoRepository;
    CampeonatoInserirProcessador inserirProcessador;
    CampeonatoAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;
    TenantRepository tenantRepository;

    @Inject
    public CampeonatoController(CampeonatoRepository campeonatoRepository, PlaySessionStore playSessionStore,
                                CampeonatoInserirProcessador inserirProcessador, CampeonatoAtualizarProcessador atualizarProcessador,
                                ValidadorRepository validadorRepository, TenantRepository tenantRepository) {
        super(playSessionStore);
        this.campeonatoRepository = campeonatoRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
        this.tenantRepository = tenantRepository;
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        Optional<Campeonato> campeonatoOptional = requestJson();
        if(!campeonatoOptional.isPresent())
            badRequest("Parâmetro ausente");
        Campeonato campeonato = campeonatoOptional.get();

        List<Validador> validadores = validadorRepository.todos(getTenant(), CampeonatoInserirProcessador.REGRA);

        try {
            inserirProcessador.executar(getTenant(), campeonato, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        //Atualiza o id do campeonato
        CampeonatoJson campeonatoJson = CampeonatoJson.of(campeonato);
        JsonNode json = ObjectJson.build(CampeonatoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT)
                .comEntidade(campeonatoJson)
                .build();
        return created(json);
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        Optional<Campeonato> campeonatoOptional = requestJson();
        if(!campeonatoOptional.isPresent())
            badRequest("Parâmetro ausente");
        Campeonato campeonato = campeonatoOptional.get();

        List<Validador> validadores = validadorRepository.todos(getTenant(), CampeonatoAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, campeonato, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        CampeonatoJson campeonatoJson = CampeonatoJson.of(campeonatoRepository.buscar(getTenant(), id).get());
        JsonNode json = ObjectJson.build(CampeonatoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT)
                .comEntidade(campeonatoJson)
                .build();
        return ok(json);
    }

    @Transactional
    public Result todos(String nome) {

        Optional<String> appKeyOptional = Optional.ofNullable(request().getHeader("X-AppCode"));

        if (!appKeyOptional.isPresent()){
            return badRequest("Key not found.");
        }

        Optional<RegistroAplicativo> registroAplicativoOptional = tenantRepository.buscar(appKeyOptional.get());

        if (!registroAplicativoOptional.isPresent()){
            return notFound("Aplicativo não registrado.");
        }

        Tenant tenant = Tenant.of(registroAplicativoOptional.get().getTenant());

        List<Campeonato> campeonatos = campeonatoRepository.todos(tenant, nome);

        List<Jsonable> jsons =  CampeonatoJson.of(campeonatos);

        // usa o builder
        ObjectJson.JsonBuilder<CampeonatoJson> builder = ObjectJson.build(CampeonatoJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        //adiciona as entidades
        campeonatos.forEach( campeonato -> builder.comEntidade(CampeonatoJson.of(campeonato)));

        JsonNode retorno = builder.build();
        return ok(retorno);
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {
        Optional<Campeonato> campeonato = campeonatoRepository.buscar(getTenant(), id);

        if (!campeonato.isPresent()) {
            return notFound("Campeonato não encontrado!");
        }
        CampeonatoJson jsonCampeonato = CampeonatoJson.of(campeonato.get());
        JsonNode json = ObjectJson.build(CampeonatoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT)
                .comEntidade(jsonCampeonato)
                .build();
        return ok(json);
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {
        try {
            campeonatoRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando exclui uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }

    private Optional<Campeonato> requestJson(){

        JsonNode json = request().body().asJson();
        String nome = json.findPath("nome").textValue();
        return Optional.ofNullable(new Campeonato(getTenant().get(), nome));
    }
}