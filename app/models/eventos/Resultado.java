package models.eventos;

import javax.persistence.*;
import java.io.Serializable;

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
    @JoinColumn(name = "time_id")
    private Time time;

    public Long getId() {
        return id;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resultado resultado = (Resultado) o;

        if (id != null ? !id.equals(resultado.id) : resultado.id != null) return false;
        return tenant != null ? tenant.equals(resultado.tenant) : resultado.tenant == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        return result;
    }
}
