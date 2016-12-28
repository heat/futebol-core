package validators.bilhete;

import models.bilhetes.Bilhete;
import validators.Validador;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
public class ValorMinimoApostaValidator extends Validador<Bilhete> {

    private static final String NOME_VALIDADOR = "bilhete.valor";

    protected ValorMinimoApostaValidator() {

        super();
    }

    public ValorMinimoApostaValidator(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto, NOME_VALIDADOR);
    }


    @Override
    public void validate(Bilhete entity) {
        BigDecimal valorAposta = entity.valorAposta();
        if(valorAposta.compareTo(getValorDecimal()) > 0) {
            System.out.println("Valor da aposta acima do comum");
        }
    }
}
