package models.financeiro;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "transferencia_saldo")
public class DocumentoTransferencia {

    @Id
    @SequenceGenerator(name="transferencia_saldo_id_seq", sequenceName = "transferencia_saldo_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transferencia_saldo_id_seq")
    @Column(name = "transferencia_saldo_id",updatable = false)
    private Long id;

    @Column(name = "criado_em")
    private Calendar criadoEm = Calendar.getInstance();

    @Column(name = "conta_origem")
    private Long origem;

    @Column(name = "conta_destino")
    private Long destino;

    private BigDecimal valor;

    public DocumentoTransferencia() {
    }

    public DocumentoTransferencia(Long origem, Long destino, BigDecimal valor) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public Calendar getCriadoEm() {
        return criadoEm;
    }

    public Long getOrigem() {
        return origem;
    }

    public Long getDestino() {
        return destino;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
