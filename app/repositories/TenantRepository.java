package repositories;

import models.seguranca.RegistroAplicativo;
import models.seguranca.Usuario;
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


public class TenantRepository implements Repository<Long,RegistroAplicativo>{

    JPAApi jpaApi;

    @Inject
    public TenantRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<RegistroAplicativo> todos(Tenant tenant) {
        return null;
    }

    @Override
    public Optional<RegistroAplicativo> buscar(Tenant tenant, Long id) {
        return null;
    }

    @Override
    public CompletableFuture<RegistroAplicativo> atualizar(Tenant tenant, Long id, RegistroAplicativo updetable) {
        return null;
    }

    @Override
    public CompletableFuture<RegistroAplicativo> inserir(Tenant tenant, RegistroAplicativo novo) {
        return null;
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }


    public Optional<RegistroAplicativo> buscar(String appKey){
        try {
            EntityManager em = jpaApi.em();
            TypedQuery<RegistroAplicativo> query = em.createQuery("SELECT c FROM RegistroAplicativo c WHERE c.appKey = :appKey ", RegistroAplicativo.class);
            query.setParameter("appKey", appKey);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
