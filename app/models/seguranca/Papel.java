package models.seguranca;


import models.seguranca.Permissao;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="papeis")
public class Papel {

    @Id
    @SequenceGenerator(name="papeis_idpapel_seq", sequenceName = "papeis_idpapel_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "papeis_idpapel_seq")
    @Column(name = "idpapel",updatable = false)
    private Long id;

    @Column
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="papel_has_permissoes",
            joinColumns= {@JoinColumn(name="idpapel", foreignKey = @ForeignKey(name = "fk_idpapel"))},
            inverseJoinColumns={@JoinColumn(name="idpermissao", foreignKey = @ForeignKey(name = "fk_idpermissao"))})
    private List<Permissao> permissoes;


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

}
