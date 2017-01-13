package dominio.processadores.apostas;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.apostas.Odd;
import models.apostas.Odds;
import models.apostas.Taxa;
import models.bilhetes.Palpite;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.vo.Chave;
import repositories.EventoApostaRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AtualizarPalpitesProcessador implements Processador<Chave, Evento>{

    public static final String REGRA = "palpite.atualizar";

    EventoApostaRepository eventoApostaRepository;

    @Inject
    public AtualizarPalpitesProcessador(EventoApostaRepository eventoApostaRepository) {
        this.eventoApostaRepository = eventoApostaRepository;
    }

    @Override
    public CompletableFuture<Evento> executar(Chave chave, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(evento);
        }

        List<Resultado> resultados = evento.getResultados();
        EventoAposta eventoAposta = evento.getEventoAposta();
        List<Taxa> taxas = eventoAposta.getTaxas();

        try{
           for(Taxa taxa : taxas){
               for(Palpite palpite: taxa.getPalpites()){
                   if(isPalpiteCorreto(evento,resultados, taxa.getOdd()))
                      palpite.setStatus(Palpite.Status.CERTO);
                   else
                      palpite.setStatus(Palpite.Status.ERRADO);
               }
           }
           eventoAposta.setPermitir(false);
           eventoApostaRepository.atualizar(chave.getTenant(), chave.getId(), eventoAposta);
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
            case Odds.CASA_RESULTADO_FINAL:
                return isResultadoCasaCorreto(evento, resultados);
            case Odds.FORA_RESULTADO_FINAL:
                return isResultadoForaCorreto(evento, resultados);
            case Odds.EMPATE:
                return isResultadoEmpateCorreto(evento, resultados);
            /*E todas as demais odds
            * Necessário verificar os casos nas quais as odds possuem
            * o mesmo nome : casa e fora para resultado final e handicap*/
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
/*
    private Resultado resultadoFinalCasa(Evento evento, List<Resultado> resultados){

        for(Resultado resultado: resultados){
            if(resultado.isMomentoFinal() && resultado.getTime().equals(evento.getCasa()))
                return resultado;
        }
        return null;
    }

    private Resultado resultadoFinalFora(Evento evento, List<Resultado> resultados){

        for(Resultado resultado: resultados){
            if(resultado.isMomentoFinal() && resultado.getTime().equals(evento.getCasa()))
                return resultado;
        }
        return null;
    }
*/


}
