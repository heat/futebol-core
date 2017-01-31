package dominio.validadores.eventos;

import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Evento;
import models.eventos.Resultado;

import javax.persistence.Entity;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Entity
public class VerificaTodosMomentosValidador extends Validador<Evento>{

    public VerificaTodosMomentosValidador() {

    }

    public VerificaTodosMomentosValidador(Long idTenant, String regra, Long valorInteiro,
                                          Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Evento evento) throws ValidadorExcpetion {

        Optional<Evento> result = Optional.ofNullable(evento);
        if(!result.isPresent())
            throw new NoResultException("Evento não encontrado");

        List<Resultado> resultados =  evento.getResultados();

        if(!Optional.ofNullable(resultados).isPresent())
            throw new NoResultException("Resultado não encontrado");

        boolean casaIntervalo = false;
        boolean foraIntervalo = false;
        boolean casaFinal = false;
        boolean foraFinal = false;

        for(Resultado resultado: resultados){

            if(resultado.getTime().getId() == evento.getCasa().getId()
                    && resultado.getMomento().equals(Resultado.Momento.I)) casaIntervalo = true;

            if(resultado.getTime().getId() == evento.getFora().getId()
                    && resultado.getMomento().equals(Resultado.Momento.I)) foraIntervalo = true;

            if(resultado.getTime().getId() == evento.getCasa().getId()
                    && resultado.getMomento().equals(Resultado.Momento.F)) casaFinal = true;

            if(resultado.getTime().getId() == evento.getFora().getId()
                    && resultado.getMomento().equals(Resultado.Momento.F)) foraFinal = true;
        }

        if(!casaIntervalo || !foraIntervalo || !casaFinal || !foraFinal){
            resultados.clear();
            throw new ValidadorExcpetion("Resultado inválido");
        }

    }



}
