package models.financeiro.credito;

import models.financeiro.Lancamento;
import models.financeiro.SolicitacaoFinanceira;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class FechamentoCredito extends Lancamento<SolicitacaoFinanceira> implements Credito {

    //TODO: Criar tabela de solicitacao, e criar subtipos (solicitacao de credito, fechamento, etc...)
    @OneToOne
    @JoinColumn(name = "origem_solicitacao_financeira")
    public SolicitacaoFinanceira origemSolicitacaoFinanceira;

    @Override
    public SolicitacaoFinanceira getOrigem() {
        return this.origemSolicitacaoFinanceira;
    }

    @Override
    public Credito getTipo() {
        return this;
    }

    public void setOrigem(SolicitacaoFinanceira origemSolicitacaoFinanceira) {
        this.origemSolicitacaoFinanceira = origemSolicitacaoFinanceira;
    }
}
