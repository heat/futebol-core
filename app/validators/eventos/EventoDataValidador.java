package validators.eventos;

import models.eventos.Evento;
import validators.Validador;
import validators.comuns.DataValidador;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Calendar;


@Entity
public class EventoDataValidador extends Validador<Evento>{

    public EventoDataValidador() {
    }

    public EventoDataValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    /**
     * Valida se a data de realizacao do evento é anterior a data atual do sistema
     * @param evento
     * @throws ValidadorExcpetion
     */
    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {
        Calendar agora = Calendar.getInstance();
        DataValidador validador = DataValidador.anterior(agora);
        validador.validate(evento.getDataEvento());
    }
}
