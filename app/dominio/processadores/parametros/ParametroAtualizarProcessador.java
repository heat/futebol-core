package dominio.processadores.parametros;

import dominio.processadores.Processador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.eventos.Campeonato;
import models.vo.Chave;
import repositories.CampeonatoRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ParametroAtualizarProcessador implements Processador<Chave, Validador> {

    public static final String REGRA = "parametro.atualizar";

    ValidadorRepository repository;

    @Inject
    public ParametroAtualizarProcessador(ValidadorRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Validador> executar(Chave chave, Validador validador,
                                                  List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador val : validadores) {
            val.validate(validador);
        }

        try{
            repository.atualizar(chave.getTenant(), chave.getId(), validador);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(validador);
    }

}
