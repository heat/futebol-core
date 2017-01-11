package models.bilhetes;

import models.apostas.EventoAposta;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "palpites")
@IdClass(PalpitePK.class)
public class Palpite implements Serializable{


    @Id
    @OneToOne
    @JoinColumn(name = "bilhete_id")
    private Bilhete bilhete;

    @Id
    @OneToOne
    @JoinColumn(name = "evento_aposta_id")
    private EventoAposta eventoAposta;

    @Column(name = "tenant_id")
    private Long tenant;

    @Column(name = "taxa")
    private BigDecimal taxa;

    @Column(name = "status")
    private char status;

    public Palpite() {

    }

    public Palpite(Long tenant, BigDecimal taxa, char status) {

        this.tenant = tenant;
        this.taxa = taxa;
        this.status = status;
    }

    public Bilhete getBilhete() {
        return bilhete;
    }

    public void setBilhete(Bilhete bilhete) {
        this.bilhete = bilhete;
    }

    public EventoAposta getEventoAposta() {
        return eventoAposta;
    }

    public void setEventoAposta(EventoAposta eventoAposta) {
        this.eventoAposta = eventoAposta;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Palpite palpite = (Palpite) o;

        if (bilhete != null ? !bilhete.equals(palpite.bilhete) : palpite.bilhete != null) return false;
        if (eventoAposta != null ? !eventoAposta.equals(palpite.eventoAposta) : palpite.eventoAposta != null)
            return false;
        return tenant != null ? tenant.equals(palpite.tenant) : palpite.tenant == null;
    }

    @Override
    public int hashCode() {
        int result = bilhete != null ? bilhete.hashCode() : 0;
        result = 31 * result + (eventoAposta != null ? eventoAposta.hashCode() : 0);
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        return result;
    }
}
