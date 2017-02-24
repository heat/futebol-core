package dominio.validadores.bilhete;

import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.seguranca.Usuario;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Entity
public class TempoCancelamentoValidador extends Validador<Bilhete> {

    public TempoCancelamentoValidador() {
    }

    public TempoCancelamentoValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete entity) throws ValidadorExcpetion {
        Long tempo = getValorInteiro();
        Calendar now = Calendar.getInstance();
        Calendar entityDate = entity.getCriadoEm();
        entityDate.add(Calendar.MINUTE, tempo.intValue());

        if (now.after(entityDate)){
            throw new ValidadorExcpetion("Tempo de cancelamento de bilhete excedido.");
        }

    }
}
