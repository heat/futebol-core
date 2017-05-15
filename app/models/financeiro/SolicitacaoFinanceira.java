package models.financeiro;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "solicitacao_financeira")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class SolicitacaoFinanceira {

    @Id
    @SequenceGenerator(name="solicitacao_financeira_id_seq", sequenceName = "solicitacao_financeira_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitacao_financeira_id_seq")
    @Column(name = "solicitacao_financeira_id",updatable = false)
    private Long id;

    @Column(name = "usuario_id")
    private Long solicitante;

    private BigDecimal valor;

    @Column(name = "criado_em")
    private Calendar criadoEm = Calendar.getInstance();

    @OneToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    public SolicitacaoFinanceira() {
    }

    public SolicitacaoFinanceira(Long solicitante, BigDecimal valor) {
        this.solicitante = solicitante;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public Long getSolicitante() {
        return solicitante;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Calendar getCriadoEm() {
        return criadoEm;
    }

}
