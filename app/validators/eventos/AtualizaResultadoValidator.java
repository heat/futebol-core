package validators.eventos;

import models.eventos.Resultado;
import validators.Validator;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "validadores")
public class AtualizaResultadoValidator extends Validator<Resultado> {


    public AtualizaResultadoValidator() {
    }

    public AtualizaResultadoValidator(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Resultado entity) throws ValidadorExcpetion {

    }
}
