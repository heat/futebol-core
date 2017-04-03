package models.apostas.mercado;

public class MarcacaoMercado extends Mercado {

    private static final String NOME = "Marcação de gols";

    private static final TipoMercado TIPO = TipoMercado.S;

    @Override
    public String getId() {
        return "marcacao-gols";
    }

    public enum Posicao {
        NINGUEM, SOMENTE, AMBOS
    }

    public MarcacaoMercado() {
        super(NOME, TIPO);
    }
}
