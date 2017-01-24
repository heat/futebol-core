package dominio.validadores.bilhete;

import dominio.validadores.IPolitica;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Politica de geração e definição do codigo do bilhete
 */
@Entity
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
        if(!(bilheteSrc.getCodigo().length() != formato.length())){
           throw new ValidadorExcpetion("Código do bilhete não possui tamanho padrão: XXX-XXX-XX-00 ");
        }
    }

    @Override
    public Bilhete politica(Bilhete bilhete) {
        UUID uuid = UUID.randomUUID();
        String uuidString = String.valueOf(uuid).replaceAll("-", "");
        uuidString = uuidString.toUpperCase();
        String parte1 = uuidString.substring(0,3);
        String parte2 = uuidString.substring(3,7);
        String parte3 = uuidString.substring(7,10);
        String parte4 = "00";

        StringBuilder codigo = new StringBuilder();

        codigo.append(parte1 + "-" + parte2 + "-" + parte3 + "-" + parte4);
        bilhete.setCodigo(codigo.toString());
        return bilhete;
    }
}
