package models.apostas.mercado;

public class ApostaDuplaMercado extends Mercado {

    private static final String NOME = "Aposta Dupla";

    private static final TipoMercado TIPO = TipoMercado.S;

    public ApostaDuplaMercado() {
        super(NOME, TIPO);
    }

    @Override
    public String getId() {
        return "aposta-dupla";
    }

    public enum Posicao {
        CASA_EMPATE,
        EMPATE_FORA,
        CASA_FORA
    }
}
