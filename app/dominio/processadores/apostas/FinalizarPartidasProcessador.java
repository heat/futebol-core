package dominio.processadores.apostas;


import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.eventos.ResultadoEvento;
import models.vo.Chave;
import repositories.BilheteRepository;
import repositories.EventoApostaRepository;
import repositories.PalpiteRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FinalizarPartidasProcessador implements Processador<Chave, Evento> {

    public static final String REGRA = "bilhete.finalizar";

    EventoApostaRepository eventoApostaRepository;
    PalpiteRepository palpiteRepository;
    BilheteRepository bilheteRepository;

    @Inject
    public FinalizarPartidasProcessador(EventoApostaRepository eventoApostaRepository, PalpiteRepository palpiteRepository, BilheteRepository bilheteRepository) {
        this.eventoApostaRepository = eventoApostaRepository;
        this.palpiteRepository = palpiteRepository;
        this.bilheteRepository = bilheteRepository;
    }

    @Override
    public CompletableFuture<Evento> executar(Chave chave, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(evento);
        }

        List<Resultado> resultados = evento.getResultados();
        Optional<EventoAposta> eventoApostaOptional = eventoApostaRepository.buscarPorEvento(chave.getTenant(), evento.getId());
        if (!eventoApostaOptional.isPresent()){
            throw new ValidadorExcpetion("Evento Aposta não encontrado.");
        }

        EventoAposta eventoAposta = eventoApostaOptional.get();
        List<Long> taxas = eventoAposta.getTaxas().stream().map(e -> e.getId()).collect(Collectors.toList());
        ResultadoEvento resultadoEvento = evento.getResultadoFutebol();

        try{

            List<Palpite> palpites = palpiteRepository.buscarPorTaxas(chave.getTenant(), taxas);
            palpites.forEach(p -> p.calcular(resultadoEvento));

            List<Bilhete> bilhetes = bilheteRepository.todosPorPalpites(chave.getTenant(), evento.getId());
            bilhetes.forEach(b -> b.calcular());

        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }
        return CompletableFuture.completedFuture(evento);
    }

}
