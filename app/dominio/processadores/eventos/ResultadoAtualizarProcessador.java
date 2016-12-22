package dominio.processadores.eventos;

import dominio.processadores.Processador;
import models.eventos.Resultado;
import models.vo.Tenant;
import repositories.ResultadoRepository;
import validators.Validator;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public class ResultadoAtualizarProcessador implements Processador<Resultado> {

    public static final String REGRA = "resultado.atualizar";
    Long idResultado;

    ResultadoRepository repository;

    @Inject
    public ResultadoAtualizarProcessador(ResultadoRepository repository) {

        this.repository = repository;
        this.idResultado = idResultado;
    }

    @Override
    public CompletableFuture<Resultado> executar(Tenant tenant, Resultado resultadoNovo, List<Validator> validators) throws ValidadorExcpetion {
        return null;
    }

    public CompletableFuture<Resultado> executar(Tenant tenant, Resultado resultadoNovo, List<Validator> validators, Long idResultado) throws ValidadorExcpetion {

        for (Validator validator : validators) {
            validator.validate(resultadoNovo);
        }

        Optional<Resultado> resultadoAtual = repository.buscar(tenant, idResultado);

        if(resultadoAtual.isPresent()){
            validate(resultadoAtual.get(), resultadoNovo);
        }

        try{
            repository.atualizar(tenant, idResultado, resultadoNovo);
        }
        catch (NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(resultadoNovo);
    }

    private void validate(Resultado resultadoAtual, Resultado resultadoNovo) throws ValidadorExcpetion {

    }
}
