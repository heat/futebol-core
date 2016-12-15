package repositories;

import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;
import validators.Validator;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ValidatorRepository implements Repository<Long, Validator> {


    JPAApi jpaApi;

    @Inject
    public ValidatorRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Validator> todos(Tenant tenant) {
        EntityManager em = jpaApi.em();
        Query query = em.createQuery("SELECT v FROM Validator v WHERE v.idTenant = :idtenant");
        query.setParameter("idtenant", tenant.get());
        List<Validator> validadores = query.getResultList();
        return validadores;
    }

    @Override
    public Optional<Validator> registro(Tenant tenant, Long id) {
        return null;
    }

    @Override
    public CompletableFuture<Validator> atualizar(Tenant tenant, Long id, Validator updetable) {
        return null;
    }

    @Override
    public CompletableFuture<Validator> inserir(Tenant tenant, Validator novo) {
        EntityManager em = jpaApi.em();
        em.persist(novo);
        return CompletableFuture.completedFuture(novo);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }
}
