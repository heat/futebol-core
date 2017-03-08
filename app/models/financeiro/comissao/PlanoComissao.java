package models.financeiro.comissao;

import models.bilhetes.Bilhete;

import java.util.List;
import java.util.Optional;

/**
 * Representa um plano de pagamento de comissao gerador a partir de um evento específico.
 */
public abstract class PlanoComissao {

    public enum EVENTO_COMISSAO {
        VENDA_BILHETE, PAGAMENTO_PREMIO, FECHAMENTO_CAIXA
    }

    List<ParametroComissao> parametros;

    public List<ParametroComissao> getParametros() {
        return parametros;
    }


    public abstract Optional<Comissao> calcular(Comissionavel<Bilhete> b, EVENTO_COMISSAO evento);
}