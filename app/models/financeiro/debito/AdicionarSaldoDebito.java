package models.financeiro.debito;

import models.bilhetes.Bilhete;
import models.financeiro.Conta;
import models.financeiro.Lancamento;
import models.financeiro.SolicitacaoSaldo;
import models.financeiro.TipoLancamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class AdicionarSaldoDebito extends Lancamento<SolicitacaoSaldo> implements Debito {

    @OneToOne
    @JoinColumn(name = "origem_solicitacao_saldo")
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
