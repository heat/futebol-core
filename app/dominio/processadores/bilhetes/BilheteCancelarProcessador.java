package dominio.processadores.bilhetes;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.vo.Chave;
import repositories.BilheteRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BilheteCancelarProcessador implements Processador<Chave, Bilhete>{

    public static final String REGRA = "bilhete.cancelar";

    BilheteRepository bilheteRepository;

    @Inject
    public BilheteCancelarProcessador(BilheteRepository bilheteRepository) {
        this.bilheteRepository = bilheteRepository;
    }

    @Override
    public CompletableFuture<Bilhete> executar(Chave chave, Bilhete bilhete, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(bilhete);
        }
        try{
            bilheteRepository.excluir(chave.getTenant(), chave.getId());
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(bilhete);
    }
}
