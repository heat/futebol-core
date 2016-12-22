package models.eventos;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "resultados")
public class Resultado implements Serializable{

    @Id
    @SequenceGenerator(name="resultados_resultado_id_seq", sequenceName = "resultados_resultado_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resultados_resultado_id_seq")
    @Column(name = "resultado_id",updatable = false)
    private Long id;

    @Column(name="tenant_id")
    private Long tenant;

    @Column(name="momento")
    private String momento;

    @Column(name="pontos")
    private Long pontos;

    @OneToOne
    @JoinColumn(name = "times_time_id", foreignKey = @ForeignKey(name = "fk_times_time_id"))
    private Time time;

    @ManyToOne
    @JoinColumn(name="resultados_evento_id")
    private Evento evento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMomento() {
        return momento;
    }

    public void setMomento(String momento) {
        this.momento = momento;
    }

    public Long getPontos() {
        return pontos;
    }

    public void setPontos(Long pontos) {
        this.pontos = pontos;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resultado resultado = (Resultado) o;

        if (id != null ? !id.equals(resultado.id) : resultado.id != null) return false;
        if (tenant != null ? !tenant.equals(resultado.tenant) : resultado.tenant != null) return false;
        if (momento != null ? !momento.equals(resultado.momento) : resultado.momento != null) return false;
        if (pontos != null ? !pontos.equals(resultado.pontos) : resultado.pontos != null) return false;
        return time != null ? time.equals(resultado.time) : resultado.time == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        result = 31 * result + (momento != null ? momento.hashCode() : 0);
        result = 31 * result + (pontos != null ? pontos.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
