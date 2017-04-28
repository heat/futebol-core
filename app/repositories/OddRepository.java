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
import javax.persistence.TypedQuery;
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

    public CompletableFuture<OddConfiguracao> atualizarConfiguracao(Tenant tenant, Long id, OddConfiguracao o) {
        EntityManager em = jpaApi.em();
        Optional<OddConfiguracao> oddConfiguracaoOptional = buscarConfiguracao(tenant, id);
        if(!oddConfiguracaoOptional.isPresent())
            throw new NoResultException("Parametro não encontrado");
        OddConfiguracao oddConfiguracao = oddConfiguracaoOptional.get();
        oddConfiguracao.setFavorita(o.getFavorita());
        oddConfiguracao.setLinhaFavorita(o.getLinhaFavorita());
        oddConfiguracao.setPrioridade(o.getPrioridade());
        oddConfiguracao.setSituacao(o.getSituacao());
        em.merge(oddConfiguracao);

        o.setId(oddConfiguracao.getId());
        o.setOdd(oddConfiguracao.get());
        o.setTenant(oddConfiguracao.getTenant());

        return CompletableFuture.completedFuture(oddConfiguracao);
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

    public Optional<OddConfiguracao> buscarConfiguracao(Tenant tenant, Long idOdd) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<OddConfiguracao> query = em.createQuery("SELECT o FROM OddConfiguracao o WHERE o.tenant = :tenant AND o.odd.id = :id", OddConfiguracao.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", idOdd);

            OddConfiguracao oddConfiguracao = query.getSingleResult();

            return Optional.ofNullable(oddConfiguracao);
        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
