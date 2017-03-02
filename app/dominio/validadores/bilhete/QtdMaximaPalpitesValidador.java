package dominio.validadores.bilhete;

import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class QtdMaximaPalpitesValidador extends Validador<Bilhete> {

    public QtdMaximaPalpitesValidador() {
    }

    public QtdMaximaPalpitesValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete entity) throws ValidadorExcpetion {
        if(entity.getPalpites().size() > getValorInteiro()) {
            throw new ValidadorExcpetion("Quantidade máxima de palpites não permitida.");
        }
    }
}
