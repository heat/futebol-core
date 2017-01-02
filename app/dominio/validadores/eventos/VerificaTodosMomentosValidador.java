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

/*
        Optional<Evento> result = Optional.ofNullable(evento);
        if(!result.isPresent()){
            throw new NoResultException("Evento não encontrado");
        }
        List<Resultado> resultados =  evento.getResultados();

        Optional<List<Resultado>> listaResultados = Optional.ofNullable(evento.getResultados());

        if(!listaResultados.isPresent()){
            throw new NoResultException("Resultado não encontrado");
        }

        boolean casaIntervalo = false;
        boolean foraIntervalo = false;
        boolean casaFinal = false;
        boolean foraFinal = false;

        for(Resultado resultado: resultados){

            if(resultado.getTime().getId() == evento.getCasa().getId()
                    && resultado.isMomentoIntervalo()) casaIntervalo = true;


            if(resultado.getTime().getId() == evento.getFora().getId()
                    && resultado.isMomentoFinal()) foraIntervalo = true;

            if(resultado.getTime().getId() == evento.getCasa().getId()
                    && resultado.isMomentoIntervalo()) casaFinal = true;

            if(resultado.getTime().getId() == evento.getFora().getId()
                    && resultado.isMomentoFinal()) foraFinal = true;
        }

        if(!casaIntervalo || !foraIntervalo || !casaFinal || !foraFinal)
            throw new ValidadorExcpetion("Resultado inválido");
*/
    }



}
