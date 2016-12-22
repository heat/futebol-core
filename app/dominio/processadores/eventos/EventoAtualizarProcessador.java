package dominio.processadores.eventos;

import models.eventos.Evento;
import models.vo.Tenant;
import repositories.EventoRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class EventoAtualizarProcessador {

    public static final String REGRA = "evento";
    private final Long idEvento;

    EventoRepository repository;

    @Inject
    public EventoAtualizarProcessador(EventoRepository repository, Long idEvento) {
        this.repository = repository;
        this.idEvento = idEvento;
    }

    public CompletableFuture<Evento> executar(Tenant tenant, Evento eventoNovo, List<Validador> validators) throws ValidadorExcpetion {

        for (Validador validator : validators) {
            validator.validate(eventoNovo);
        }

        Optional<Evento> eventoAtual = repository.buscar(tenant, idEvento);

        if(eventoAtual.isPresent()){
            validate(eventoAtual.get(), eventoNovo);
        }

        try{
            repository.atualizar(tenant, idEvento, eventoNovo);
        }
        catch (NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(eventoNovo);
    }

    private void validate(Evento eventoAtual, Evento eventoNovo) throws ValidadorExcpetion {

        if(!eventoNovo.getCasa().equals(eventoAtual.getCasa())){
            throw new ValidadorExcpetion("O nome do time da casa não pode ser alterado! ");
        }

        if(!eventoNovo.getFora().equals(eventoAtual.getFora())){
            throw new ValidadorExcpetion("O nome do time de fora não pode ser alterado! ");
        }

        if(!eventoNovo.getCampeonato().equals(eventoAtual.getCampeonato())){
            throw new ValidadorExcpetion("O campeonato não pode ser alterado! ");
        }
    }
}
