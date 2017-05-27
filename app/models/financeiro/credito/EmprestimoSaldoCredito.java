package models.financeiro.credito;

import models.financeiro.Lancamento;
import models.financeiro.SolicitacaoFinanceira;
import models.financeiro.SolicitacaoSaldo;
import models.financeiro.TipoLancamento;
import models.financeiro.debito.Debito;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class EmprestimoSaldoCredito extends Lancamento<SolicitacaoFinanceira> implements Debito {

    @OneToOne
    @JoinColumn(name = "origem_solicitacao_financeira")
    public SolicitacaoSaldo origem;

    @Override
    public SolicitacaoSaldo getOrigem() {
        return origem;
    }

    @Override
    public TipoLancamento getTipo() {
        return this;
    }

    public void setOrigem(SolicitacaoSaldo origem) {
        this.origem = origem;
    }
}
