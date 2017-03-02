package dominio.validadores.bilhete;

import com.google.inject.Inject;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.Apostavel;
import models.apostas.EventoAposta;
import models.bilhetes.Bilhete;
import models.eventos.Evento;
import repositories.EventoApostaRepository;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class EventosAbertosApostasValidador extends Validador<Bilhete> {

    @Inject
    EventoApostaRepository eventoApostaRepository;

    public EventosAbertosApostasValidador() {
    }

    public EventosAbertosApostasValidador(Long idTenant, String regra, Long valorInteiro, Boolean valorLogico, BigDecimal valorDecimal, String valorTexto) {
        super(idTenant, regra, valorInteiro, valorLogico, valorDecimal, valorTexto);
    }

    @Override
    public void validate(Bilhete entity) throws ValidadorExcpetion {

        List<Long> idsEventoAposta = entity.getPalpites().stream().map(p -> p.getTaxa().getEventoAposta()).collect(Collectors.toList());

        List<EventoAposta> eventosAposta = eventoApostaRepository.buscar(entity.getTenant(), idsEventoAposta);

        eventosAposta.forEach(e -> {
            if (e.getSituacao() != Apostavel.Situacao.A){
                throw new ValidadorExcpetion("A aposta possui partida(s) fechada(s).");
            }
        });
    }
}
