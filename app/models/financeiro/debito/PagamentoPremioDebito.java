package models.financeiro.debito;

import models.bilhetes.Bilhete;
import models.financeiro.Lancamento;


/**
 * Pagamento de premio registra o pagamento de um premigo apos o bilhete ficar premiado
 */
public class PagamentoPremioDebito extends Lancamento<Bilhete> implements Debito {

    public Bilhete origemBilhete;

    @Override
    public Bilhete getOrigem() {
        return this.origemBilhete;
    }

    @Override
    public Debito getTipo() {
        return this;
    }
}
