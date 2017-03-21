package models.apostas.mercado;

public abstract class Mercado {

    final String nome;

    final TipoMercado tipo;

    public Mercado(String nome, TipoMercado tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public enum TipoMercado {
        L("LINHA"), S("SIMPLES");

        private final String tipo;

        TipoMercado(String tipo) {
            this.tipo = tipo;
        }
    }
}
