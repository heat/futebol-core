package models.apostas.mercado;

public class HandicapAsiaticoMercado extends Mercado {

    private static final String NOME = "Handicap asiatico";

    private static final TipoMercado TIPO = TipoMercado.L;

    public HandicapAsiaticoMercado() {
        super(NOME, TIPO);
    }

    @Override
    public String getId() {
        return "handicap-asiatico";
    }

    public enum Posicao {
        CASA, FORA
    }
}
