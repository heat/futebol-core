package api.rest.trading;

import actions.TenantAction;
import api.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import controllers.ApplicationController;
import dominio.processadores.apostas.OddConfigAtualizarProcessador;
import dominio.processadores.eventos.CampeonatoAtualizarProcessador;
import dominio.processadores.eventos.CampeonatoInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.OddConfiguracao;
import models.eventos.Campeonato;
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
import repositories.CampeonatoRepository;
import repositories.OddRepository;
import repositories.TenantRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@With(TenantAction.class)
public class OddController extends ApplicationController {

    ValidadorRepository validadorRepository;
    TenantRepository tenantRepository;
    OddRepository oddRepository;
    OddConfigAtualizarProcessador oddConfigAtualizarProcessador;

    @Inject
    public OddController(PlaySessionStore playSessionStore,
                         ValidadorRepository validadorRepository, TenantRepository tenantRepository,
                         OddRepository oddRepository, OddConfigAtualizarProcessador oddConfigAtualizarProcessador) {
        super(playSessionStore);
        this.validadorRepository = validadorRepository;
        this.tenantRepository = tenantRepository;
        this.oddRepository = oddRepository;
        this.oddConfigAtualizarProcessador = oddConfigAtualizarProcessador;
    }


    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        Tenant tenant = getTenantAppCode();
        List<OddConfiguracao> odds = oddRepository.todosConfiguracao(tenant);

        List<Jsonable> jsons =  OddJson.of(odds);
        // usa o builder
        ObjectJson.JsonBuilder<OddJson> builder = ObjectJson.build(OddJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        //adiciona as entidades
        odds.forEach( odd -> {
            builder.comEntidade(OddJson.of(odd));
            builder.comRelacionamento(MercadoJson.TIPO, MercadoJson.of(odd.getOdd().getMercado()));
        });

        JsonNode retorno = builder.build();
        return ok(retorno);
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        OddJson oddJson = Json.fromJson(request().body().asJson().get("odd"), OddJson.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), OddConfigAtualizarProcessador.REGRA);

        OddConfiguracao oddConfiguracao = oddJson.toConfiguracao();

        try {
            Chave chave = Chave.of(getTenant(), id);
            oddConfigAtualizarProcessador.executar(chave, oddConfiguracao, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        JsonNode json = ObjectJson.build(ParametroJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT)
                .comEntidade(OddJson.of(oddConfiguracao))
                .build();
        return ok(json);
    }
}