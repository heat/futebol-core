package dominio.validadores.eventos;

import models.eventos.Campeonato;
import dominio.validadores.Validador;
import dominio.validadores.comuns.StringRegexValidador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Optional;

@Entity
public class CampeonatoNomeValidator extends Validador<Campeonato> {

    protected CampeonatoNomeValidator() {

    }

    public CampeonatoNomeValidator(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Campeonato campeonato) throws ValidadorExcpetion {
        if(getValorLogico()) { // verifica se esta habilidator a executar essa validacao
            StringRegexValidador.of(getValorTexto())
                    .validate(Optional.of(campeonato.getNome())); // faz a validação do nome do campeonato
        }
    }

}
