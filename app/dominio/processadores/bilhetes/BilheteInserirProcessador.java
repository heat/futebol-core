package dominio.processadores.bilhetes;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.vo.Tenant;
import repositories.BilheteRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BilheteInserirProcessador implements Processador<Tenant, Bilhete>{

    public static final String REGRA = "bilhete.inserir";

    BilheteRepository bilheteRepository;

    @Inject
    public BilheteInserirProcessador(BilheteRepository bilheteRepository) {

        this.bilheteRepository = bilheteRepository;
    }

    @Override
    public CompletableFuture<Bilhete> executar(Tenant tenant, Bilhete bilhete, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(bilhete);
        }

        bilheteRepository.inserir(tenant, bilhete);
        return CompletableFuture.completedFuture(bilhete);
    }
}
