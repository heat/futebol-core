package models.apostas.odd.resultados.handicap;

import models.apostas.Odd;
import models.apostas.mercado.HandicapAsiaticoMercado;
import models.apostas.mercado.Mercado;

import javax.persistence.Entity;

@Entity
public class CasaHandicapAsiaticoOdd extends Odd<HandicapAsiaticoMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando casa ganha a partida por diferença de gols";

    private static final String NOME = "Casa";

    private static final String ABREVIACAO = "C";

    public CasaHandicapAsiaticoOdd() {
    }

    public CasaHandicapAsiaticoOdd(String codigo) {
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
    public HandicapAsiaticoMercado.Posicao getPosicao() {
        return HandicapAsiaticoMercado.Posicao.CASA;
    }
}
