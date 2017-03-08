package models.financeiro.comissao;

import models.bilhetes.Bilhete;

import java.math.BigDecimal;

class BilheteComissionavel implements Comissionavel<Bilhete> {

    final Bilhete bilhete;

    public BilheteComissionavel(Bilhete bilhete) {
        this.bilhete = bilhete;
    }

    @Override
    public BigDecimal valor() {
        return bilhete.getValorAposta();
    }

    @Override
    public Bilhete get() {
        return this.bilhete;
    }
}
