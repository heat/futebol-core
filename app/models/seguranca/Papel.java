package models.seguranca;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="papeis")
public class Papel implements Serializable{

    @Id
    @Column(name = "papel_id",updatable = false)
    private Long id;

    @Column
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="papeis_has_permissoes",
            joinColumns= {@JoinColumn(name="papel_id")},
            inverseJoinColumns={@JoinColumn(name="permissao_id")})
    private List<Permissao> permissoes;

    public Papel() {

    }

    public Papel(Long id, String nome, List<Permissao> permissoes) {

        this.id = id;
        this.nome = nome;
        this.permissoes = permissoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Papel papel = (Papel) o;

        return id != null ? id.equals(papel.id) : papel.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
