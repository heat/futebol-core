package models.bilhetes;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "palpites")
public class Palpite implements Serializable{

    @EmbeddedId
    private PalpitePK id;

    @Column(name = "tenant_id")
    private Long tenant;

    @Column(name = "taxa")
    private BigDecimal taxa;

    @Column(name = "status")
    private char status;

    public PalpitePK getId() {
        return id;
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
