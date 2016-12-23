package dominio.processadores.eventos;

import dominio.processadores.ProcessadorAtualizar;
import models.eventos.Resultado;
import models.vo.Tenant;
import repositories.ResultadoRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public class ResultadoAtualizarProcessador implements ProcessadorAtualizar<Resultado> {

    public static final String REGRA = "resultado.atualizar";
    ResultadoRepository repository;

    @Inject
    public ResultadoAtualizarProcessador(ResultadoRepository repository) {

        this.repository = repository;
    }

    @Override
    public CompletableFuture<Resultado> executar(Tenant tenant, Resultado resultadoNovo, List<Validador> validadores, Long idResultado) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(resultadoNovo);
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
