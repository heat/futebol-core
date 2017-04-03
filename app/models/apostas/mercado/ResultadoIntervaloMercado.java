package models.apostas.mercado;

public class ResultadoIntervaloMercado extends Mercado {

    private static final String NOME = "Resultado Intervalo";

    private static final TipoMercado TIPO = TipoMercado.S;

    public ResultadoIntervaloMercado() {
        super(NOME, TIPO);
    }

    @Override
    public String getId() {
        return "resultado-intervalo";
    }

    public enum Posicao {
        CASA, EMPATE, FORA
    }
}
