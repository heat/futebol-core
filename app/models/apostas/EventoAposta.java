package models.apostas;

import models.bilhetes.Palpite;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table( name = "evento_apostas")
public class EventoAposta implements Serializable{


    @Id
    @SequenceGenerator(name="eventos_apostas_id_seq", sequenceName = "eventos_apostas_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventos_apostas_id_seq")
    @Column(name = "evento_aposta_id",updatable = false)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenant;

    @Column(name = "permitir")
    private boolean permitir;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "evento_apostas_id")
    private List<Taxa> taxas;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "evento_apostas_id")
    private List<Palpite> palpites;

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
}
