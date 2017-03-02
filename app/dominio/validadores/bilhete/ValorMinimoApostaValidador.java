package dominio.validadores.bilhete;

import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class ValorMinimoApostaValidador extends Validador<Bilhete> {

    public ValorMinimoApostaValidador() {
    }

    public ValorMinimoApostaValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete bilhete) throws ValidadorExcpetion {
         if(bilhete.getValorAposta().compareTo(getValorDecimal()) < 0) {
           throw new ValidadorExcpetion("Valor mínimo de aposta não permitido");
        }
    }
}
