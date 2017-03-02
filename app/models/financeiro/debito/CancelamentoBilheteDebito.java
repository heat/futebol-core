package models.financeiro.debito;

import models.bilhetes.Bilhete;
import models.financeiro.Lancamento;
import models.financeiro.TipoLancamento;

public class CancelamentoBilheteDebito extends Lancamento<Bilhete> implements Debito {

    private Bilhete origemBilhete;

    @Override
    public Bilhete getOrigem() {
        return this.origemBilhete;
    }

    @Override
    public TipoLancamento getTipo() {
        return this;
    }
}
