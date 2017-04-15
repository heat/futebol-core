package models.bilhetes;

import models.apostas.Taxa;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "palpites_pin")
public class PalpitePin implements Serializable{

    @Id
    @SequenceGenerator(name="palpites_pin_id_seq", sequenceName = "palpites_pin_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "palpites_pin_id_seq")
    @Column(name = "palpite_pin_id",updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "taxa_id")
    private Taxa taxa;

    public PalpitePin() {
    }

    public PalpitePin(Taxa taxa) {
        this.taxa = taxa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Taxa getTaxa() {
        return taxa;
    }

    public void setTaxa(Taxa taxa) {
        this.taxa = taxa;
    }
}
