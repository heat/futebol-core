package models.apostas.mercado;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public abstract class Mercado {

    @Column(name = "mercado")
    final String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_linha")
    final TipoMercado tipo;

    public Mercado(String nome, TipoMercado tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

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
