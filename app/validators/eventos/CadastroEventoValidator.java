package validators.eventos;

import models.eventos.Campeonato;
import models.eventos.Evento;
import validators.Validator;
import validators.exceptions.ValidadorExcpetion;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

import com.google.common.base.Strings;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "validadores")
public class CadastroEventoValidator extends Validator<Evento> {

    protected CadastroEventoValidator() {

    }

    public CadastroEventoValidator(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {

        Optional<Evento> eventoOptional = Optional.ofNullable(evento);
        if (!eventoOptional.isPresent()){
            throw new ValidadorExcpetion("Evento não informado! ");
        }

        if ( Strings.isNullOrEmpty(evento.getCasa()) || Strings.isNullOrEmpty(evento.getCasa().trim())) {
            throw new ValidadorExcpetion("Nome time casa inválido! ");
        }

        if ( Strings.isNullOrEmpty(evento.getFora()) || Strings.isNullOrEmpty(evento.getFora().trim())) {
            throw new ValidadorExcpetion("Nome time fora inválido! ");
        }

        if(evento.getCasa().equals(evento.getFora())){
                throw new ValidadorExcpetion("Times casa e fora não podem ter o mesmo nome! ");
        }

/*        if(evento.getDataEvento()== null || evento.getDataEvento().before(Calendar.getInstance()) ){
               throw new ValidadorExcpetion("Data do evento deve ser após a partir da data atual ");
        }*/

        Optional<Campeonato> campeonato = Optional.ofNullable(evento.getCampeonato());
        if (!campeonato.isPresent() ||
                evento.getCampeonato().getId() <= 0) {
            throw new ValidadorExcpetion("Campeonato não informado! ");
        }
    }
}
