package models.financeiro.comissao;

import models.bilhetes.Bilhete;

import java.math.BigDecimal;

class PremioComissionavel implements Comissionavel<Bilhete> {

    final Bilhete bilhete;

    public PremioComissionavel(Bilhete bilhete) {
        this.bilhete = bilhete;
    }

    @Override
    public BigDecimal valor() {
        return this.bilhete.getValorPremio();
    }

    @Override
    public Bilhete get() {
        return this.bilhete;
    }
}
