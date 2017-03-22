package models.eventos;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "times")
public class Time implements Serializable {

    public enum Situacao {

        A("ATIVO"), I("INATIVO");

        private String situacao;

        Situacao(String situacao) {

            this.situacao = situacao;
        }
    }

    @Id
    @SequenceGenerator(name="times_time_id_seq", sequenceName = "times_time_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "times_time_id_seq")
    @Column(name = "time_id",updatable = false)
    Long id;

    @Column(name = "tenant_id")
    Long tenant;

    @Column
    String nome;

    @Enumerated(EnumType.STRING)
    private Situacao situacao = Situacao.A;

    protected Time() {

    }

    public Time(Long idTime) {
        this.id = idTime;
    }


    public Time(Long tenant, String nome) {
        this.tenant = tenant;
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

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Time time = (Time) o;

        if (id != null ? !id.equals(time.id) : time.id != null) return false;
        return tenant != null ? tenant.equals(time.tenant) : time.tenant == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        return result;
    }

    public static Time ref(Long time) {
        return new Time(time);
    }
}
