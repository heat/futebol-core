package dominio.validadores.bilhete;

import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class ValorMaximoApostaValidador extends Validador<Bilhete> {

    public ValorMaximoApostaValidador() {
    }

    public ValorMaximoApostaValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete entity) throws ValidadorExcpetion {
        if(entity.getValorAposta().compareTo(getValorDecimal()) > 0) {
            throw new ValidadorExcpetion("Valor máximo de aposta não permitido");
        }
    }
}
