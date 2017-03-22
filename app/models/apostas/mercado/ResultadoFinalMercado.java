package models.apostas.mercado;

public class ResultadoFinalMercado extends Mercado {

    private static final String NOME = "Resultado Final";

    public ResultadoFinalMercado() {
        super(NOME, TipoMercado.S);
    }

    public enum Posicao {
        CASA, EMPATE, FORA
    }
}
