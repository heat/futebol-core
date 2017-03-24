package models.apostas.odd.resultados.intervalofinal;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoIntervaloFinalMercado;
import models.eventos.futebol.ResultadoFutebol;

import javax.persistence.Entity;

@Entity
public class EmpateForaIntervaloFinalOdd extends Odd<ResultadoIntervaloFinalMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa empata no primeiro e perde no segundo tempo";

    private static final String NOME = "Empate - Fora";

    private static final String ABREVIACAO = "E-F";

    public EmpateForaIntervaloFinalOdd() {
    }

    public EmpateForaIntervaloFinalOdd(String codigo) {
        super(codigo);
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public Mercado getMercado() {
        return Mercado.ResultadoFinal;
    }

    @Override
    public String getAbreviacao() {
        return ABREVIACAO;
    }

    @Override
    public String getDescricao() {
        return DESCRICAO;
    }

    @Override
    public ResultadoIntervaloFinalMercado.Posicao getPosicao() {
        return ResultadoIntervaloFinalMercado.Posicao.EMPATE_FORA;
    }

    @Override
    public Calculadora getCalculadora(Taxa taxa) {

        return new CalculadoraM();
    }

    public class CalculadoraM implements Calculadora<ResultadoFutebol> {
        @Override
        public boolean calcular(ResultadoFutebol resultado) {
            Long pontosCasaPrimeiro = resultado.casaPrimeiroTempo.getPontos();
            Long pontosForaPrimeiro = resultado.foraPrimeiroTempo.getPontos();
            Long pontosCasaSegundo = resultado.casaSegundoTempo.getPontos();
            Long pontosForaSegundo = resultado.foraSegundoTempo.getPontos();
            return (pontosCasaPrimeiro == pontosForaPrimeiro) && (pontosCasaSegundo < pontosForaSegundo);
        }
    }
}
