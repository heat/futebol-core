package repositories;

import models.bilhetes.Pin;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PinRepository implements Repository<Long, Pin> {

    JPAApi jpaApi;

    @Inject
    public PinRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Pin> todos(Tenant tenant) {
        return null;
    }

    @Override
    public Optional<Pin> buscar(Tenant tenant, Long id) {
        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Pin> query = em.createQuery("SELECT p FROM Pin p WHERE p.tenant = :tenant and p.id = :id AND p.expiraEm > CURRENT_TIMESTAMP ", Pin.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Pin> atualizar(Tenant tenant, Long id, Pin updetable) {
        return null;
    }

    @Override
    public CompletableFuture<Pin> inserir(Tenant tenant, Pin pin) {
        EntityManager em = jpaApi.em();
        pin.setTenant(tenant.get());
        em.persist(pin);
        return CompletableFuture.completedFuture(pin);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }
}
