package dominio.validadores.usuarios;

import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.seguranca.Perfil;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
public class PerfilLocalidadeValidador extends Validador<Perfil> {

    @Transient
    List<Locale> localidades = new ArrayList<>();

    public PerfilLocalidadeValidador() {
        this.localidades.add(new Locale("pt", "BR"));
    }

    public PerfilLocalidadeValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Perfil entity) throws ValidadorExcpetion {

        if (!this.localidades.contains(entity.getLocalidade())){
            throw new ValidadorExcpetion("Localidade não permitida.");
        }
    }
}
