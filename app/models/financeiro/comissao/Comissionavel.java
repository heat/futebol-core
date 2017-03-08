package models.financeiro.comissao;

import models.bilhetes.Bilhete;

import java.math.BigDecimal;

/**
 * Qualquer objeto que pode ser referenciavel para gerar uma comissao
 * @param <B>
 */
public interface Comissionavel<B> {

    public BigDecimal valor();

    public B get();

    public static Comissionavel<Bilhete> aposta(Bilhete bilhete) {
        return new BilheteComissionavel(bilhete);
    }

    public static Comissionavel<Bilhete> premio(Bilhete bilhete) {
        return new PremioComissionavel(bilhete);
    }
}
