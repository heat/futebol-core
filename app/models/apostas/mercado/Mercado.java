package models.apostas.mercado;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public abstract class Mercado {

    public static Mercado ResultadoExato = new ResultadoExatoMercado();
    public static Mercado ResultadoFinal = new ResultadoFinalMercado();

    final String nome;

    final TipoMercado tipo;

    public Mercado(String nome, TipoMercado tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public abstract String getId();

    public String getNome() {
        return nome;
    }

    public TipoMercado getTipo() {
        return tipo;
    }

    public enum TipoMercado {
        L("LINHA"), S("SIMPLES");

        private final String tipo;

        TipoMercado(String tipo) {
            this.tipo = tipo;
        }
    }
}
