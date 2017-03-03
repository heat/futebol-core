package repositories;

import models.financeiro.Lancamento;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LancamentoRepository implements Repository<Long, Lancamento> {


    JPAApi jpaApi;

    @Inject
    public LancamentoRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Lancamento> todos(Tenant tenant) {
        return null;
    }

    @Override
    public Optional<Lancamento> buscar(Tenant tenant, Long id) {
        return null;
    }

    @Override
    public CompletableFuture<Lancamento> atualizar(Tenant tenant, Long id, Lancamento updetable) {
        return null;
    }

    @Override
    public CompletableFuture<Lancamento> inserir(Tenant tenant, Lancamento novo) {
        EntityManager em = jpaApi.em();
        em.persist(novo);
        return CompletableFuture.completedFuture(novo);
    }

    public CompletableFuture<Lancamento> inserir(Lancamento novo) {
        EntityManager em = jpaApi.em();
        em.persist(novo);
        return CompletableFuture.completedFuture(novo);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }
}
