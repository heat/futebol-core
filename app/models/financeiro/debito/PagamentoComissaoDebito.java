package models.financeiro.debito;

import models.bilhetes.Bilhete;
import models.financeiro.Lancamento;
import models.financeiro.comissao.Comissao;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * O pagamento de comissao adiciona um saldo a conta do usuairo que normalmente vende um bilhete
 */
@Entity
public class PagamentoComissaoDebito extends Lancamento<Comissao> implements Debito {

    @OneToOne
    @JoinColumn(name = "origem_comissao")
    public Comissao origemComissao;

    public void setOrigemComissao(Comissao origemComissao) {
        this.origemComissao = origemComissao;
    }

    @Override
    public Comissao getOrigem() {
        return this.origemComissao;
    }

    @Override
    public Debito getTipo() {
        return this;
    }
}
