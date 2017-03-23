package models.bilhetes;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Table( name = "pins")
public class Pin {

    @Id
    @SequenceGenerator(name="pins_pin_id_seq", sequenceName = "pins_pin_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pins_pin_id_seq")
    @Column(name = "pin_id",updatable = false)
    private Long id;

    private String cliente;

    @Column(name = "valor_aposta")
    private BigDecimal valorAposta;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em")
    private Calendar criadoEm = Calendar.getInstance();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expira_em")
    private Calendar expiraEm = Calendar.getInstance();

    @Column(name = "tenant_id")
    private Long tenant;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "pin_id", nullable = false, updatable = false, insertable = false)
    private List<PalpitePin> palpitesPin = new ArrayList<>();

    public Pin() {
    }

    public Pin(String cliente, BigDecimal valorAposta) {
        this.cliente = cliente;
        this.valorAposta = valorAposta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getValorAposta() {
        return valorAposta;
    }

    public void setValorAposta(BigDecimal valorAposta) {
        this.valorAposta = valorAposta;
    }

    public Calendar getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Calendar criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<PalpitePin> getPalpitesPin() {
        return palpitesPin;
    }

    public void setPalpitesPin(List<PalpitePin> palpitesPin) {
        this.palpitesPin = palpitesPin;
    }

    public Calendar getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(Calendar expiraEm) {
        this.expiraEm = expiraEm;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }

    public void addPalpitesPin(PalpitePin palpitePin){
        this.palpitesPin.add(palpitePin);
    }
}
