package dominio.processadores.eventos;

import dominio.processadores.ProcessadorInserir;
import models.eventos.Evento;
import models.eventos.Resultado;
import models.vo.Tenant;
import repositories.ResultadoRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ResultadoInserirProcessador implements ProcessadorInserir<Resultado> {

    public static final String REGRA = "resultado.inserir";

    ResultadoRepository repository;

    @Inject
    public ResultadoInserirProcessador(ResultadoRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Resultado> executar(Tenant tenant, Resultado resultado, List<Validador> validadores) throws ValidadorExcpetion {

        return null;
    }

    public CompletableFuture<Resultado> executar(Tenant tenant, Resultado[] resultados, Evento evento, List<Validador> validadores) throws ValidadorExcpetion {

       validate(resultados, evento);

        Resultado result = null;
        for(Resultado resultado : resultados){
            resultado.setEvento(evento);
            repository.inserir(tenant, resultado);
            result = resultado;
        }

       return CompletableFuture.completedFuture(result);

    }

    public void validate(Resultado[] resultados, Evento evento){

    }
}
