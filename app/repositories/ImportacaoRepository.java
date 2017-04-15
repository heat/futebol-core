package repositories;

import models.Importacao.Importacao;
import models.eventos.Time;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public class ImportacaoRepository implements  Repository<Long, Importacao>{

    JPAApi jpaApi;

    @Inject
    public ImportacaoRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Importacao> todos(Tenant tenant) {
        return null;
    }

    @Override
    public Optional<Importacao> buscar(Tenant tenant, Long id) {
        EntityManager em = jpaApi.em();
        try {
            TypedQuery<Importacao> query = em.createQuery("SELECT i FROM Importacao i WHERE i.tenant = :tenant AND i.id = :id", Importacao.class);
            return Optional.of(query.getSingleResult());

        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Importacao> buscar(Tenant tenant, String chave) {
        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Importacao> query = em.createQuery("SELECT b FROM Importacao b WHERE b.tenant = :tenant and b.chave = :chave", Importacao.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("chave", chave);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Importacao> atualizar(Tenant tenant, Long id, Importacao updetable) {

        EntityManager em = jpaApi.em();
        updetable.setId(id);
        updetable.setTenant(tenant.get());
        em.merge(updetable);
        return CompletableFuture.completedFuture(updetable);
    }

    @Override
    public CompletableFuture<Importacao> inserir(Tenant tenant, Importacao novo) {
        EntityManager em = jpaApi.em();
        novo.setTenant(tenant.get());
        em.persist(novo);
        return CompletableFuture.completedFuture(novo);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }


    public boolean contains(Importacao importacao) {
        EntityManager em = jpaApi.em();
        if (em.contains(importacao))
            return true;
        if(!Objects.isNull(importacao.getId()))
            return true;

        return false;
    }
}
