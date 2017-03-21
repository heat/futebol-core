package models.apostas;

import models.apostas.mercado.Mercado;
import models.apostas.odd.Posicionavel;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table( name = "odds")
//TODO fazer com herança
public abstract class Odd<P> implements Serializable, Posicionavel<P> {

    @Id
    @SequenceGenerator(name="odds_odd_id_seq", sequenceName = "odds_odd_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "odds_odd_id_seq")
    @Column(name = "odd_id",updatable = false)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "mercado")
    //TODO Fazer embedded de mercado
    private Mercado mercado;

    //TODO refatorar para embedded no mercado renomear pra tipo somente
    // @Column(name = "tipo_linha")
    // private char tipoLinha;

    @Column(name = "abreviacao")
    private String abreviacao;

    @Column(name = "descricao")
    private String descricao;


    protected Odd() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Mercado getMercado() {
        return mercado;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Odd odd = (Odd) o;

        if (id != null ? !id.equals(odd.id) : odd.id != null) return false;
        if (nome != null ? !nome.equals(odd.nome) : odd.nome != null) return false;
        if (mercado != null ? !mercado.equals(odd.mercado) : odd.mercado != null) return false;
        if (abreviacao != null ? !abreviacao.equals(odd.abreviacao) : odd.abreviacao != null) return false;
        return descricao != null ? descricao.equals(odd.descricao) : odd.descricao == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (mercado != null ? mercado.hashCode() : 0);
        result = 31 * result + (abreviacao != null ? abreviacao.hashCode() : 0);
        result = 31 * result + (descricao != null ? descricao.hashCode() : 0);
        return result;
    }
}
