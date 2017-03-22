package models.apostas.odd.resultados.intervalo;

import models.apostas.Odd;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.ResultadoIntervaloMercado;

import javax.persistence.Entity;

@Entity
public class EmpateIntervaloOdd extends Odd<ResultadoIntervaloMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa empata no primeiro tempo";

    private static final String NOME = "Empate1T";

    private static final String ABREVIACAO = "E1T";

    public EmpateIntervaloOdd() {
    }

    public EmpateIntervaloOdd(String codigo) {
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
        return ResultadoIntervaloMercado.Posicao.EMPATE;
    }
}
