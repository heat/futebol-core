package models.financeiro;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

@Entity
public class SolicitacaoSaldo extends SolicitacaoFinanceira {

    public SolicitacaoSaldo(Long idConta, BigDecimal valor, TipoSolicitacaoSaldo tipo) {
    }

    public TipoSolicitacaoSaldo getTipo() {
        return null;
    }

    public enum TipoSolicitacaoSaldo {
        D("DEPOSITO"),
        E("EMPRESTIMO"),
        N("NETELLER"),
        P("PAYPAL");

        private String situacao;

        TipoSolicitacaoSaldo(String situacao) {
            this.situacao = situacao;
        }
    }

}
