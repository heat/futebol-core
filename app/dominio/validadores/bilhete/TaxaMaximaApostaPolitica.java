package dominio.validadores.bilhete;

import dominio.validadores.IPolitica;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Politica de taxa máxima
 */
@Entity
public class TaxaMaximaApostaPolitica extends Validador<Bilhete> implements IPolitica<Bilhete> {

    public TaxaMaximaApostaPolitica() {
    }

    public TaxaMaximaApostaPolitica(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete bilheteSrc) throws ValidadorExcpetion {
        Bilhete bilhete = politica(bilheteSrc);

    }

    @Override
    public Bilhete politica(Bilhete bilhete) {
        if (getValorLogico()){
            BigDecimal taxaMaxima = getValorDecimal().multiply(bilhete.getValorAposta());
            BigDecimal valorPremio = bilhete.getValorAposta();

            bilhete.getPalpites().forEach(p ->  valorPremio.multiply(p.getTaxa().getTaxa()));

            if (valorPremio.compareTo(taxaMaxima) > 0){
                bilhete.setValorPremio(taxaMaxima);
            }
        }

        return bilhete;
    }
}
