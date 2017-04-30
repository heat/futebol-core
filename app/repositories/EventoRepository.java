package repositories;

import models.eventos.Campeonato;
import models.eventos.Evento;
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

public class EventoRepository implements Repository<Long, Evento>{

    JPAApi jpaApi;

    @Inject
    public EventoRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Evento> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Evento as e WHERE e.tenant = :tenant");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    @Override
    public Optional<Evento> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Evento> query = em.createQuery("SELECT e FROM Evento e WHERE e.tenant = :tenant and e.id = :id", Evento.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Evento> atualizar(Tenant tenant, Long id, Evento e) {

        EntityManager em = jpaApi.em();
        if(em.contains(e)) {
            em.persist(e);
            return CompletableFuture.completedFuture(e);
        }
        Optional<Evento> eventoOptional = buscar(tenant, id);
        if(!eventoOptional.isPresent())
            throw new NoResultException("Evento não encontrado");
        Evento evento = eventoOptional.get();
        evento.setDataEvento(e.getDataEvento());
        em.merge(evento);
        return CompletableFuture.completedFuture(evento);
    }

    public CompletableFuture<Evento> finalizar(Tenant tenant, Long id, Evento e) {

        EntityManager em = jpaApi.em();
        Optional<Evento> eventoOptional = buscar(tenant, id);
        if(!eventoOptional.isPresent())
            throw new NoResultException("Evento não encontrado");
        Evento evento = eventoOptional.get();
        evento.setSituacao(Evento.Situacao.E);
        em.merge(e);
        return CompletableFuture.completedFuture(e);
    }

    @Override
    public CompletableFuture<Evento> inserir(Tenant tenant, Evento evento) {

        EntityManager em = jpaApi.em();
        evento.setTenant(tenant.get());
        evento.setResultados(null);
        if(!em.contains(evento.getCasa()))
        evento.setCasa(em.find(Time.class, evento.getCasa().getId()));
        if(!em.contains(evento.getFora()))
        evento.setFora(em.find(Time.class, evento.getFora().getId()));
        if(!em.contains(evento.getCampeonato()))
        evento.setCampeonato(em.find(Campeonato.class, evento.getCampeonato().getId()));
        em.persist(evento);
        return CompletableFuture.completedFuture(evento);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {

        EntityManager em = jpaApi.em();
        Optional<Evento> evento = buscar(tenant, id);
        if(!evento.isPresent())
            throw new NoResultException("Evento não encontrado");

        Evento ev = evento.get();
        ev.setSituacao(Evento.Situacao.D);
        em.merge(ev);

        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }

}
