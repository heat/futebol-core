package repositories;

import models.Importacao.Importacao;
import models.eventos.Time;
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
        return null;
    }

    public Optional<Importacao> buscar(Tenant tenant, String chave) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Importacao> query = em.createQuery("SELECT c FROM Importacao c WHERE c.tenant = :tenant and c.chave = :chave ", Importacao.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("chave", chave);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Importacao> atualizar(Tenant tenant, Long id, Importacao updetable) {
        return null;
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


}
