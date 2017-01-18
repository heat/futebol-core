package dominio.validadores.bilhete;

import dominio.validadores.IPolitica;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;

import java.math.BigDecimal;

/**
 * Politica de geração e definição do codigo do bilhete
 */
public class CodigoBilhetePolitica extends Validador<Bilhete> implements IPolitica<Bilhete> {

    public CodigoBilhetePolitica() {
    }

    public CodigoBilhetePolitica(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete bilheteSrc) throws ValidadorExcpetion {
        Bilhete bilhete = politica(bilheteSrc);

        String formato = getValorTexto();
        //TODO logica regex para ve se o formato do bilhete
    }

    @Override
    public Bilhete politica(Bilhete bilhete) {
        // TODO alguma logica para gerar o codigo do bilhete
        String codigo = "";
        bilhete.setCodigo(codigo);
        return bilhete;
    }
}
