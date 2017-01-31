package models.apostas;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import models.serializacoes.CalendarDeserializer;
import models.serializacoes.CalendarSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table( name = "taxas")
public class Taxa implements Serializable{

    @Id
    @SequenceGenerator(name="taxas_taxa_id_seq", sequenceName = "taxas_taxa_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taxas_taxa_id_seq")
    @Column(name = "taxa_id",updatable = false)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenant;

    @OneToOne
    @JoinColumn(name = "odd_id")
    private Odd odd;

    @JoinColumn(name = "evento_aposta_id")
    private Long aposta;

    @Column(name = "taxa")
    private BigDecimal taxa;

    @Column(name = "linha")
    private BigDecimal linha;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em")
    private Calendar criadoEm;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "alterado_em")
    private Calendar alteradoEm;

    protected Taxa() {

    }

    public Taxa(Long tenant, Odd odd, BigDecimal taxa, BigDecimal linha, Long aposta) {

        this.tenant = tenant;
        this.odd = odd;
        this.taxa = taxa;
        this.linha = linha;
        this.aposta = aposta;
        this.criadoEm = Calendar.getInstance();
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

    public Odd getOdd() {
        return odd;
    }

    public void setOdd(Odd odd) {
        this.odd = odd;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }

    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    public BigDecimal getLinha() {
        return linha;
    }

    public void setLinha(BigDecimal linha) {
        this.linha = linha;
    }

    @JsonDeserialize(using= CalendarDeserializer.class)
    public Calendar getCriadoEm() {
        return criadoEm;
    }

    @JsonSerialize(using= CalendarSerializer.class)
    public void setCriadoEm(Calendar criadoEm) {
        this.criadoEm = criadoEm;
    }

    @JsonDeserialize(using= CalendarDeserializer.class)
    public Calendar getAlteradoEm() {
        return alteradoEm;
    }

    @JsonSerialize(using= CalendarSerializer.class)
    public void setAlteradoEm(Calendar alteradoEm) {
        this.alteradoEm = alteradoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Taxa taxa = (Taxa) o;

        if (id != null ? !id.equals(taxa.id) : taxa.id != null) return false;
        return tenant != null ? tenant.equals(taxa.tenant) : taxa.tenant == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tenant != null ? tenant.hashCode() : 0);
        return result;
    }
}
