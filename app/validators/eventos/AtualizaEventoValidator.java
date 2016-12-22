package validators.eventos;

import com.google.common.base.Strings;
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
public class AtualizaEventoValidator extends Validador<Evento> {

    public AtualizaEventoValidator() {
    }

    public AtualizaEventoValidator(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {

/*        Editar:
        - Não pode alterar o time nem o campeonato
                - Pode colocar qualquer data*/

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

        Optional<Campeonato> campeonato = Optional.ofNullable(evento.getCampeonato());
        if (!campeonato.isPresent() ||
                evento.getCampeonato().getId() <= 0) {
            throw new ValidadorExcpetion("Campeonato não selecionado! ");
        }
    }

}
