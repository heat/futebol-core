package services;

import com.google.inject.Inject;
import models.apostas.EventoAposta;
import repositories.EventoApostaRepository;

import java.util.List;

public class EventoApostaService {

    EventoApostaRepository eventoApostaRepository;

    @Inject
    public EventoApostaService(EventoApostaRepository eventoApostaRepository) {
        this.eventoApostaRepository = eventoApostaRepository;
    }

    public List<EventoAposta> getEventosApostaByIds(Long tenant, List<Long> ids){
        List<EventoAposta> eventosAposta = eventoApostaRepository.buscar(tenant, ids);

        return eventosAposta;
    }
}

