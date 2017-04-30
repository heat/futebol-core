package api.rest;

import actions.TenantAction;
import api.json.ObjectJson;
import api.json.ResultadoJson;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.ApplicationController;
import dominio.processadores.apostas.AtualizarPalpitesProcessador;
import dominio.processadores.apostas.FinalizarPartidasProcessador;
import dominio.processadores.eventos.AtualizarResultadoProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.eventos.futebol.ResultadoFutebol;
import models.vo.Tenant;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.*;
import repositories.EventoRepository;
import repositories.ResultadoRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@With(TenantAction.class)
public class ResultadoController extends ApplicationController {

    ResultadoRepository resultadoRepository;
    AtualizarResultadoProcessador atualizarResultadoProcessador;
    ValidadorRepository validadorRepository;
    EventoRepository eventoRepository;
    FinalizarPartidasProcessador finalizarPartidasProcessador;

    @Inject
    public ResultadoController(ResultadoRepository resultadoRepository, PlaySessionStore playSessionStore,
                               AtualizarResultadoProcessador atualizarResultadoProcessador,
                               ValidadorRepository validadorRepository, EventoRepository eventoRepository,
                               FinalizarPartidasProcessador finalizarPartidasProcessador) {
        super(playSessionStore);
        this.resultadoRepository = resultadoRepository;
        this.atualizarResultadoProcessador = atualizarResultadoProcessador;
        this.validadorRepository = validadorRepository;
        this.eventoRepository = eventoRepository;
        this.finalizarPartidasProcessador = finalizarPartidasProcessador;

    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir(Long idresultado) throws IOException {

        JsonNode body = Controller.request()
                .body()
                .asJson();

        Tenant tenant = getTenant();

        ResultadoJson resultado = Json.fromJson(body.get("resultado"), ResultadoJson.class);

        Long idEvento = resultado.evento;

        Resultado entidade = resultado.to(tenant);

        List<Validador> validadoresEventos = validadorRepository.todos(getTenant(), AtualizarResultadoProcessador.REGRA);
        List<Validador> validadoresPalpites = validadorRepository.todos(getTenant(), AtualizarPalpitesProcessador.REGRA);
        List<Validador> validadoresBilhetes = validadorRepository.todos(getTenant(), AtualizarPalpitesProcessador.REGRA);

        Optional<Evento> eventoOptional = eventoRepository.buscar(getTenant(), idEvento);

        if(!eventoOptional.isPresent())
            return notFound("Evento não encontrado");

            try {
                Evento ev = atualizarResultadoProcessador.executar(entidade, eventoOptional.get(), validadoresEventos)
                .get();

                Optional<Resultado> ra = ev.getResultados().stream().filter( r -> r.getId() == idresultado).findFirst();
                ObjectJson.JsonBuilder<ResultadoJson> builder = ObjectJson.build(ResultadoJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);
                builder.comEntidade(ResultadoJson.of(ev, ra.get()));
                return ok(builder.build());

        } catch (ValidadorExcpetion   ex) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, ex.getMessage());
        } catch (ExecutionException|InterruptedException e) {
            return status(Http.Status.INTERNAL_SERVER_ERROR);
        }

    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos(Long eventoId) {

        if(eventoId == null || eventoId <= 0) {
            badRequest("deve ser informado um padrao de busca");
        }

        Tenant tenant = getTenant();
        Optional<Evento> _e = eventoRepository.buscar(tenant, eventoId);
        if(!_e.isPresent()) {
            return notFound();
        }

        Evento evento = _e.get();

        List<Resultado> resultados = evento.getResultados();

        // usa o builder
        ObjectJson.JsonBuilder<ResultadoJson> builder = ObjectJson.build(ResultadoJson.TIPO, ObjectJson.JsonBuilderPolicy.COLLECTION);
        //adiciona as entidades

        resultados  = ResultadoFutebol.merge(resultados, ResultadoFutebol._default(evento));
        resultados.forEach( resultado -> builder.comEntidade(ResultadoJson.of(evento, resultado)));


        return ok(builder.build());
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(Long id) {

        Optional<Resultado> todos = (Optional<Resultado>) resultadoRepository.buscar(getTenant(), id);

        if (!todos.isPresent()) {
            return notFound("Resultado não encontrado!");
        }
        return ok("Resultado Cadastrado - retornar json corretamente");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result excluir(Long id) {

        try {
            resultadoRepository.excluir(getTenant(), id);
            return noContent();
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }
}
