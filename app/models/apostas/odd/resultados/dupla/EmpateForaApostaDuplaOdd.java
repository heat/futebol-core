package models.apostas.odd.resultados.dupla;

import models.apostas.Odd;
import models.apostas.mercado.ApostaDuplaMercado;
import models.apostas.mercado.Mercado;

import javax.persistence.Entity;

@Entity
public class EmpateForaApostaDuplaOdd extends Odd<ApostaDuplaMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa empata ou perde";

    private static final String NOME = "ForaEmpate";

    private static final String ABREVIACAO = "FE";

    public EmpateForaApostaDuplaOdd() {
    }

    public EmpateForaApostaDuplaOdd(String codigo) {
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
    public ApostaDuplaMercado.Posicao getPosicao() {
        return ApostaDuplaMercado.Posicao.EMPATE_FORA;
    }
}
