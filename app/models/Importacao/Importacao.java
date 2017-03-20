package models.Importacao;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "importacaos")
public class Importacao implements Serializable {

    public enum Situacao {

        ATUALIZADO("ATUALIZADO"), CRIADO("CRIADO"), FALHA("FALHA");

        private String situacao;

        Situacao(String situacao) {

            this.situacao = situacao;
        }
    }

    @Id
    @SequenceGenerator(name="importacao_id_seq", sequenceName = "importacao_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "importacao_id_seq")
    @Column(name = "importacao_id",updatable = false)
    private Long id;

    private Long tenant;

    private String chave;

    @Column(name = "criado_em")
    private Calendar criadoEm;

    private Situacao situacao;

    private BigDecimal variacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public Calendar getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Calendar criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getVariacao() {
        return variacao;
    }

    public void setVariacao(BigDecimal variacao) {
        this.variacao = variacao;
    }

    public Long getTenant() {
        return tenant;
    }

    public void setTenant(Long tenant) {
        this.tenant = tenant;
    }
}
