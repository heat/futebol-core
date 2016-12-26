package models.seguranca;


import javax.persistence.*;

@Entity
@Table(name = "permissoes")
public class Permissao {

    @Id
    @Column(name = "permissao_id",updatable = false)
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
