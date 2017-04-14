package repositories;

import filters.FiltroBilhete;
import models.bilhetes.Bilhete;
import models.financeiro.comissao.ComissaoBilhete;
import models.seguranca.Usuario;
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

public class BilheteRepository implements Repository<Long, Bilhete> {

    JPAApi jpaApi;
    ContaRepository contaRepository;

    @Inject
    public BilheteRepository(JPAApi jpaApi, ContaRepository contaRepository) {

        this.jpaApi = jpaApi;
        this.contaRepository = contaRepository;
    }

    @Override
    public List<Bilhete> todos(Tenant tenant) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Bilhete as b WHERE b.tenant = :tenant");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    public List<Bilhete> todos(Tenant tenant, Usuario usuario, FiltroBilhete filtro) {

        EntityManager em = jpaApi.em();
        /*
        * Todos os bilhetes se for administrador
        * e apenas do usuário caso contrário
        * 1L -> Papel de administrador
        * */
        Query query = em.createQuery("FROM Bilhete as b WHERE b.tenant = :tenant " +
                " and (:papel = 1L or b.usuario.id = :usuario) " +
                " and (FUNCTION('to_char', b.criadoEm, 'YYYY-MM-DD') between :inicio and :termino or :inicio = null) " +
                " AND EXISTS (SELECT p FROM Palpite p WHERE p.taxa.eventoAposta = :evento or :evento = 0) ");
        query.setParameter("tenant", tenant.get());
        query.setParameter("papel", usuario.getPapel().getId());
        query.setParameter("usuario", usuario.getId());
        query.setParameter("inicio", filtro.inicio);
        query.setParameter("termino", filtro.termino);
        query.setParameter("evento", filtro.evento);

        return query.getResultList();
    }

    public Optional<Bilhete> buscar(Tenant tenant, String codigo) {
        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Bilhete> query = em.createQuery("SELECT b FROM Bilhete b WHERE b.tenant = :tenant and b.codigo = :codigo", Bilhete.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("codigo", codigo);

            Bilhete bilhete = query.getSingleResult();

            List<ComissaoBilhete> comissoes = contaRepository.buscarComissaoBilhete(tenant, bilhete.getId());
            bilhete.setComissoes(comissoes);

            return Optional.ofNullable(bilhete);
        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public Optional<Bilhete> buscar(Tenant tenant, Long id) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Bilhete> query = em.createQuery("SELECT b FROM Bilhete b WHERE b.tenant = :tenant and b.id = :id", Bilhete.class);
            query.setParameter("tenant", tenant.get());
            query.setParameter("id", id);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Bilhete> atualizar(Tenant tenant, Long id, Bilhete b) {

        EntityManager em = jpaApi.em();
        Optional<Bilhete> bilhete = buscar(tenant, id);
        if(!bilhete.isPresent())
            throw new NoResultException("Bilhete não encontrado");
        Bilhete blt = bilhete.get();
        blt.setAlteradoEm(b.getAlteradoEm());
        blt.setSituacao(b.getSituacao());
        em.merge(blt);
        return CompletableFuture.completedFuture(blt);
    }

    @Override
    public CompletableFuture<Bilhete> inserir(Tenant tenant, Bilhete bilhete) {

        EntityManager em = jpaApi.em();
        bilhete.setTenant(tenant.get());
        em.persist(bilhete);
        return CompletableFuture.completedFuture(bilhete);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) throws NoResultException{

        EntityManager em = jpaApi.em();
        Optional<Bilhete> bilheteOptional = buscar(tenant, id);
        if(!bilheteOptional.isPresent())
            throw new NoResultException("Bilhete não encontrado");
        Bilhete bilhete = bilheteOptional.get();
        bilhete.setSituacao(Bilhete.Situacao.C);
        em.merge(bilhete);
        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);
    }

    public List<Bilhete> todosPorPalpites(Tenant tenant, Long evento) {

        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Bilhete as b WHERE b.tenant = :tenant " +
                " AND EXISTS (SELECT p FROM Palpite p WHERE p.bilhete = b.id AND p.taxa.eventoAposta = :evento) ");
        query.setParameter("tenant", tenant.get());
        query.setParameter("evento", evento);

        return query.getResultList();
    }
}
