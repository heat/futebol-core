package models.financeiro.debito;

import models.bilhetes.Bilhete;
import models.financeiro.Conta;
import models.financeiro.Lancamento;
import models.financeiro.SolicitacaoSaldo;
import models.financeiro.TipoLancamento;

import javax.persistence.Column;

public class AdicionarSaldoDebito extends Lancamento<SolicitacaoSaldo> implements Debito {

    @Column(name = "solicitacao_saldo_id")
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
