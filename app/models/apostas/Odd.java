package models.apostas;

import models.apostas.mercado.Mercado;
import models.apostas.odd.Posicionavel;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "odds")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Odd<P> implements Serializable, Posicionavel<P> {

    private static final long serialVersionUID = -5179541857358724150L;

    @Id
    @SequenceGenerator(name="odds_odd_id_seq", sequenceName = "odds_odd_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "odds_odd_id_seq")
    @Column(name = "odd_id",updatable = false)
    protected Long id;

    /**
     * Codigo referencia da odd. Normalmente nome sem espaços e caracters acentuados
     * Resultado Final Casa = resultado-final.casa
     */
    private String codigo;

    protected Odd() {
    }

    public Odd(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public Long getId() {
        return id;
    }

    public abstract String getNome();

    public abstract Mercado getMercado();

    public abstract String getAbreviacao();

    public abstract String getDescricao();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Odd<?> odd = (Odd<?>) o;

        if (id != null ? !id.equals(odd.id) : odd.id != null) return false;
        return codigo != null ? codigo.equals(odd.codigo) : odd.codigo == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (codigo != null ? codigo.hashCode() : 0);
        return result;
    }

    public Calculadora getCalculadora(Taxa taxa) {
        //valor default para não de erro de compilação
        return  resultado -> false;
    };

}
