package models.apostas;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import models.eventos.Resultado;
import models.eventos.ResultadoEvento;
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
    @Column(name = "taxa_id")
    private Long id;

    @Column(name = "tenant_id")
    private Long tenant;

    @ManyToOne
    @JoinColumn(name = "odd_id")
    private Odd odd;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="tenant_id", referencedColumnName = "tenant_id", updatable = false, insertable = false),
            @JoinColumn(name = "odd_id", referencedColumnName = "odd_id", updatable = false, insertable = false)
    })
    private OddConfiguracao oddConfiguracao;

    @Column(name = "taxa")
    private BigDecimal taxa;

    @Column(name = "evento_aposta_id", insertable = false, updatable = false)
    private Long eventoAposta;

    @Column(name = "linha")
    private BigDecimal linha;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "criado_em")
    private Calendar criadoEm = Calendar.getInstance();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "alterado_em")
    private Calendar alteradoEm = Calendar.getInstance();

    private Boolean visivel = true;

    protected Taxa() {

    }

    public Taxa(Long tenant, Odd odd, BigDecimal taxa, BigDecimal linha) {

        this.tenant = tenant;
        this.odd = odd;
        this.taxa = taxa;
        this.linha = linha;
        this.criadoEm = Calendar.getInstance();
        this.alteradoEm = Calendar.getInstance();
    }

    public Taxa(Long id, BigDecimal taxa, Long eventoAposta, BigDecimal linha, Boolean visivel) {
        this.id = id;
        this.taxa = taxa;
        this.eventoAposta = eventoAposta;
        this.linha = linha;
        this.visivel = visivel;
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

    public Long getEventoAposta() {
        return eventoAposta;
    }

    public void setEventoAposta(Long eventoAposta) {
        this.eventoAposta = eventoAposta;
    }

    public OddConfiguracao getOddConfiguracao() {
        return oddConfiguracao;
    }

    public Boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(Boolean visivel) {
        this.visivel = visivel;
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

    public boolean isFavorita() {
        return oddConfiguracao.getFavorita();
    }

    public <V extends ResultadoEvento> boolean calcular(V resultado) {
        Calculadora calc = getOdd().getCalculadora(this);

        boolean b = calc.calcular(resultado);
        return b;
    }
}
