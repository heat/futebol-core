package models.financeiro.credito;

import models.bilhetes.Bilhete;
import models.financeiro.Lancamento;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Informações de credito quando se realiza uma aposta. Normalmente o valor é o valor da aposta do bilhete
 */
@Entity
public class CompraBilheteCredito extends Lancamento<Bilhete> implements Credito {

    @OneToOne
    @JoinColumn(name = "origem_bilhete")
    public Bilhete origemBilhete;

    @Override
    public Bilhete getOrigem() {
        return this.origemBilhete;
    }

    @Override
    public Credito getTipo() {
        return this;
    }

    public Bilhete getOrigemBilhete() {
        return origemBilhete;
    }

    public void setOrigemBilhete(Bilhete origemBilhete) {
        this.origemBilhete = origemBilhete;
    }
}
