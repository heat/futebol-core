package models.financeiro;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
@Table(name = "solicitacao_saldo")
public class SolicitacaoSaldo {

    public enum TipoSolicitacaoSaldo {
        D("DEPOSITO"),
        E("EMPRESTIMO"),
        N("NETELLER"),
        P("PAYPAL");

        private String situacao;

        TipoSolicitacaoSaldo(String situacao) {
            this.situacao = situacao;
        }
    }

    @Id
    @SequenceGenerator(name="solicitacao_saldo_id_seq", sequenceName = "solicitacao_saldo_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitacao_saldo_id_seq")
    @Column(name = "solicitacao_saldo_id",updatable = false)
    private Long id;

    @Column(name = "conta_id")
    private Long solicitante;

    private BigDecimal valor;

    @Column(name = "criado_em")
    private Calendar criadoEm = Calendar.getInstance();

    @Enumerated(EnumType.STRING)
    private TipoSolicitacaoSaldo tipo;

    public SolicitacaoSaldo() {
    }

    public SolicitacaoSaldo(Long solicitante, BigDecimal valor, TipoSolicitacaoSaldo tipo) {
        this.solicitante = solicitante;
        this.valor = valor;
        this.tipo = tipo;
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

    public TipoSolicitacaoSaldo getTipo() {
        return tipo;
    }
}
