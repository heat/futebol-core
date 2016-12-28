package models.eventos;

import models.vo.Tenant;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "times")
public class Time implements Serializable {

    @Id
    @SequenceGenerator(name="times_time_id_seq", sequenceName = "times_time_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "times_time_id_seq")
    @Column(name = "time_id",updatable = false)
    Long id;

    @Column(name = "tenant_id")
    Long tenant;

    @Column
    String nome;

    protected Time() {
    }

    public Time(Tenant tenant, String nome) {
        this.tenant = tenant.get();
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Time time = (Time) o;

        if (id != null ? !id.equals(time.id) : time.id != null) return false;
        if (tenant != null ? !tenant.equals(time.tenant) : time.tenant != null) return false;
        return nome != null ? nome.equals(time.nome) : time.nome == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        return result;
    }
}
