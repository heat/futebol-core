package models.eventos;

import javax.persistence.*;

@Entity
@Table(name = "campeonatos")
public class Campeonato {

    @Id
    @SequenceGenerator(name="campeonatos_idcampeonato_seq", sequenceName = "campeonatos_idcampeonato_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campeonatos_idcampeonato_seq")
    @Column(name = "idcampeonato",updatable = false)
    private Long id;

    @Column(name="tenant_id")
    private Long tenant;

    @Column
    private String nome;

    protected Campeonato() {
    }

    public Campeonato(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
