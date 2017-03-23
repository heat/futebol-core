package models.apostas.odd.resultados.intervalofinal;

import models.apostas.Odd;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoIntervaloFinalMercado;

import javax.persistence.Entity;

@Entity
public class ForaEmpateIntervaloFinalOdd extends Odd<ResultadoIntervaloFinalMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa perde no primeiro e empata no segundo tempo";

    private static final String NOME = "Fora - Empate";

    private static final String ABREVIACAO = "F-E";

    public ForaEmpateIntervaloFinalOdd() {
    }

    public ForaEmpateIntervaloFinalOdd(String codigo) {
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
        return ResultadoIntervaloFinalMercado.Posicao.FORA_EMPATE;
    }
}
