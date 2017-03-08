package models.financeiro.comissao;

import models.vo.Parametro;

import java.text.DecimalFormat;

/**
 * Qualquer informação utilizada como parametro para as classes que implementam um plano de comissao
 */
public class ParametroComissao {

    Parametro parametro;

    Long valor;

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }
}
