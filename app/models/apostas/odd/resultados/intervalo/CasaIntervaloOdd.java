package models.apostas.odd.resultados.intervalo;

import models.apostas.Calculadora;
import models.apostas.Odd;
import models.apostas.Taxa;
import models.apostas.mercado.ApostaDuplaMercado;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoIntervaloMercado;
import models.eventos.futebol.ResultadoFutebol;

import javax.persistence.Entity;

@Entity
public class CasaIntervaloOdd extends Odd<ResultadoIntervaloMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa ganha no primeiro tempo";

    private static final String NOME = "Casa1T";

    private static final String ABREVIACAO = "C1T";

    public CasaIntervaloOdd() {
    }

    public CasaIntervaloOdd(String codigo) {
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
    public ResultadoIntervaloMercado.Posicao getPosicao() {
        return ResultadoIntervaloMercado.Posicao.CASA;
    }

    @Override
    public Calculadora getCalculadora(Taxa taxa) {

        return new CalculadoraM();
    }

    public class CalculadoraM implements Calculadora<ResultadoFutebol> {
        @Override
        public boolean calcular(ResultadoFutebol resultado) {
            Long pontosCasa = resultado.casaPrimeiroTempo.getPontos();
            Long pontosFora = resultado.foraPrimeiroTempo.getPontos();
            return pontosCasa > pontosFora;
        }
    }
}
