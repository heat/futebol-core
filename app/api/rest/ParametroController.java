package api.rest;

import actions.TenantAction;
import api.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationController;
import dominio.processadores.parametros.ParametroAtualizarProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.vo.Chave;
import models.vo.Tenant;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repositories.TenantRepository;
import repositories.ValidadorRepository;
import play.libs.Json;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@With(TenantAction.class)
public class ParametroController extends ApplicationController {

    ValidadorRepository validadorRepository;
    TenantRepository tenantRepository;
    ParametroAtualizarProcessador parametroAtualizarProcessador;

    @Inject
    public ParametroController(PlaySessionStore playSessionStore,
                               ValidadorRepository validadorRepository, TenantRepository tenantRepository,
                               ParametroAtualizarProcessador parametroAtualizarProcessador) {
        super(playSessionStore);
        this.validadorRepository = validadorRepository;
        this.tenantRepository = tenantRepository;
        this.parametroAtualizarProcessador = parametroAtualizarProcessador;
    }


    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        Tenant tenant = getTenantAppCode();
        List<Validador> parametros = validadorRepository.todosEditaveis(tenant);

        // usa o builder
        ObjectJson.JsonBuilder<ParametroJson> builder = ObjectJson.build(ParametroJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        //adiciona as entidades
        parametros.forEach( p -> {
            builder.comEntidade(ParametroJson.of(p));
        });

        JsonNode retorno = builder.build();
        return ok(retorno);
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        ParametroJson parametroJson = Json.fromJson(request().body().asJson().get("parametro"), ParametroJson.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), ParametroAtualizarProcessador.REGRA);

        Optional<Validador> validadorOptional = validadorRepository.buscar(getTenant(), id);

        if(!validadorOptional.isPresent())
            return badRequest("Parâmetro ausente");

        Validador validador = parametroJson.to(validadorOptional.get());

        try {
            Chave chave = Chave.of(getTenant(), id);
            parametroAtualizarProcessador.executar(chave, validador, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        JsonNode json = ObjectJson.build(ParametroJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT)
                .comEntidade(ParametroJson.of(validador))
                .build();
        return ok(json);
    }

}