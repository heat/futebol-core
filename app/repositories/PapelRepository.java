package repositories;

import filters.Paginacao;
import models.eventos.Time;
import models.seguranca.Papel;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;
import services.PaginacaoService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public class PapelRepository implements  Repository<Long, Papel>{

    JPAApi jpaApi;

    @Inject
    public PapelRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Papel> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Papel as t");
        return query.getResultList();
    }

    @Override
    public Optional<Papel> buscar(Tenant tenant, Long id) {

        return null;
    }

    @Override
    public CompletableFuture<Papel> atualizar(Tenant tenant, Long id, Papel updetable) {
        return null;
    }

    @Override
    public CompletableFuture<Papel> inserir(Tenant tenant, Papel novo) {
        return null;
    }


    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }

    public Optional<Papel> buscar(Tenant tenant, String nome) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Papel> query = em.createQuery("SELECT p FROM Papel p WHERE UPPER(p.nome) = UPPER(:nome)", Papel.class);
            query.setParameter("nome", nome);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


}
