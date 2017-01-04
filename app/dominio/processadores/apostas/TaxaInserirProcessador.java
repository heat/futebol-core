package dominio.processadores.apostas;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.Taxa;
import models.vo.Tenant;
import repositories.TaxaRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TaxaInserirProcessador implements Processador<Tenant, Taxa>{

    public static final String REGRA = "taxa.inserir";

    TaxaRepository taxaRepository;

    @Inject
    public TaxaInserirProcessador(TaxaRepository taxaRepository) {

        this.taxaRepository = taxaRepository;
    }

    @Override
    public CompletableFuture<Taxa> executar(Tenant tenant, Taxa taxa, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(taxa);
        }

        taxaRepository.inserir(tenant, taxa);
        return CompletableFuture.completedFuture(taxa);
    }
}
