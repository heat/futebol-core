package models.financeiro.debito;

import models.bilhetes.Bilhete;
import models.financeiro.Lancamento;

/**
 * O pagamento de comissao adiciona um saldo a conta do usuairo que normalmente vende um bilhete
 */
public class PagamentoComissaoDebito extends Lancamento<Bilhete> implements Debito {

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
