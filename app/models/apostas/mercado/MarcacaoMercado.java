package models.apostas.mercado;

public class MarcacaoMercado extends Mercado {

    private static final String NOME = "Marcação de gols";

    public enum Posicao {
        NINGUEM, SOMENTE, AMBOS
    }

    public MarcacaoMercado() {
        super(NOME, TipoMercado.S);
    }
}
