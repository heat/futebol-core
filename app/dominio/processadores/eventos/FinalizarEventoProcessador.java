package dominio.processadores.eventos;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.vo.Chave;
import repositories.EventoRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FinalizarEventoProcessador implements Processador<Chave, Evento>{


    public static final String REGRA = "resultado.inserir";

    EventoRepository eventoRepository;

    @Inject
    public FinalizarEventoProcessador(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Override
    public CompletableFuture<Evento> executar(Chave chave, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

        List<Resultado> resultados = evento.getResultados();
        for(Resultado result : resultados){
            result.setTenant(chave.getTenant().get());
        }

        for (Validador validador : validadores) {
            validador.validate(evento);
        }

        try{
         eventoRepository.atualizar(chave.getTenant(), evento.getId(), evento);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(evento);
    }
}
