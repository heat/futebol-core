package repositories;

import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;
import validators.Validator;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ValidatorRepository implements Repository<Long, Validator> {


    JPAApi jpaApi;

    @Inject
    public ValidatorRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public List<Validator> todos(Tenant tenant, String regra) {
        List<Validator> validators = todos(tenant);
        return validators.stream().filter( v -> regra.equals(v.getRegra())).collect(Collectors.toList());
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
    public Optional<Validator> buscar(Tenant tenant, Long id) {
        return null;
    }

    @Override
    public CompletableFuture<Validator> atualizar(Tenant tenant, Long id, Validator updetable) {
        return null;
    }

    @Override
    public CompletableFuture<Validator> inserir(Tenant tenant, Validator novo) {
        EntityManager em = jpaApi.em();
        novo.setIdTenant(tenant.get());
        em.persist(novo);
        return CompletableFuture.completedFuture(novo);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }
}
