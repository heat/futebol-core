package validators.eventos;

import models.eventos.Evento;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

@Entity
public class EventoDataValidador extends Validador<Evento>{

    public EventoDataValidador() {
    }

    public EventoDataValidador(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {

        Optional<Evento> eventoOptional = Optional.ofNullable(evento);

        if (!eventoOptional.isPresent()){
            throw new ValidadorExcpetion("Evento não informado! ");
        }
        if(evento.getDataEvento()== null || evento.getDataEvento().before(Calendar.getInstance()) ){
            throw new ValidadorExcpetion("Data do evento deve ser após a data atual ");
        }
    }
}
