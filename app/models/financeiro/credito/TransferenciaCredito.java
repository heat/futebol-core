package models.financeiro.credito;

import models.financeiro.DocumentoTransferencia;
import models.financeiro.Lancamento;
import models.financeiro.TipoLancamento;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Uma transferencia de credito é o lançamento de retirada de uma conta para outra conta
 */
@Entity
public class TransferenciaCredito extends Lancamento<DocumentoTransferencia> implements Credito {

    @OneToOne
    @JoinColumn(name = "origem_transferencia_credito")
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
