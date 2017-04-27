package dominio.processadores.apostas;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.OddConfiguracao;
import models.vo.Chave;
import repositories.OddRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OddConfigAtualizarProcessador implements Processador<Chave, OddConfiguracao> {

    public static final String REGRA = "odd.atualizar";

    OddRepository repository;

    @Inject
    public OddConfigAtualizarProcessador(OddRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<OddConfiguracao> executar(Chave chave, OddConfiguracao odd,
                                                  List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(odd);
        }

        try{
            repository.atualizarConfiguracao(chave.getTenant(), chave.getId(), odd);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(odd);
    }

}
