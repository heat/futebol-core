package models.financeiro.debito;


import models.financeiro.DocumentoTransferencia;
import models.financeiro.Lancamento;
import models.financeiro.TipoLancamento;

/**
 * é a conta destinataria de um documento de transferencia
 */
public class TransferenciaDebito extends Lancamento<DocumentoTransferencia> implements Debito {

    DocumentoTransferencia docOrigem;

    @Override
    public DocumentoTransferencia getOrigem() {
        return this.docOrigem;
    }

    @Override
    public TipoLancamento getTipo() {
        return this;
    }
}
