package models.apostas.odd.resultados.termino;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoFinalMercado;
import models.apostas.odd.Posicionavel;
import models.eventos.Resultado;

import javax.persistence.Entity;

@Entity
public class CasaResultadoFinalOdd extends Odd<ResultadoFinalMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa ganha a partida";

    private static final String NOME = "Casa";

    private static final String ABREVIACAO = "C";

    public CasaResultadoFinalOdd() {
    }

    public CasaResultadoFinalOdd(String codigo) {
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
        return ResultadoFinalMercado.Posicao.CASA;
    }

    @Override
    public Calculadora getCalculadora(Taxa taxa) {

        return new CalculadoraM();
    }

    public class CalculadoraM implements Calculadora<Resultado> {
        @Override
        public boolean calcular(Resultado resultado) {
            return false;
        }
    }
}
