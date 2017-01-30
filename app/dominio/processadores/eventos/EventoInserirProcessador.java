package dominio.processadores.eventos;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Evento;
import models.vo.Tenant;
import repositories.EventoRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventoInserirProcessador implements Processador<Tenant, Evento> {

    public static final String REGRA = "evento.inserir";

    EventoRepository eventoRepository;

    @Inject
    public EventoInserirProcessador(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public CompletableFuture<Evento> executar(Tenant tenant, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
             validador.validate(evento);
        }
        return eventoRepository.inserir(tenant, evento);
    }
}
