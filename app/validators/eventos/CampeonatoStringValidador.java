package validators.eventos;

import com.google.common.base.Strings;
import models.eventos.Campeonato;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
public class CampeonatoStringValidador extends Validador<Campeonato> {

    private static final String NOME_VALIDADOR = "campeonato.nome";

    protected CampeonatoStringValidador() {

    }

    public CampeonatoStringValidador(Long idTenant, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, valorInteiro, valorLogico, valorDecimal, valorTexto, NOME_VALIDADOR);
    }

    @Override
    public void validate(Campeonato entity) throws ValidadorExcpetion {

        if ( Strings.isNullOrEmpty(entity.getNome()) || Strings.isNullOrEmpty(entity.getNome().trim())) {
            throw new ValidadorExcpetion("Nome inválido!");
        }
    }

}
