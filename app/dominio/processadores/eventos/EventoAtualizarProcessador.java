package dominio.processadores.eventos;

import models.eventos.Evento;
import models.vo.Tenant;
import repositories.EventoRepository;
import validators.Validator;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventoAtualizarProcessador {

    public static final String REGRA = "evento.atualizar";
    private final Long idEvento;

    EventoRepository repository;

    @Inject
    public EventoAtualizarProcessador(EventoRepository repository, Long idEvento) {
        this.repository = repository;
        this.idEvento = idEvento;
    }

    public CompletableFuture<Evento> executar(Tenant tenant, Evento evento, List<Validator> validators) throws ValidadorExcpetion {

        for (Validator validator : validators) {
            validator.validate(evento);
        }

        repository.atualizar(tenant, idEvento, evento);


        return CompletableFuture.completedFuture(evento);
    }
}
