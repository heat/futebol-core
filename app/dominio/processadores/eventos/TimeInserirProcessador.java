package dominio.processadores.eventos;

import dominio.processadores.ProcessadorInserir;
import models.eventos.Time;
import models.vo.Tenant;
import repositories.TimeRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class TimeInserirProcessador implements ProcessadorInserir<Time> {

    public static final String REGRA = "time.inserir";
    TimeRepository repository;

    @Inject
    public TimeInserirProcessador(TimeRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Time> executar(Tenant tenant, Time time, List<Validador> validadores) throws ValidadorExcpetion {
        for (Validador validador : validadores) {
            validador.validate(time);
        }

        repository.inserir(tenant, time);
        return CompletableFuture.completedFuture(time);
    }
}
