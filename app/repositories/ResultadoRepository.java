package repositories;

import models.eventos.Resultado;
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


public class ResultadoRepository implements Repository<Long, Resultado> {

    JPAApi jpaApi;

    @Inject
    public ResultadoRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Resultado> todos(Tenant tenant) {
        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Resultado as r WHERE r.tenant = :tenant");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    @Override
    public Optional<Resultado> buscar(Tenant tenant, Long id) {
        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Resultado> query = em.createQuery("SELECT r FROM Resultado r WHERE r.tenant = :tenant and r.id = :id", Resultado.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.ofNullable(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CompletableFuture<Resultado> atualizar(Tenant tenant, Long id, Resultado result) {

        return CompletableFuture.completedFuture(result);
    }

    @Override
    public CompletableFuture<Resultado> inserir(Tenant tenant, Resultado resultado) {

        EntityManager em = jpaApi.em();
        resultado.setTenant(tenant.get());
        em.persist(resultado);
        return CompletableFuture.completedFuture(resultado);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {

        EntityManager em = jpaApi.em();
        Optional<Resultado> resultado = buscar(tenant, id);
        if(!resultado.isPresent()){
            throw new NoResultException("Resultado não encontrado");
        }
        Resultado r = resultado.get();
        em.remove(r);

        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }
}
