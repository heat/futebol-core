package validators.bilhete;

import models.bilhetes.Bilhete;
import validators.Validador;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="validadores")
public class ValorMinimoApostaValidator extends Validador<Bilhete> {


    protected ValorMinimoApostaValidator() {

        super();
    }

    public ValorMinimoApostaValidator(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }


    @Override
    public void validate(Bilhete entity) {
        BigDecimal valorAposta = entity.valorAposta();
        if(valorAposta.compareTo(getValorDecimal()) > 0) {
            System.out.println("Valor da aposta acima do comum");
        }
    }
}
