package models.eventos;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "resultados")
public class Resultado implements Serializable{

    public enum Momento {
        /**
         * Situacao em que o apostavel aceita apostas
         */
        I("INTERVALO"),
        /**
         * Apostavel é valido porém temporariamente não aceita apostas
         */
        F("FINAL");

        private String momento;

        Momento(String momento) {

            this.momento = momento;
        }
    }

    @Id
    @SequenceGenerator(name="resultados_resultado_id_seq", sequenceName = "resultados_resultado_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resultados_resultado_id_seq")
    @Column(name = "resultado_id",updatable = false)
    private Long id;

    @Column(name="tenant_id")
    private Long tenant;

    @Enumerated(EnumType.STRING)
    @Column(name="momento")
    private Momento momento;

    @Column(name="pontos")
    private Long pontos;

    @OneToOne
    @JoinColumn(name = "time_id")
    private Time time;

    public Resultado() {

    }

    public Resultado(Long tenant, Momento momento, Long pontos, Time time) {

        this.tenant = tenant;
        this.momento = momento;
        this.pontos = pontos;
        this.time = time;
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

    public Momento getMomento() {
        return momento;
    }

    public void setMomento(Momento momento) {
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

    public boolean isMomentoFinal() {
        return false;
    }
}
