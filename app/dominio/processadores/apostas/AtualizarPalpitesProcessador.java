package dominio.processadores.apostas;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.bilhetes.Palpite;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.eventos.ResultadoEvento;
import models.vo.Chave;
import repositories.EventoApostaRepository;
import repositories.PalpiteRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Deprecated
public class AtualizarPalpitesProcessador implements Processador<Chave, Evento>{

    public static final String REGRA = "palpite.atualizar";

    EventoApostaRepository eventoApostaRepository;
    PalpiteRepository palpiteRepository;

    @Inject
    public AtualizarPalpitesProcessador(EventoApostaRepository eventoApostaRepository, PalpiteRepository palpiteRepository) {
        this.eventoApostaRepository = eventoApostaRepository;
        this.palpiteRepository = palpiteRepository;
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
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }
        return CompletableFuture.completedFuture(evento);
    }

    private boolean isPalpiteCorreto(Evento evento, List<Resultado> resultados, Odd odd){

        String oddSelecionada = odd.getNome();
        oddSelecionada = oddSelecionada.toUpperCase();
        switch (oddSelecionada) {
            // case Odds.CASA_RESULTADO_FINAL:
            //    return isResultadoCasaCorreto(evento, resultados);
            // case Odds.FORA_RESULTADO_FINAL:
            //    return isResultadoForaCorreto(evento, resultados);
            //case Odds.EMPATE:
            //    return isResultadoEmpateCorreto(evento, resultados);
            default:
                System.out.println("Lembre de completar os demais cases, MIKE!!!!");
        }
        return false;
    }

    private boolean isResultadoCasaCorreto(Evento evento, List<Resultado> resultados){

        Long pontosCasa = Long.valueOf(0);
        Long pontosFora = Long.valueOf(0);
        for(Resultado resultado: resultados){
            if(resultado.isMomentoFinal() && resultado.getTime().equals(evento.getCasa()))
                pontosCasa = resultado.getPontos();
            if(resultado.isMomentoFinal() && resultado.getTime().equals(evento.getCasa()))
                pontosFora = resultado.getPontos();
        }
        return pontosCasa > pontosFora ? true : false;
    }

    private boolean isResultadoForaCorreto(Evento evento, List<Resultado> resultados){

        Long pontosCasa = Long.valueOf(0);
        Long pontosFora = Long.valueOf(0);
        for(Resultado resultado: resultados){
            if(resultado.isMomentoFinal() && resultado.getTime().equals(evento.getCasa()))
                pontosCasa = resultado.getPontos();
            if(resultado.isMomentoFinal() && resultado.getTime().equals(evento.getCasa()))
                pontosFora = resultado.getPontos();
        }
        return pontosFora > pontosCasa ? true : false;
    }

    private boolean isResultadoEmpateCorreto(Evento evento, List<Resultado> resultados){

        Long pontosCasa = Long.valueOf(0);
        Long pontosFora = Long.valueOf(0);
        for(Resultado resultado: resultados){
            if(resultado.isMomentoFinal() && resultado.getTime().equals(evento.getCasa()))
                pontosCasa = resultado.getPontos();
            if(resultado.isMomentoFinal() && resultado.getTime().equals(evento.getCasa()))
                pontosFora = resultado.getPontos();
        }
        return pontosFora == pontosCasa ? true : false;
    }
}
