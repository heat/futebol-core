package dominio.validadores.eventos;

import models.eventos.Evento;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class EventoTimesDiferenteValidador extends Validador<Evento> {

    public EventoTimesDiferenteValidador() {

    }

    public EventoTimesDiferenteValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {
        if( getValorLogico() && evento.getCasa().equals(evento.getFora())){
            // ele pode indicar se aceita ou nao cadastro de times iguais competindo obs.: 'sem logica mas...'
            throw new ValidadorExcpetion("Times casa e fora não podem ter o mesmo nome! ");
        }
    }
}
