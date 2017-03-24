package repositories;

import models.bilhetes.Palpite;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
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

        return null;
    }

    @Override
    public Optional<Palpite> buscar(Tenant tenant, Long id) {

        return Optional.empty();
    }

    @Override
    public CompletableFuture<Palpite> atualizar(Tenant tenant, Long id, Palpite p) {

        return CompletableFuture.completedFuture(p);
    }

    @Override
    public CompletableFuture<Palpite> inserir(Tenant tenant, Palpite palpite) {

        EntityManager em = jpaApi.em();
        em.persist(palpite);

        return CompletableFuture.completedFuture(palpite);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) throws NoResultException{

        EntityManager em = jpaApi.em();
        Optional<Palpite> palpite = buscar(tenant, id);
        if(!palpite.isPresent()){
            throw new NoResultException("Palpite não encontrado");
        }
        else{
            Palpite c = palpite.get();
            em.remove(c);
        }

        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }

    public List<Palpite> buscarPorTaxas(Tenant tenant, List<Long> taxas) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("SELECT p FROM Palpite WHERE p.tenant = :tenant AND p.taxa IN :taxas");
        query.setParameter("tenant", tenant);
        query.setParameter("taxas", taxas);

        return query.getResultList();

    }
}
