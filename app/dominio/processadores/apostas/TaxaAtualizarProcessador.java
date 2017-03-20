package dominio.processadores.apostas;

import com.google.inject.Inject;
import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.Taxa;
import models.vo.Chave;
import repositories.TaxaRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Deprecated
public class TaxaAtualizarProcessador implements Processador<Chave, Taxa>{

    public static final String REGRA = "taxa.atualizar";

    TaxaRepository taxaRepository;

    @Inject
    public TaxaAtualizarProcessador(TaxaRepository taxaRepository) {
        this.taxaRepository = taxaRepository;
    }

    @Override
    public CompletableFuture<Taxa> executar(Chave chave, Taxa taxa, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(taxa);
        }
        try{
            taxaRepository.atualizar(chave.getTenant(), chave.getId(), taxa);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(taxa);
    }
}
