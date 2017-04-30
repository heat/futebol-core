package dominio.processadores.eventos;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.vo.Tenant;
import repositories.EventoRepository;
import repositories.ResultadoRepository;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventoInserirProcessador implements Processador<Tenant, Evento> {

    public static final String REGRA = "evento.inserir";

    EventoRepository eventoRepository;

    ResultadoRepository resultadoRepository;

    @Inject
    public EventoInserirProcessador(EventoRepository eventoRepository, ResultadoRepository resultadoRepository) {
        this.eventoRepository = eventoRepository;
        this.resultadoRepository = resultadoRepository;
    }

    public CompletableFuture<Evento> executar(Tenant tenant, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
             validador.validate(evento);
        }
        return eventoRepository.inserir(tenant, evento)
                .thenCompose(ev -> {
                    ev.setResultados(
                            Arrays.asList(
                                new Resultado(tenant.get(), Resultado.Momento.I, 0L, ev.getCasa()),
                                new Resultado(tenant.get(), Resultado.Momento.I, 0L, ev.getFora()),
                                new Resultado(tenant.get(), Resultado.Momento.F, 0L, ev.getCasa()),
                                new Resultado(tenant.get(), Resultado.Momento.F, 0L, ev.getFora())
                            )
                    );
                    return eventoRepository.atualizar(tenant, ev.getId(), ev);
                });
    }
}
