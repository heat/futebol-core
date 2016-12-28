package validators.eventos;

import com.google.common.base.Strings;
import models.eventos.Evento;
import models.eventos.Time;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Optional;

@Entity
public class EventoTimesCasaForaValidador extends Validador<Evento> {

    public EventoTimesCasaForaValidador() {

    }

    public EventoTimesCasaForaValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {

        Optional<Evento> eventoOptional = Optional.ofNullable(evento);

        if (!eventoOptional.isPresent()){
            throw new ValidadorExcpetion("Evento não informado! ");
        }

        Optional<Time> casa = Optional.ofNullable(eventoOptional.get().getCasa());
        Optional<Time> fora = Optional.ofNullable(eventoOptional.get().getFora());
        if ( !casa.isPresent()) {
            throw new ValidadorExcpetion("Time da casa não informado! ");
        }

        if ( !fora.isPresent()) {
            throw new ValidadorExcpetion("Time de fora não informado! ");
        }    }
}
