package dominio.processadores.eventos;

import dominio.processadores.ProcessadorAtualizar;
import models.eventos.Time;
import models.vo.Tenant;
import repositories.TimeRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TimeAtualizarProcessador implements ProcessadorAtualizar<Time> {

    public static final String REGRA = "time.atualizar";
    TimeRepository repository;

    @Inject
    public TimeAtualizarProcessador(TimeRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Time> executar(Tenant tenant, Time time, List<Validador> validadores, Long idTime) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(time);
        }
        try{
            repository.atualizar(tenant, idTime, time);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }
        return CompletableFuture.completedFuture(time);
    }
}
