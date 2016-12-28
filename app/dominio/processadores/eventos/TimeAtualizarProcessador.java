package dominio.processadores.eventos;

import dominio.processadores.Processador;
import models.eventos.Time;
import models.vo.Chave;
import models.vo.Tenant;
import repositories.TimeRepository;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TimeAtualizarProcessador implements Processador<Chave, Time> {

    public static final String REGRA = "time.atualizar";

    TimeRepository repository;

    @Inject
    public TimeAtualizarProcessador(TimeRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletableFuture<Time> executar(Chave chave, Time time, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(time);
        }
        try{
            repository.atualizar(chave.getTenant(), chave.getId(), time);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }
        return CompletableFuture.completedFuture(time);
    }
}
