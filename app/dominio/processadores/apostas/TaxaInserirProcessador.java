package dominio.processadores.apostas;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.EventoAposta;
import models.vo.Tenant;
import repositories.EventoApostaRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Deprecated
public class TaxaInserirProcessador implements Processador<Tenant, EventoAposta>{

    public static final String REGRA = "taxa.inserir";

    EventoApostaRepository apostaRepository;

    @Inject
    public TaxaInserirProcessador(EventoApostaRepository apostaRepository) {
        this.apostaRepository = apostaRepository;
    }

    @Override
    public CompletableFuture<EventoAposta> executar(Tenant tenant, EventoAposta eventoAposta, List<Validador> validadores) throws ValidadorExcpetion {

        eventoAposta.getTaxas().forEach(taxa -> {
            validadores.forEach(validador -> {
                validador.validate(taxa);
            });
            taxa.setTenant(tenant.get());
        });

        apostaRepository.inserirTaxa(tenant, eventoAposta);
        return CompletableFuture.completedFuture(eventoAposta);
    }
}
