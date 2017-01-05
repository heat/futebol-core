package dominio.processadores.bilhetes;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Palpite;
import models.vo.Chave;
import repositories.PalpiteRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PalpiteAtualizarProcessador implements Processador<Chave, Palpite>{

    public static final String REGRA = "palpite.atualizar";

    PalpiteRepository palpiteRepository;

    @Inject
    public PalpiteAtualizarProcessador(PalpiteRepository palpiteRepository) {
        this.palpiteRepository = palpiteRepository;
    }

    @Override
    public CompletableFuture<Palpite> executar(Chave chave, Palpite palpite, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(palpite);
        }
        try{
            palpiteRepository.atualizar(chave.getTenant(), chave.getId(), palpite);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(palpite);
    }
}
