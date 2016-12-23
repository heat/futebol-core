package validators.eventos;

import models.eventos.Resultado;
import validators.Validator;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "validadores")
public class InsereResultadoValidator extends Validator<Resultado> {

    public InsereResultadoValidator() {
    }

    public InsereResultadoValidator(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Resultado entity) throws ValidadorExcpetion {

        /*

        A regra do resultado será:
        Vai receber uma lista de resultados
         Nessa lista tem que ter todos os momentos e um resultado para cada time
         exemplo temos hoje o momento intervalo e final
         então tem que ter na lista
         e temos 2 times em um evento
         1 resultado para casa intervalo, 1 resultado para fora intervalo, 1 resultado para casa final e 1 resultado para casa final*/

    }
}
