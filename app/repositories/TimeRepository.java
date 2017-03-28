package repositories;

import models.eventos.Time;
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


public class TimeRepository implements  Repository<Long, Time>{

    JPAApi jpaApi;

    @Inject
    public TimeRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Time> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Time as t WHERE t.tenant = :tenant");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    public List<Time> todos(Tenant tenant, String nome) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Time as t WHERE t.tenant = :tenant AND (UPPER(t.nome) LIKE UPPER(:nome) or :nome IS NULL) AND t.situacao = 'A' ORDER BY t.nome");
        query.setParameter("tenant", tenant.get());
        query.setParameter("nome", "%" + nome + "%");
        return query.getResultList();
    }

    @Override
    public Optional<Time> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Time> query = em.createQuery("SELECT t FROM Time t WHERE t.tenant = :tenant and t.id = :id", Time.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Time> buscar(Tenant tenant, String nome) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Time> query = em.createQuery("SELECT t FROM Time t WHERE t.tenant = :tenant and t.nome = :nome", Time.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("nome", nome);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Time> atualizar(Tenant tenant, Long id, Time t) {

        EntityManager em = jpaApi.em();
        Optional<Time> time = buscar(tenant, id);
        if(!time.isPresent())
            throw new NoResultException("Time não encontrado");
        Time tm = time.get();
        tm.setNome(t.getNome());
        em.merge(tm);
        return CompletableFuture.completedFuture(tm);
    }

    @Override
    public CompletableFuture<Time> inserir(Tenant tenant, Time time) {

        Optional<Time> t = this.buscar(tenant, time.getNome());

        if (!t.isPresent()){
            EntityManager em = jpaApi.em();
            time.setTenant(tenant.get());
            em.persist(time);
            return CompletableFuture.completedFuture(time);
        }

        Time timePresent = t.get();
        timePresent.setSituacao(Time.Situacao.A);

        return CompletableFuture.completedFuture(timePresent);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {

        EntityManager em = jpaApi.em();
        Optional<Time> time = buscar(tenant, id);
        if(!time.isPresent())
            throw new NoResultException("Time não encontrado");
        em.remove(time.get());
        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }
}
