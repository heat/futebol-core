package models.financeiro.debito;

import models.bilhetes.Bilhete;
import models.financeiro.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class AdicionarSaldoDebito extends Lancamento<SolicitacaoFinanceira> implements Debito {

    @OneToOne
    @JoinColumn(name = "origem_solicitacao_financeira")
    public SolicitacaoFinanceira origem;

    @Override
    public SolicitacaoFinanceira getOrigem() {
        return origem;
    }

    @Override
    public TipoLancamento getTipo() {
        return this;
    }

    public void setOrigem(SolicitacaoFinanceira origem) {
        this.origem = origem;
    }
}
