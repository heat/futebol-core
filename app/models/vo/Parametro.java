package models.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Parametro onde um valor 0 é considerando um parametro vazio.
 */
@Embeddable
public class Parametro implements Comparable<Parametro> {

    public static Parametro EMPTY = new Parametro(0L);

    @Column(name = "parametro_valor")
    private Long parametroValor;

    public Parametro() {
    }

    public Parametro(Long parametroValor) {
        this.parametroValor = parametroValor;
    }

    public Long valor() {
        return this.parametroValor;
    }

    public static Parametro of(int size) {
        return new Parametro(Long.valueOf(size));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parametro parametro = (Parametro) o;

        return parametroValor != null ? parametroValor.equals(parametro.parametroValor) : parametro.parametroValor == null;

    }

    @Override
    public int hashCode() {
        return parametroValor != null ? parametroValor.hashCode() : 0;
    }

    @Override
    public int compareTo(Parametro o) {
        return this.valor().compareTo(o.valor());
    }
}
