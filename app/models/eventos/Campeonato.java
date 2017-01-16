package models.eventos;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "campeonatos")
public class Campeonato implements Serializable{

    @Id
    @SequenceGenerator(name="campeonatos_campeonato_id_seq", sequenceName = "campeonatos_campeonato_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "campeonatos_campeonato_id_seq")
    @Column(name = "campeonato_id",updatable = false)
    private Long id;

    @Column(name="tenant_id")
    private Long tenant;

    @Column
    private String nome;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="campeonato_id", nullable = false, updatable = true, insertable = true)
    private List<Evento> eventos;

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

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public void addEvento (Evento evento){
        this.eventos.add(evento);
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
