package dominio.processadores.eventos;

import dominio.processadores.Processador;
import models.eventos.Campeonato;
import models.vo.Tenant;
import repositories.CampeonatoRepository;
import validators.Validator;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CampeonatoInserirProcessador implements Processador<Campeonato> {


    public static final String REGRA = "campeonato.inserir";

    CampeonatoRepository repository;

    Tenant tenant;

    @Inject
    public CampeonatoInserirProcessador(CampeonatoRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Campeonato> executar(Tenant tenant, Campeonato campeonato, List<Validator> validators) throws ValidadorExcpetion {

        for (Validator validator : validators) {
            validator.validate(campeonato);
        }

        repository.inserir(tenant, campeonato);
        return CompletableFuture.completedFuture(campeonato);
    }
}
