package models.financeiro.comissao;

import models.bilhetes.Bilhete;
import models.financeiro.Conta;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ComissaoBilhete extends Comissao<Bilhete> {

    @ManyToOne
    @JoinColumn(name = "origem_bilhete")
    public Bilhete bilhete;

    public ComissaoBilhete(Conta conta, BigDecimal valorComissao, PlanoComissao.EVENTO_COMISSAO evento, Bilhete bilhete) {
        super(conta, valorComissao, evento);
        this.bilhete = bilhete;
    }

    public ComissaoBilhete() {
    }

    public Bilhete getBilhete() {
        return bilhete;
    }

    public void setBilhete(Bilhete bilhete) {
        this.bilhete = bilhete;
    }

    @Override
    public Comissionavel<Bilhete> getComissionavel() {
        return Comissionavel.aposta(bilhete);
    }
}
