package models.apostas.odd.resultados.termino;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoFinalMercado;
import models.eventos.futebol.ResultadoFutebol;

import javax.persistence.Entity;

@Entity
public class ForaResultadoFinalOdd extends Odd<ResultadoFinalMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando fora ganha a partida";

    private static final String NOME = "Fora";

    private static final String ABREVIACAO = "F";

    public ForaResultadoFinalOdd() {
    }

    public ForaResultadoFinalOdd(String codigo) {
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
    public ResultadoFinalMercado.Posicao getPosicao() {
        return ResultadoFinalMercado.Posicao.FORA;
    }

    @Override
    public Calculadora getCalculadora(Taxa taxa) {

        return new CalculadoraM();
    }

    public class CalculadoraM implements Calculadora<ResultadoFutebol> {
        @Override
        public boolean calcular(ResultadoFutebol resultado) {
            Long pontosCasa = resultado.casaSegundoTempo.getPontos();
            Long pontosFora = resultado.foraSegundoTempo.getPontos();
            return pontosCasa < pontosFora;
        }
    }
}
