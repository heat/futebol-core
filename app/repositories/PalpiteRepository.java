package repositories;

import models.bilhetes.Palpite;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PalpiteRepository implements Repository<Long, Palpite> {

    JPAApi jpaApi;

    @Inject
    public PalpiteRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Palpite> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Palpite as p WHERE p.tenant = :tenant");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    @Override
    public Optional<Palpite> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Palpite> query = em.createQuery("SELECT p FROM Palpite p WHERE p.tenant = :tenant and p.id = :id", Palpite.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Palpite> atualizar(Tenant tenant, Long id, Palpite p) {

        EntityManager em = jpaApi.em();
        Optional<Palpite> palpite = buscar(tenant, id);
        if(!palpite.isPresent())
            throw new NoResultException("Palpite não encontrado");
        Palpite ppt = palpite.get();
        ppt.setStatus(p.getStatus());
        ppt.setTaxa(p.getTaxa());
        em.merge(ppt);
        return CompletableFuture.completedFuture(ppt);
    }

    @Override
    public CompletableFuture<Palpite> inserir(Tenant tenant, Palpite palpite) {

        EntityManager em = jpaApi.em();
        palpite.setTenant(tenant.get());
        em.persist(palpite);
        return CompletableFuture.completedFuture(palpite);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) throws NoResultException{

        EntityManager em = jpaApi.em();
        Optional<Palpite> palpite = buscar(tenant, id);
        if(!palpite.isPresent())
            throw new NoResultException("Palpite não encontrado");
        em.remove(palpite.get());
        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }
}
