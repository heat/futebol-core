package models.apostas.mercado;

public class ResultadoIntervaloFinalMercado extends Mercado{

    private static final String NOME = "Resultado Intervalo/Final";

    private static final TipoMercado TIPO = TipoMercado.S;

    public ResultadoIntervaloFinalMercado() {
        super(NOME, TIPO);
    }

    @Override
    public String getId() {
        return "resultado-intervalo-final";
    }

    public enum Posicao {
        CASA_CASA,
        CASA_EMPATE,
        CASA_FORA,
        EMPATE_CASA,
        EMPATE_EMPATE,
        EMPATE_FORA,
        FORA_CASA,
        FORA_EMPATE,
        FORA_FORA
    }

}
