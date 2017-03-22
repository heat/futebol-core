package models.apostas.mercado;

public class NumeroGolsMercado extends Mercado {

    public NumeroGolsMercado(String nome, TipoMercado tipo) {
        super(nome, tipo);
    }

    public enum Posicao {
        ABAIXO, ACIMA
    }
}
