package models.seguranca;


import javax.persistence.*;

@Entity
@Table(name = "permissoes")
public class Permissao {

    @Id
    @SequenceGenerator(name="permissoes_idpermissao_seq", sequenceName = "permissoes_idpermissao_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissoes_idpermissao_seq")
    @Column(name = "idpermissao",updatable = false)
    private Long id;

    @Column(name="nome")
    private String nome;

    @Column(name="descricao")
    private String descricao;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
