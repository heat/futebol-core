package validators.eventos;

import models.eventos.Campeonato;
import models.eventos.Evento;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Optional;

@Entity
@Table(name = "validadores")
public class EventoCampeonatoValidador extends Validador<Evento>{

    public EventoCampeonatoValidador() {
    }

    public EventoCampeonatoValidador(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {

        Optional<Evento> eventoOptional = Optional.ofNullable(evento);

        if (!eventoOptional.isPresent()){
            throw new ValidadorExcpetion("Evento não informado! ");
        }
        Optional<Campeonato> campeonato = Optional.ofNullable(evento.getCampeonato());
        if (!campeonato.isPresent() ||
                evento.getCampeonato().getId() <= 0) {
            throw new ValidadorExcpetion("Campeonato não informado! ");
        }
    }
}
