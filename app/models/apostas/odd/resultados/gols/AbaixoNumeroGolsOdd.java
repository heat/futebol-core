package models.apostas.odd.resultados.gols;

import models.apostas.Odd;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.NumeroGolsMercado;

import javax.persistence.Entity;

@Entity
public class AbaixoNumeroGolsOdd extends Odd<NumeroGolsMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando sai abaixo de X gols na partida";

    private static final String NOME = "Abaixo";

    private static final String ABREVIACAO = "A";

    public AbaixoNumeroGolsOdd() {
    }

    public AbaixoNumeroGolsOdd(String codigo) {
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
    public NumeroGolsMercado.Posicao getPosicao() {
        return NumeroGolsMercado.Posicao.ABAIXO;
    }
}
