package dominio.processadores.eventos;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.vo.Tenant;
import repositories.EventoRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FinalizarEventoProcessador implements Processador<Tenant, Evento>{


    public static final String REGRA = "resultado.inserir";

    EventoRepository eventoRepository;

    @Inject
    public FinalizarEventoProcessador(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Override
    public CompletableFuture<Evento> executar(Tenant tenant, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

        List<Resultado> resultados = evento.getResultados();
        for(Resultado result : resultados){
            result.setTenant(tenant.get());
        }

/*        for (Validador validador : validadores) {
            validador.validate(evento);
        }*/

        try{
         eventoRepository.atualizar(tenant, evento.getId(), evento);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(evento);
    }
}
