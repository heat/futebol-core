package models.apostas.odd.resultados.intervalofinal;

import models.apostas.Odd;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoFinalMercado;
import models.apostas.mercado.ResultadoIntervaloFinalMercado;

import javax.persistence.Entity;

@Entity
public class CasaCasaIntervaloFinalOdd extends Odd<ResultadoIntervaloFinalMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa ganha no primeiro e segundo tempo";

    private static final String NOME = "Casa - Casa";

    private static final String ABREVIACAO = "C-C";

    public CasaCasaIntervaloFinalOdd() {
    }

    public CasaCasaIntervaloFinalOdd(String codigo) {
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
        return ResultadoIntervaloFinalMercado.Posicao.CASA_CASA;
    }
}
