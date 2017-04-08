package models.financeiro.debito;

import models.financeiro.DocumentoTransferencia;
import models.financeiro.Lancamento;
import models.financeiro.TipoLancamento;
import models.financeiro.credito.Credito;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * é a conta destinataria de um documento de transferencia
 */
@Entity
public class TransferenciaDebito extends Lancamento<DocumentoTransferencia> implements Debito {

    @OneToOne
    @JoinColumn(name = "origem_transferencia_debito")
    private DocumentoTransferencia origem;

    @Override
    public DocumentoTransferencia getOrigem() {
        return origem;
    }

    @Override
    public TipoLancamento getTipo() {
        return this;
    }

    public void setOrigem(DocumentoTransferencia origem) {
        this.origem = origem;
    }
}
