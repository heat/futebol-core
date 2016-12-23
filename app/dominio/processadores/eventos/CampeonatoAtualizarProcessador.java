package dominio.processadores.eventos;

import dominio.processadores.ProcessadorAtualizar;
import models.eventos.Campeonato;
import models.vo.Tenant;
import repositories.CampeonatoRepository;
import validators.Validador;
import validators.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
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

        try{
            repository.atualizar(tenant, idCampeonato, campeonato);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }


        return CompletableFuture.completedFuture(campeonato);
    }
}
