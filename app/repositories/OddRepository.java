package repositories;


import com.google.inject.Inject;
import models.apostas.Odd;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class OddRepository implements Repository<Long, Odd>{

    JPAApi jpaApi;

    @Inject
    public OddRepository(JPAApi jpaApi) {

        this.jpaApi = jpaApi;
    }

    @Override
    public List<Odd> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Odd as o ");
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

        EntityManager em = jpaApi.em();
        Optional<Odd> oddOptional = buscar(tenant, id);
        if(!oddOptional.isPresent())
            throw new NoResultException("Odd não encontrada");
        Odd odd = oddOptional.get();
        odd.setAbreviacao(o.getAbreviacao());
        odd.setDescricao(o.getAbreviacao());
        odd.setMercado(o.getMercado());
        odd.setNome(o.getNome());
        odd.setPosicao(o.getPosicao());
        odd.setPrioridade(o.getPrioridade());
        odd.setTipoLinha(o.getTipoLinha());
        em.merge(odd);
        return CompletableFuture.completedFuture(odd);
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
}
