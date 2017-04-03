package models.apostas.mercado;

public class ResultadoFinalMercado extends Mercado {

    private static final String NOME = "Resultado Final";

    private static final TipoMercado TIPO = TipoMercado.S;

    public ResultadoFinalMercado() {
        super(NOME, TIPO);
    }

    @Override
    public String getId() {
        return "resultado-final";
    }

    public enum Posicao {
        CASA, EMPATE, FORA
    }
}
