package dominio.validadores.bilhete;

import com.google.inject.Inject;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.Apostavel;
import models.apostas.EventoAposta;
import models.bilhetes.Bilhete;
import repositories.EventoApostaRepository;
import services.EventoApostaService;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class TempoLimiteApostasValidador extends Validador<Bilhete> {

    public TempoLimiteApostasValidador() {
    }

    public TempoLimiteApostasValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete entity) throws ValidadorExcpetion {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, getValorInteiro().intValue());

        entity.getEventosAposta().forEach(e -> {
            if (now.after(e.getEvento().getDataEvento())){
                throw new ValidadorExcpetion("A aposta possui partida(s) fechada(s).");
            }
        });
    }
}
