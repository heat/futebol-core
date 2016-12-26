package validators.eventos;

import models.eventos.Evento;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Optional;

@Entity
public class EventoTimesDiferenteValidador extends Validador<Evento> {

    public EventoTimesDiferenteValidador() {

    }

    public EventoTimesDiferenteValidador(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {

        Optional<Evento> eventoOptional = Optional.ofNullable(evento);

        if (!eventoOptional.isPresent()){
            throw new ValidadorExcpetion("Evento não informado! ");
        }

        if(evento.getCasa().equals(evento.getFora())){
            throw new ValidadorExcpetion("Times casa e fora não podem ter o mesmo nome! ");
        }

    }
}
