package api.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.ApplicationController;
import dominio.processadores.apostas.AtualizarBilhetesFinalizacaoPartidaProcessador;
import dominio.processadores.apostas.AtualizarPalpitesProcessador;
import dominio.processadores.eventos.FinalizarEventoProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.vo.Chave;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.EventoRepository;
import repositories.ResultadoRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ResultadoController extends ApplicationController {

    ResultadoRepository resultadoRepository;
    FinalizarEventoProcessador finalizarEventoProcessador;
    ValidadorRepository validadorRepository;
    EventoRepository eventoRepository;
    AtualizarPalpitesProcessador atualizarPalpitesProcessador;
    AtualizarBilhetesFinalizacaoPartidaProcessador atualizaBilhetesFinalizacaoPartidaProcessador;

    @Inject
    public ResultadoController(ResultadoRepository resultadoRepository, PlaySessionStore playSessionStore,
                               FinalizarEventoProcessador finalizarEventoProcessador,
                               ValidadorRepository validadorRepository, EventoRepository eventoRepository,
                               AtualizarPalpitesProcessador atualizarPalpitesProcessador,
                               AtualizarBilhetesFinalizacaoPartidaProcessador atualizaBilhetesFinalizacaoPartidaProcessador) {
        super(playSessionStore);
        this.resultadoRepository = resultadoRepository;
        this.finalizarEventoProcessador = finalizarEventoProcessador;
        this.validadorRepository = validadorRepository;
        this.eventoRepository = eventoRepository;
        this.atualizarPalpitesProcessador = atualizarPalpitesProcessador;
        this.atualizaBilhetesFinalizacaoPartidaProcessador = atualizaBilhetesFinalizacaoPartidaProcessador;

    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir(Long idEvento) throws IOException {

        JsonNode json = Controller.request()
                .body()
                .asJson();
        ObjectMapper mapper = new ObjectMapper();

        List<Resultado> resultados = mapper.readValue(json.toString(), new TypeReference<List<Resultado>>() { });

        if(!Optional.ofNullable(resultados).isPresent())
            return notFound("Lista de resultados não pode ser vazia!");

        List<Validador> validadoresEventos = validadorRepository.todos(getTenant(), FinalizarEventoProcessador.REGRA);
        List<Validador> validadoresPalpites = validadorRepository.todos(getTenant(), AtualizarPalpitesProcessador.REGRA);
        List<Validador> validadoresBilhetes = validadorRepository.todos(getTenant(), AtualizarPalpitesProcessador.REGRA);

        Optional<Evento> eventoOptional = eventoRepository.buscar(getTenant(), idEvento);
        if(!eventoOptional.isPresent())
            return notFound("Evento não encontrado");

        Evento evento = eventoOptional.get();
        for(Resultado resultado: resultados){
            evento.addResultado(resultado);
        }
        Chave chave = Chave.of(getTenant(), idEvento);
        try {

            finalizarEventoProcessador.executar(chave, evento, validadoresEventos)
                    .thenCompose( eventoFinalizado ->
                            atualizarPalpitesProcessador.executar(chave, eventoFinalizado, validadoresPalpites)
                    ).thenCompose( eventoFinalizado ->
                        atualizaBilhetesFinalizacaoPartidaProcessador.executar(chave, eventoFinalizado, validadoresBilhetes) )
                    .thenApply( eventoFinalizado -> {
                        return eventoFinalizado;
                    });

        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        return created(Json.toJson(resultados));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {

        List todos = resultadoRepository.todos(getTenant());
        return ok("Resultado Cadastrado - retornar json corretamente");
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
