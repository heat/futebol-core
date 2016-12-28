package dominio.processadores.eventos;

import dominio.processadores.ProcessadorInserir;
import models.eventos.Campeonato;
import models.vo.Tenant;
import repositories.CampeonatoRepository;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CampeonatoInserirProcessador implements ProcessadorInserir<Campeonato> {


    public static final String REGRA = "campeonato.inserir";

    CampeonatoRepository repository;

    @Inject
    public CampeonatoInserirProcessador(CampeonatoRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Campeonato> executar(Tenant tenant, Campeonato campeonato, List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(campeonato);
        }

        repository.inserir(tenant, campeonato);
        return CompletableFuture.completedFuture(campeonato);
    }
}
