package dominio.processadores.bilhetes;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.bilhetes.Pin;
import models.vo.Tenant;
import repositories.BilheteRepository;
import repositories.PinRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PinInserirProcessador implements Processador<Tenant, Pin>{

    public static final String REGRA = "pin.inserir";

    PinRepository pinRepository;

    @Inject
    public PinInserirProcessador(PinRepository pinRepository) {

        this.pinRepository = pinRepository;
    }

    @Override
    public CompletableFuture<Pin> executar(Tenant tenant, Pin pin, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador<Pin> validador: validadores) {
            validador.validate(pin);
        }

        return pinRepository.inserir(tenant, pin) ;
    }
}
