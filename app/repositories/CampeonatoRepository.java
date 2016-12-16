package repositories;

import models.eventos.Campeonato;
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

public class CampeonatoRepository implements Repository<Long, Campeonato> {

    JPAApi jpaApi;

    @javax.inject.Inject
    public CampeonatoRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Campeonato> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Campeonato as c WHERE c.tenant = :tenant");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    //Tratar exceção
    @Override
    public Optional<Campeonato> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Campeonato> query = em.createQuery("SELECT c FROM Campeonato c WHERE c.tenant = :tenant and c.id = :id", Campeonato.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());

        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CompletableFuture<Campeonato> atualizar(Tenant tenant, Long id, Campeonato updetable) {
        return null;
    }

    @Override
    public CompletableFuture<Campeonato> inserir(Tenant tenant, Campeonato campeonato) {

        EntityManager em = jpaApi.em();
        campeonato.setTenant(tenant.get());
        em.persist(campeonato);

        return CompletableFuture.completedFuture(campeonato);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {

        EntityManager em = jpaApi.em();
        Optional<Campeonato> campeonato = buscar(tenant, id);
        Campeonato c = campeonato.get();
        em.remove(c);

        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }
}
