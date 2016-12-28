package repositories;

import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;
import dominio.validadores.Validador;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ValidadorRepository implements Repository<Long, Validador> {


    JPAApi jpaApi;

    @Inject
    public ValidadorRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public List<Validador> todos(Tenant tenant, String regra) {
        List<Validador> validators = todos(tenant);
        return validators.stream().filter( v -> regra.equals(v.getRegra())).collect(Collectors.toList());
    }

    @Override
    public List<Validador> todos(Tenant tenant) {
        EntityManager em = jpaApi.em();
        Query query = em.createQuery("SELECT v FROM Validador v WHERE v.idTenant = :idtenant");
        query.setParameter("idtenant", tenant.get());
        List<Validador> validadores = query.getResultList();
        return validadores;
    }

    @Override
    public Optional<Validador> buscar(Tenant tenant, Long id) {
        return null;
    }

    @Override
    public CompletableFuture<Validador> atualizar(Tenant tenant, Long id, Validador updetable) {
        return null;
    }

    @Override
    public CompletableFuture<Validador> inserir(Tenant tenant, Validador novo) {
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
