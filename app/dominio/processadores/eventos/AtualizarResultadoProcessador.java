package dominio.processadores.eventos;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.vo.Chave;
import models.vo.Tenant;
import repositories.EventoRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AtualizarResultadoProcessador implements Processador<Resultado, Evento>{


    public static final String REGRA = "resultado.inserir";

    EventoRepository eventoRepository;

    @Inject
    public AtualizarResultadoProcessador(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Override
    public CompletableFuture<Evento> executar( Resultado resultado, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {


        for (Validador validador : validadores) {
            validador.validate(evento);
        }

            evento.getResultados().stream().filter( r -> resultado.getTime().getId() == resultado.getTime().getId() && resultado.getMomento() == resultado.getMomento())
                    .forEach( r -> r.setPontos(resultado.getPontos()));
         return eventoRepository.atualizar(Tenant.of(evento.getTenant()), evento.getId(), evento);
    }
}
