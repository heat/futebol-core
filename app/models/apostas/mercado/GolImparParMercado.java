package models.apostas.mercado;

public class GolImparParMercado extends Mercado{

    private static final String NOME = "Gol Impar/Par";

    private static final TipoMercado TIPO = TipoMercado.S;

    public GolImparParMercado() {
        super(NOME, TIPO);
    }

    @Override
    public String getId() {
        return "gol-impar-par";
    }

    public enum Posicao {
        IMPAR, PAR
    }
}
