package dominio.processadores.eventos;

import dominio.processadores.Processador;
import models.eventos.Campeonato;
import models.vo.Tenant;
import repositories.CampeonatoRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CampeonatoAtualizarProcessador implements Processador<Campeonato> {


    public static final String REGRA = "campeonato.atualizar";
    private final Long idCampeonato;

    CampeonatoRepository repository;

    @Inject
    public CampeonatoAtualizarProcessador(CampeonatoRepository repository, Long idCampeonato) {
        this.repository = repository;
        this.idCampeonato = idCampeonato;
    }

    public CompletableFuture<Campeonato> executar(Tenant tenant, Campeonato campeonato, List<Validador> validators) throws ValidadorExcpetion {

        for (Validador validator : validators) {
            validator.validate(campeonato);
        }

        repository.atualizar(tenant, idCampeonato, campeonato);


        return CompletableFuture.completedFuture(campeonato);
    }
}
