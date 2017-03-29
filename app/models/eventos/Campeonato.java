package models.eventos;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "campeonatos")
public class Campeonato implements Serializable{

    public static Campeonato of(String nome) {
        return new Campeonato(nome);
    }

    public static Campeonato ref(Long id) {
        return new Campeonato(id);
    }

    public enum Situacao {
        A("ATIVO"), I("INATIVO");

        private String situacao;

        Situacao(String situacao) {
            this.situacao = situacao;
        }
    }

    @Id
    @SequenceGenerator(name="campeonatos_campeonato_id_seq", sequenceName = "campeonatos_campeonato_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campeonatos_campeonato_id_seq")
    @Column(name = "campeonato_id",updatable = false)
    private Long id;

    @Column(name="tenant_id")
    private Long tenant;

    @Column(name = "nome")
    private String nome;

    @Enumerated(EnumType.STRING )
    @Column(name = "situacao")
    private Situacao situacao = Situacao.A;

    protected Campeonato() {
    }

    private Campeonato(Long id) {
        this.id = id;
    }

    private Campeonato(String nome) {
        this.nome = nome;
    }

    public Campeonato(Long tenant, String nome) {
        this.nome = nome;
        this.tenant = tenant;
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

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campeonato that = (Campeonato) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return tenant != null ? tenant.equals(that.tenant) : that.tenant == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        return result;
    }
}
