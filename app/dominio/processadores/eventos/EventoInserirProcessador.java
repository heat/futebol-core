package dominio.processadores.eventos;

import dominio.processadores.ProcessadorInserir;
import models.eventos.Evento;
import models.vo.Tenant;
import repositories.EventoRepository;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventoInserirProcessador implements ProcessadorInserir<Evento> {


    public static final String REGRA = "evento.inserir";

    EventoRepository repository;

    @Inject
    public EventoInserirProcessador(EventoRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Evento> executar(Tenant tenant, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(evento);
        }

        repository.inserir(tenant, evento);
        return CompletableFuture.completedFuture(evento);
    }
}
