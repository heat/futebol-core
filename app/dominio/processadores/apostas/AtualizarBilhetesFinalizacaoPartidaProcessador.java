package dominio.processadores.apostas;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.apostas.Taxa;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.eventos.Evento;
import models.vo.Chave;
import repositories.EventoApostaRepository;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AtualizarBilhetesFinalizacaoPartidaProcessador implements Processador<Chave, Evento>{

    public static final String REGRA = "palpite.atualizar";

    EventoApostaRepository eventoApostaRepository;

    @Inject
    public AtualizarBilhetesFinalizacaoPartidaProcessador(EventoApostaRepository eventoApostaRepository) {
        this.eventoApostaRepository = eventoApostaRepository;
    }

    @Override
    public CompletableFuture<Evento> executar(Chave chave, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

        List<Bilhete> bilhetes = Collections.emptyList();

        try{
            for(Bilhete bilhete: bilhetes){
                /*
                * Neste momento, a partida já foi finalizada e os palpites da mesma foram atualizados.
                *
                * */
               boolean isBilheteCorreto = true;

            }
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }
        return CompletableFuture.completedFuture(evento);
    }
}
