package dominio.validadores.bilhete;

import dominio.validadores.IPolitica;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Politica de premio máximo
 */
@Entity
public class PremioMaximoPolitica extends Validador<Bilhete> implements IPolitica<Bilhete> {

    public PremioMaximoPolitica() {
    }

    public PremioMaximoPolitica(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete bilheteSrc) throws ValidadorExcpetion {
        Bilhete bilhete = politica(bilheteSrc);

    }

    @Override
    public Bilhete politica(Bilhete bilhete) {
        BigDecimal valorMaximoPremio = getValorDecimal();
        BigDecimal valorPremio = bilhete.getValorAposta();

        bilhete.getPalpites().forEach(p -> {
            valorPremio.multiply(p.getTaxa().getTaxa());
        });

        if (valorPremio.compareTo(valorMaximoPremio) < 0){
            bilhete.setValorPremio(valorMaximoPremio);
        }

        return bilhete;
    }
}
