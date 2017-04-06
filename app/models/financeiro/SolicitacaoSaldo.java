package models.financeiro;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "solicitacao_saldo")
public class SolicitacaoSaldo {

    @Id
    @SequenceGenerator(name="solicitacao_saldo_id_seq", sequenceName = "solicitacao_saldo_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitacao_saldo_id_seq")
    @Column(name = "solicitacao_saldo_id",updatable = false)
    private Long id;

    @Column(name = "conta_id")
    private Long solicitante;

    private BigDecimal valor;

    @Column(name = "criado_em")
    private Calendar criadoEm;

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
