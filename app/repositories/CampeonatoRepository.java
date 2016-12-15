package repositories;

import models.eventos.Campeonato;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CampeonatoRepository implements Repository<Long, Campeonato> {

    JPAApi jpaApi;

    @javax.inject.Inject
    public CampeonatoRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Campeonato> todos(Tenant tenant) {
        return jpaApi.em().createQuery("select c from Campeonato c")
                .getResultList();
    }

    @Override
    public Optional<Campeonato> registro(Tenant tenant, Long id) {
        return null;
    }

    @Override
    public CompletableFuture<Campeonato> atualizar(Tenant tenant, Long id, Campeonato updetable) {
        return null;
    }

    @Override
    public CompletableFuture<Campeonato> inserir(Tenant tenant, Campeonato novo) {
        return null;
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }
}
