package dominio.validadores.bilhete;

import models.bilhetes.Bilhete;
import dominio.validadores.Validador;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class ValorMinimoApostaValidator extends Validador<Bilhete> {

    protected ValorMinimoApostaValidator() {

        super();
    }

    public ValorMinimoApostaValidator(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete entity) {
        BigDecimal valorAposta = entity.valorAposta();
        if(valorAposta.compareTo(getValorDecimal()) > 0) {
            System.out.println("Valor da aposta acima do comum");
        }
    }
}
