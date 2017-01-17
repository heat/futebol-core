package models.apostas;

import models.eventos.Evento;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table( name = "evento_apostas")
// TODO: colocar stauts em um evento aposta
public class EventoAposta implements Apostavel<Evento>, Serializable{

    public enum Situacao {
        /**
         * Situacao em que o apostavel aceita apostas
         */
        ABERTO("ABERTO"),
        /**
         * Apostavel é valido porém temporariamente não aceita apostas
         */
        FECHADO("FECHADO"),
        /**
         * Já encerrado o apostavél e não é mais possível realizar apostas
         */
        FINALIZADO("FINALIZADO"),
        /**
         * Apostável foi cancelado pela adminsitracao
         */
        CANCELADO("CANCELADO");

        private String string;

        Situacao(String string) {
            this.string = string;
        }
    }

    @Id
    @SequenceGenerator(name="eventos_apostas_id_seq", sequenceName = "eventos_apostas_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventos_apostas_id_seq")
    @Column(name = "evento_aposta_id",updatable = false)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenant;

    @OneToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @Column(name = "permitir")
    private boolean permitir;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao")
    private Situacao situacao;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "evento_aposta_id", nullable = false, updatable = true, insertable = true)
    private List<Taxa> taxas;

    public EventoAposta() {

    }

    public EventoAposta(Long tenant, boolean permitir, List<Taxa> taxas) {

        this.tenant = tenant;
        this.permitir = permitir;
        this.taxas = taxas;
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

    public boolean isPermitir() {
        return permitir;
    }

    public void setPermitir(boolean permitir) {
        this.permitir = permitir;
    }

    public List<Taxa> getTaxas() {
        return taxas;
    }

    public void setTaxas(List<Taxa> taxas) {
        this.taxas = taxas;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
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

        EventoAposta that = (EventoAposta) o;

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
