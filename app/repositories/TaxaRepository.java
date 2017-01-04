package repositories;


import com.google.inject.Inject;
import models.apostas.Taxa;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TaxaRepository implements Repository<Long, Taxa>{

    JPAApi jpaApi;

    @Inject
    public TaxaRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Taxa> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Taxa as t WHERE t.tenant = :tenant");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    @Override
    public Optional<Taxa> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Taxa> query = em.createQuery("SELECT t FROM Taxa t WHERE t.tenant = :tenant and t.id = :id",
                                                                Taxa.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Taxa> atualizar(Tenant tenant, Long id, Taxa t) {
        EntityManager em = jpaApi.em();
        Optional<Taxa> taxaOptional = buscar(tenant, id);
        if(!taxaOptional.isPresent()){
            throw new NoResultException("Taxa não encontrada");
        }
        Taxa taxa = taxaOptional.get();
        taxa.setOdd(t.getOdd());
        taxa.setTaxa(t.getTaxa());
        taxa.setLinha(t.getLinha());
        taxa.setAlteradoEm(t.getAlteradoEm());
        em.merge(taxa);
        return CompletableFuture.completedFuture(taxa);
    }

    @Override
    public CompletableFuture<Taxa> inserir(Tenant tenant, Taxa taxa) {
        EntityManager em = jpaApi.em();
        em.persist(taxa);
        taxa.setTenant(tenant.get());
        return CompletableFuture.completedFuture(taxa);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        EntityManager em = jpaApi.em();
        Optional<Taxa> taxaOptional = buscar(tenant, id);
        if(!taxaOptional.isPresent()){
            throw new NoResultException("Taxa não encontrada");
        }
        else{
            Taxa taxa = taxaOptional.get();
            em.remove(taxa);
        }
        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }
}
