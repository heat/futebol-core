package repositories;


import com.google.inject.Inject;
import models.apostas.EventoAposta;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class EventoApostaRepository implements Repository<Long, EventoAposta>{

    JPAApi jpaApi;

    @Inject
    public EventoApostaRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<EventoAposta> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM EventoAposta as ea WHERE ea.tenant = :tenant");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    @Override
    public Optional<EventoAposta> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<EventoAposta> query = em.createQuery("SELECT ea FROM EventoAposta ea WHERE ea.tenant = :tenant and ea.id = :id",
                                                                EventoAposta.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<EventoAposta> atualizar(Tenant tenant, Long id, EventoAposta ea) {
        EntityManager em = jpaApi.em();
        Optional<EventoAposta> eventoApostaOptional = buscar(tenant, id);
        if(!eventoApostaOptional.isPresent()){
            throw new NoResultException("Aposta não encontrada");
        }
        EventoAposta eventoAposta = eventoApostaOptional.get();
        eventoAposta.setPermitir(ea.isPermitir());
        eventoAposta.setTaxas(ea.getTaxas());
        em.merge(eventoAposta);
        return CompletableFuture.completedFuture(eventoAposta);
    }

    @Override
    public CompletableFuture<EventoAposta> inserir(Tenant tenant, EventoAposta eventoAposta) {
        EntityManager em = jpaApi.em();
        eventoAposta.setTenant(tenant.get());
        em.persist(eventoAposta);

        return CompletableFuture.completedFuture(eventoAposta);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        EntityManager em = jpaApi.em();
        Optional<EventoAposta> eventoApostaOptional = buscar(tenant, id);
        if(!eventoApostaOptional.isPresent()){
            throw new NoResultException("Aposta não encontrada");
        }
        else{
            EventoAposta eventoAposta = eventoApostaOptional.get();
            em.remove(eventoAposta);
        }
        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }
}
