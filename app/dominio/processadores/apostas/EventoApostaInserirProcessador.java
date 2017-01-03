package dominio.processadores.apostas;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.apostas.Taxa;
import models.vo.Tenant;
import repositories.EventoApostaRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventoApostaInserirProcessador implements Processador<Tenant, EventoAposta>{

    public static final String REGRA = "evento_aposta.inserir";

    EventoApostaRepository eventoApostaRepository;

    @Inject
    public EventoApostaInserirProcessador(EventoApostaRepository eventoApostaRepository) {

        this.eventoApostaRepository = eventoApostaRepository;
    }

    @Override
    public CompletableFuture<EventoAposta> executar(Tenant tenant, EventoAposta eventoAposta, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(eventoAposta);
        }
        List<Taxa> taxas = eventoAposta.getTaxas();
        for(Taxa taxa : taxas){

        }

        eventoApostaRepository.inserir(tenant, eventoAposta);
        return CompletableFuture.completedFuture(eventoAposta);
    }
}
