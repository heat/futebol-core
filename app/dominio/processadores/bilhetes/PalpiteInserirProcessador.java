package dominio.processadores.bilhetes;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Palpite;
import models.vo.Tenant;
import repositories.PalpiteRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PalpiteInserirProcessador implements Processador<Tenant, Palpite>{

    public static final String REGRA = "palpite.inserir";

    PalpiteRepository palpiteRepository;

    @Inject
    public PalpiteInserirProcessador(PalpiteRepository palpiteRepository) {

        this.palpiteRepository = palpiteRepository;
    }

    @Override
    public CompletableFuture<Palpite> executar(Tenant tenant, Palpite palpite, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(palpite);
        }

        palpiteRepository.inserir(tenant, palpite);
        return CompletableFuture.completedFuture(palpite);
    }
}
