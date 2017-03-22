package models.apostas.odd.resultados.dupla;

import models.apostas.Odd;
import models.apostas.mercado.ApostaDuplaMercado;
import models.apostas.mercado.Mercado;

import javax.persistence.Entity;

@Entity
public class CasaForaApostaDuplaOdd extends Odd<ApostaDuplaMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa ganha ou perde";

    private static final String NOME = "CasaFora";

    private static final String ABREVIACAO = "CF";

    public CasaForaApostaDuplaOdd() {
    }

    public CasaForaApostaDuplaOdd(String codigo) {
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
        return ApostaDuplaMercado.Posicao.CASA_FORA;
    }
}
