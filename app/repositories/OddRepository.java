package repositories;


import com.google.inject.Inject;
import models.apostas.Odd;
import models.apostas.OddConfiguracao;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

public class OddRepository implements Repository<Long, Odd>{

    JPAApi jpaApi;

    @Inject
    public OddRepository(JPAApi jpaApi) {

        this.jpaApi = jpaApi;
    }

    @Override
    public List<Odd> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("SELECT o FROM Odd as o ");
        return query.getResultList();
    }

    @Override
    public Optional<Odd> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            Odd odd = em.find(Odd.class, id);
            return Optional.ofNullable(odd);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Odd> atualizar(Tenant tenant, Long id, Odd o) {
        return CompletableFuture.supplyAsync(() -> {
           throw  new RejectedExecutionException("não implementado");
        });
    }

    @Override
    public CompletableFuture<Odd> inserir(Tenant tenant, Odd odd) {

        EntityManager em = jpaApi.em();
        em.persist(odd);
        return CompletableFuture.completedFuture(odd);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {

        EntityManager em = jpaApi.em();
        Optional<Odd> oddOptional = buscar(tenant, id);
        if(!oddOptional.isPresent())
            throw new NoResultException("Odd não encontrada");
        em.remove(oddOptional.get());
        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }

    public List<OddConfiguracao> todosConfiguracao(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("SELECT o FROM OddConfiguracao o WHERE o.tenant = :tenant ");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }
}
