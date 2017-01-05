package repositories;

import models.bilhetes.Bilhete;
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

public class BilheteRepository implements Repository<Long, Bilhete> {

    JPAApi jpaApi;

    @Inject
    public BilheteRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Bilhete> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Bilhete as b WHERE b.tenant = :tenant");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    @Override
    public Optional<Bilhete> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Bilhete> query = em.createQuery("SELECT b FROM Bilhete b WHERE b.tenant = :tenant and b.id = :id", Bilhete.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Bilhete> atualizar(Tenant tenant, Long id, Bilhete b) {

        EntityManager em = jpaApi.em();
        Optional<Bilhete> bilhete = buscar(tenant, id);
        if(!bilhete.isPresent())
            throw new NoResultException("Bilhete não encontrado");
        Bilhete blt = bilhete.get();
        blt.setAlteradoEm(b.getAlteradoEm());
        blt.setCliente(b.getCliente());
        blt.setCodigo(b.getCodigo());
        blt.setPalpites(b.getPalpites());
        blt.setStatus(b.getStatus());
        blt.setValorAposta(b.getValorAposta());
        blt.setValorPremio(b.getValorPremio());
        em.merge(blt);
        return CompletableFuture.completedFuture(blt);
    }

    @Override
    public CompletableFuture<Bilhete> inserir(Tenant tenant, Bilhete bilhete) {

        EntityManager em = jpaApi.em();
        bilhete.setTenant(tenant.get());
        em.persist(bilhete);
        return CompletableFuture.completedFuture(bilhete);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) throws NoResultException{

        EntityManager em = jpaApi.em();
        Optional<Bilhete> bilhete = buscar(tenant, id);
        if(!bilhete.isPresent())
            throw new NoResultException("Bilhete não encontrado");
        em.remove(bilhete.get());
        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }
}
