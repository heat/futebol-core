package models.financeiro.credito;

import models.bilhetes.Bilhete;
import models.financeiro.Lancamento;

/**
 * Informações de credito quando se realiza uma aposta. Normalmente o valor é o valor da aposta do bilhete
 */
public class CompraBilheteCredito extends Lancamento<Bilhete> implements Credito {


    public Bilhete origemBilhete;


    @Override
    public Bilhete getOrigem() {
        return this.origemBilhete;
    }

    @Override
    public Credito getTipo() {
        return this;
    }
}
