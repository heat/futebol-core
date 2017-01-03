package dominio.processadores.apostas;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.vo.Chave;
import repositories.EventoApostaRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventoApostaAtualizarProcessador implements Processador<Chave, EventoAposta>{

    public static final String REGRA = "evento_aposta.atualizar";

    EventoApostaRepository eventoApostaRepository;

    @Inject
    public EventoApostaAtualizarProcessador(EventoApostaRepository eventoApostaRepository) {
        this.eventoApostaRepository = eventoApostaRepository;
    }

    @Override
    public CompletableFuture<EventoAposta> executar(Chave chave, EventoAposta eventoAposta, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(eventoAposta);
        }
        try{
            eventoApostaRepository.atualizar(chave.getTenant(), chave.getId(), eventoAposta);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(eventoAposta);
    }
}
