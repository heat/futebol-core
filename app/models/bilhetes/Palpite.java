package models.bilhetes;

import models.apostas.Taxa;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "palpites")
public class Palpite implements Serializable{

    public enum Status {

        /**
         * Situacao em que o palpite ainda não foi atualizado após o fim da partida
         */
        A("ABERTO"),
        /**
         *O palpite está correto
         */
        V("CERTO"),
        /**
         *O palpite está errado
         */
        E("ERRADO"),
        /**
         *Desistiram do palpite antes do início (?) da partida
         */
        C("CANCELADO");

        private String status;

        Status(String status) {

            this.status = status;
        }
    }

    @Id
    @SequenceGenerator(name="palpites_palpite_id_seq", sequenceName = "palpites_palpite_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "palpites_palpite_id_seq")
    @Column(name = "palpite_id",updatable = false)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenant;

    @OneToOne
    @JoinColumn(name = "taxa_id")
    private Taxa taxa;

    @Column(name = "taxa")
    private BigDecimal valorTaxa;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;


    public Palpite() {
    }

    public Palpite(Long tenant, Taxa taxa, BigDecimal valorTaxa, Status status) {
        this.tenant = tenant;
        this.taxa = taxa;
        this.valorTaxa = valorTaxa;
        this.status = status;
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

    public Taxa getTaxa() {
        return taxa;
    }

    public void setTaxa(Taxa taxa) {
        this.taxa = taxa;
    }

    public BigDecimal getValorTaxa() {
        return valorTaxa;
    }

    public void setValorTaxa(BigDecimal valorTaxa) {
        this.valorTaxa = valorTaxa;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Palpite palpite = (Palpite) o;

        if (id != null ? !id.equals(palpite.id) : palpite.id != null) return false;
        return tenant != null ? tenant.equals(palpite.tenant) : palpite.tenant == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        return result;
    }
}
