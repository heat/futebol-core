package models.apostas.odd.resultados.gols;

import models.apostas.Odd;
import models.apostas.mercado.HandicapAsiaticoMercado;
import models.apostas.mercado.Mercado;
import models.apostas.mercado.NumeroGolsMercado;

import javax.persistence.Entity;

@Entity
public class AcimaNumeroGolsOdd extends Odd<NumeroGolsMercado.Posicao> {

    private static final String DESCRICAO = "Ganha quando sai acima de X gols na partida";

    private static final String NOME = "Acima";

    private static final String ABREVIACAO = "A";

    public AcimaNumeroGolsOdd() {
    }

    public AcimaNumeroGolsOdd(String codigo) {
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
        return NumeroGolsMercado.Posicao.ACIMA;
    }
}
