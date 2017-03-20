package repositories;

import models.eventos.Campeonato;
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

public class CampeonatoRepository implements Repository<Long, Campeonato> {

    JPAApi jpaApi;

    @Inject
    public CampeonatoRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Campeonato> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Campeonato as c WHERE c.tenant = :tenant AND c.situacao = 'A' ");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    public List<Campeonato> todos(Tenant tenant, String nome) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Campeonato as c WHERE c.tenant = :tenant AND (c.nome = :nome or :nome IS NULL) AND c.situacao = 'A' " +
                                     " AND c.id IN (SELECT e.campeonato.id FROM Evento e WHERE e.situacao = 'A' AND e.dataEvento > current_timestamp) " +
                                     " ORDER BY c.nome ");

        query.setParameter("tenant", tenant.get());
        query.setParameter("nome", nome);
        return query.getResultList();
    }

    @Override
    public Optional<Campeonato> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Campeonato> query = em.createQuery("SELECT c FROM Campeonato c WHERE c.tenant = :tenant and c.id = :id AND c.situacao = 'A' ", Campeonato.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Campeonato> buscar(Tenant tenant, String nome) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Campeonato> query = em.createQuery("SELECT c FROM Campeonato c WHERE c.tenant = :tenant and c.nome = :nome AND c.situacao = 'A'", Campeonato.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("nome", nome);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Campeonato> atualizar(Tenant tenant, Long id, Campeonato c) {

        EntityManager em = jpaApi.em();
        Optional<Campeonato> campeonato = buscar(tenant, id);
        if(!campeonato.isPresent())
            throw new NoResultException("Campeonato não encontrado");
        Campeonato cp = campeonato.get();
        cp.setNome(c.getNome());
        em.merge(cp);
        return CompletableFuture.completedFuture(c);
    }

    @Override
    public CompletableFuture<Campeonato> inserir(Tenant tenant, Campeonato campeonato) {


        Optional<Campeonato> t = this.buscar(tenant, campeonato.getNome());

        if (!t.isPresent()){
            EntityManager em = jpaApi.em();
            campeonato.setTenant(tenant.get());
            em.persist(campeonato);
            return CompletableFuture.completedFuture(campeonato);
        }

        Campeonato campeonatoPresent = t.get();
        campeonatoPresent.setSituacao(Campeonato.Situacao.A);

        return CompletableFuture.completedFuture(campeonatoPresent);

    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) throws NoResultException{

        EntityManager em = jpaApi.em();
        Optional<Campeonato> campeonato = buscar(tenant, id);
        if(!campeonato.isPresent())
            throw new NoResultException("Campeonato não encontrado");

        Campeonato camp = campeonato.get();
        camp.setSituacao(Campeonato.Situacao.I);
        em.merge(camp);

        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }
}
