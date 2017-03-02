package models.financeiro.credito;

import models.financeiro.DocumentoTransferencia;
import models.financeiro.Lancamento;
import models.financeiro.TipoLancamento;

/**
 * Uma transferencia de credito é o lançamento de retirada de uma conta para outra conta
 */
public class TransferenciaCredito extends Lancamento<DocumentoTransferencia> implements Credito {

    private DocumentoTransferencia docOrigem;

    @Override
    public DocumentoTransferencia getOrigem() {
        return docOrigem;
    }

    @Override
    public TipoLancamento getTipo() {
        return this;
    }
}
