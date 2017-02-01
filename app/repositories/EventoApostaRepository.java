package repositories;


import com.google.inject.Inject;
import models.apostas.EventoAposta;
import models.apostas.Taxa;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.Logger;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 *
 * TODO: verificar padrao de utilizacao do log e como utilizar
 * https://www.playframework.com/documentation/2.5.x/JavaLogging#toolbar
 */
public class EventoApostaRepository implements Repository<Long, EventoAposta>{

    JPAApi jpaApi;

    final Logger.ALogger logger = Logger.of(this.getClass());

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
        if(!eventoApostaOptional.isPresent())
            throw new NoResultException("Aposta não encontrada");

        EventoAposta eventoAposta = eventoApostaOptional.get();
        eventoAposta.setPermitir(ea.isPermitir());
        eventoAposta.setTaxas(ea.getTaxas());
        em.merge(eventoAposta);
        logger.info("atualização: eventoAposta={}", eventoAposta);
        return CompletableFuture.completedFuture(eventoAposta);
    }

    @Override
    public CompletableFuture<EventoAposta> inserir(Tenant tenant, EventoAposta eventoAposta) {

        EntityManager em = jpaApi.em();
        eventoAposta.setTenant(tenant.get());
        em.persist(eventoAposta);
        logger.info("criacao: eventoAposta={}", eventoAposta);
        return CompletableFuture.completedFuture(eventoAposta);
    }

    public CompletableFuture<EventoAposta> inserirTaxa(Tenant tenant, EventoAposta eventoAposta) {

        EntityManager em = jpaApi.em();
        em.merge(eventoAposta);
        return CompletableFuture.completedFuture(eventoAposta);
    }

    /**
     * Um evento aposta nunca é excluido. Ele é apenas cancelado.
     * @param tenant
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {

        EntityManager em = jpaApi.em();
        Optional<EventoAposta> eventoApostaOptional = buscar(tenant, id);
        if(!eventoApostaOptional.isPresent())
            throw new NoResultException("Aposta não encontrada");
        EventoAposta eventoAposta = eventoApostaOptional.get();
        eventoAposta.setSituacao(EventoAposta.Situacao.C);
        em.merge(eventoAposta);
        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }

    public Optional<List<Palpite>> buscarPalpites(Tenant tenant, Long idEventoAposta){

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Palpite> query =
                    em.createQuery( "SELECT palpites " +
                                    "FROM EventoAposta eventoAposta " +
                                    "JOIN eventoAposta.taxas as taxas " +
                                    "JOIN taxas.palpites as palpites "+
                                    "WHERE ea.tenant = :tenant and ea.id = :id",
                                    Palpite.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", idEventoAposta);

            return Optional.ofNullable(query.getResultList());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<List<Taxa>> buscarTaxas(Tenant tenant, Long idEventoAposta){

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Taxa> query =
                    em.createQuery( "SELECT palpites " +
                                    "FROM EventoAposta eventoAposta " +
                                    "JOIN eventoAposta.taxas as taxas " +
                                    "WHERE ea.tenant = :tenant and ea.id = :id",
                                    Taxa.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", idEventoAposta);

            return Optional.ofNullable(query.getResultList());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<List<Bilhete>> buscarBilhetesPorEvento(Tenant tenant, Long idEventoAposta){

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Bilhete> query =
                    em.createQuery( "SELECT b " +
                                    "FROM Bilhete b " +
                                    "JOIN b.palpites as p " +
                                    "WHERE p.eventoAposta.id = :idEventoAposta and b.tenant = :tenant and p.tenant = :tenant and p.eventoAposta.tenant = :tenant",
                            Bilhete.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", idEventoAposta);

            return Optional.ofNullable(query.getResultList());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Long> buscarIdBilhetePorEvento(Tenant tenant, Long idEventoAposta){

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Long> query =
                    em.createQuery( "SELECT b.id " +
                                    "FROM Bilhete b " +
                                    "JOIN b.palpites as p " +
                                    "WHERE p.eventoAposta.id = :idEventoAposta and b.tenant = :tenant and p.tenant = :tenant and p.eventoAposta.tenant = :tenant",
                            Long.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", idEventoAposta);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
