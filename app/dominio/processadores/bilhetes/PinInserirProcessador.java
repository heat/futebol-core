package dominio.processadores.bilhetes;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.bilhetes.Bilhete;
import models.bilhetes.Pin;
import models.vo.Tenant;
import repositories.BilheteRepository;
import repositories.EventoApostaRepository;
import repositories.PinRepository;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PinInserirProcessador implements Processador<Tenant, Pin>{

    public static final String REGRA = "pin.inserir";

    PinRepository pinRepository;
    EventoApostaRepository eventoApostaRepository;

    @Inject
    public PinInserirProcessador(PinRepository pinRepository, EventoApostaRepository eventoApostaRepository) {

        this.pinRepository = pinRepository;
        this.eventoApostaRepository = eventoApostaRepository;
    }

    @Override
    public CompletableFuture<Pin> executar(Tenant tenant, Pin pin, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador<Pin> validador: validadores) {
            validador.validate(pin);
        }

        List<Long> taxas = pin.getPalpitesPin().stream().map(p -> p.getTaxa()).collect(Collectors.toList());
        List<EventoAposta> eventosAposta = eventoApostaRepository.buscarPorTaxas(tenant, taxas);
        pin.setExpiraEm(getMenorDataEvento(eventosAposta));

        return pinRepository.inserir(tenant, pin) ;
    }

    public Calendar getMenorDataEvento(List<EventoAposta> eventosAposta){

        Calendar dataJogo = eventosAposta.get(0).getEvento().getDataEvento();

        for(EventoAposta eventoAposta : eventosAposta){
            Calendar d = eventoAposta.getEvento().getDataEvento();
            if (d.before(dataJogo)){
                dataJogo = d;
            }
        }

        return dataJogo;
    }
}
