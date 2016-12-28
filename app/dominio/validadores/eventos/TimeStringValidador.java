package dominio.validadores.eventos;


import com.google.common.base.Strings;
import models.eventos.Time;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class TimeStringValidador extends Validador<Time> {

    public TimeStringValidador() {
    }

    public TimeStringValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Time time) throws ValidadorExcpetion {
        if ( Strings.isNullOrEmpty(time.getNome()) || Strings.isNullOrEmpty(time.getNome().trim())) {
            throw new ValidadorExcpetion("Nome inválido!");
        }
    }
}
