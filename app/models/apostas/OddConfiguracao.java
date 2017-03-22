package models.apostas;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * A configuracao representa o Paramatro da banca para cada Odd. É necessário que a banca tenha uma configuracao para
 * cada odd
 */
@Entity
@Table(name = "configuracao_odd")
public class OddConfiguracao {

    enum Situacao {
        A("ATIVA");

        private final String situacao;

        Situacao(String situacao) {
            this.situacao = situacao;
        }
    }

    @Id
    @SequenceGenerator(name="configuracao_odd_id_seq", sequenceName = "configuracao_odd_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "configuracao_odd_id_seq")
    @Column(name = "configuracao_odd_id",updatable = false)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenant;

    @ManyToOne
    @JoinColumn(name = "odd_id")
    private Odd odd;

    private Boolean favorita;

    private Situacao situacao = Situacao.A;

    /**
     * Odd do tipo linha deve ter específicado qual a linha favorita
     */
    @Column(name = "linha")
    private BigDecimal linhaFavorita;

    private Long prioridade;

    public OddConfiguracao() {
    }

    public OddConfiguracao(Long tenant, Odd odd, Boolean favorita, BigDecimal linhaFavorita, Long prioridade) {
        this.tenant = tenant;
        this.odd = odd;
        this.favorita = favorita;
        this.linhaFavorita = linhaFavorita;
        this.prioridade = prioridade;
    }

    public Long getIdOdd() {
        return this.odd.getId();
    }

    public Odd get() {
        return this.odd;
    }

    public Long getId() {
        return id;
    }

    public Long getTenant() {
        return tenant;
    }

    public Odd getOdd() {
        return odd;
    }

    public Boolean getFavorita() {
        return favorita;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public BigDecimal getLinhaFavorita() {
        return linhaFavorita;
    }

    public Long getPrioridade() {
        return prioridade;
    }
}
