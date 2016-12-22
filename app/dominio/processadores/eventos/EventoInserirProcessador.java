package dominio.processadores.eventos;

import dominio.processadores.Processador;
import models.eventos.Evento;
import models.vo.Tenant;
import repositories.EventoRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventoInserirProcessador implements Processador<Evento>{


    public static final String REGRA = "evento";

    EventoRepository repository;

    @Inject
    public EventoInserirProcessador(EventoRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Evento> executar(Tenant tenant, Evento evento, List<Validador> validators) throws ValidadorExcpetion {

        for (Validador validator : validators) {
            validator.validate(evento);
        }

        repository.inserir(tenant, evento);
        return CompletableFuture.completedFuture(evento);
    }
}
