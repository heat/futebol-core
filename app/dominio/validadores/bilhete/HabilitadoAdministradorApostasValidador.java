package dominio.validadores.bilhete;

import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class HabilitadoAdministradorApostasValidador extends Validador<Bilhete> {

    public HabilitadoAdministradorApostasValidador() {
    }

    public HabilitadoAdministradorApostasValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete entity) throws ValidadorExcpetion {
        if(!getValorLogico()) {
            throw new ValidadorExcpetion("Administrador não habilitado para fazer apostas.");
        }
    }
}
