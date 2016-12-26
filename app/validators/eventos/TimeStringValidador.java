package validators.eventos;


import com.google.common.base.Strings;
import models.eventos.Time;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
public class TimeStringValidador extends Validador<Time> {

    public TimeStringValidador() {
    }

    public TimeStringValidador(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Time time) throws ValidadorExcpetion {
        if ( Strings.isNullOrEmpty(time.getNome()) || Strings.isNullOrEmpty(time.getNome().trim())) {
            throw new ValidadorExcpetion("Nome inválido!");
        }
    }
}
