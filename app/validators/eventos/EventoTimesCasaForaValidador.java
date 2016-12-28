package validators.eventos;

import models.eventos.Evento;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Deprecated
@Entity
public class EventoTimesCasaForaValidador extends Validador<Evento> {

    public EventoTimesCasaForaValidador() {

    }

    public EventoTimesCasaForaValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {

    }
}
