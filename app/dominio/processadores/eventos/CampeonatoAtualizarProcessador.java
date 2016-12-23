package dominio.processadores.eventos;

import dominio.processadores.ProcessadorAtualizar;
import models.eventos.Campeonato;
import models.vo.Tenant;
import repositories.CampeonatoRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CampeonatoAtualizarProcessador implements ProcessadorAtualizar<Campeonato> {

    public static final String REGRA = "campeonato.atualizar";

    CampeonatoRepository repository;
    @Inject
    public CampeonatoAtualizarProcessador(CampeonatoRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Campeonato> executar(Tenant tenant, Campeonato campeonato,
                                                  List<Validador> validadores, Long idCampeonato) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(campeonato);
        }

        repository.atualizar(tenant, idCampeonato, campeonato);

        return CompletableFuture.completedFuture(campeonato);
    }
}
