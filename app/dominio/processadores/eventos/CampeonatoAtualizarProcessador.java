package dominio.processadores.eventos;

import dominio.processadores.Processador;
import models.eventos.Campeonato;
import models.vo.Chave;
import models.vo.Tenant;
import repositories.CampeonatoRepository;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CampeonatoAtualizarProcessador implements Processador<Chave, Campeonato> {

    public static final String REGRA = "campeonato.atualizar";

    CampeonatoRepository repository;

    @Inject
    public CampeonatoAtualizarProcessador(CampeonatoRepository repository) {
        this.repository = repository;
    }

    public CompletableFuture<Campeonato> executar(Chave chave, Campeonato campeonato,
                                                  List<Validador> validadores) throws ValidadorExcpetion {

        for (Validador validador : validadores) {
            validador.validate(campeonato);
        }

        try{
            repository.atualizar(chave.getTenant(), chave.getId(), campeonato);
        }
        catch(NoResultException e){
            throw new ValidadorExcpetion(e.getMessage());
        }

        return CompletableFuture.completedFuture(campeonato);
    }
}
