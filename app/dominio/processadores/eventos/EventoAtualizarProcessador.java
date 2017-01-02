package dominio.processadores.eventos;

import dominio.processadores.Processador;
import models.eventos.Evento;
import models.vo.Chave;
import models.vo.Tenant;
import repositories.EventoRepository;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class EventoAtualizarProcessador implements Processador<Chave, Evento> {

    public static final String REGRA = "evento.atualizar";

    EventoRepository repository;

    @Inject
    public EventoAtualizarProcessador(EventoRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Evento> executar(Chave chave, Evento eventoNovo, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(eventoNovo);
        }

        Optional<Evento> eventoAtual = repository.buscar(chave.getTenant(), chave.getId());

        if(eventoAtual.isPresent()) validate(eventoAtual.get(), eventoNovo);

        try{
            repository.atualizar(chave.getTenant(), chave.getId(), eventoNovo);
        }
        catch (NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(eventoNovo);
    }

    /**
     * Validar se o nome de pelo menos um dos times foi alterado
     * @param eventoAtual
     * @param eventoNovo
     * @throws ValidadorExcpetion
     */
    private void validate(Evento eventoAtual, Evento eventoNovo) throws ValidadorExcpetion {

        if(!eventoNovo.getCasa().equals(eventoAtual.getCasa())){
            throw new ValidadorExcpetion("O nome dos times não podem ser alterados! ");
        }

        if(!eventoNovo.getFora().equals(eventoAtual.getFora())){
            throw new ValidadorExcpetion("O nome dos times não podem ser alterados! ");
        }

/*        if(!eventoNovo.getCampeonato().equals(eventoAtual.getCampeonato())){
            throw new ValidadorExcpetion("O campeonato não pode ser alterado! ");
        }*/
    }
}
