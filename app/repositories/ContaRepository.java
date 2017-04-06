package repositories;


import com.google.inject.Inject;
import models.apostas.Odd;
import models.financeiro.Conta;
import models.financeiro.SolicitacaoSaldo;
import models.financeiro.comissao.Comissao;
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
import java.util.concurrent.CompletionStage;

public class ContaRepository implements Repository<Long, Conta>{

    JPAApi jpaApi;

    @Inject
    public ContaRepository(JPAApi jpaApi) {

        this.jpaApi = jpaApi;
    }

    @Override
    public List<Conta> todos(Tenant tenant) {
        return null;
    }

    @Override
    public Optional<Conta> buscar(Tenant tenant, Long idUsuario) {

        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Conta> query = em.createQuery("SELECT c FROM Conta c WHERE c.proprietario.id = :idUsuario ", Conta.class);
            query.setParameter("idUsuario", idUsuario);
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public CompletableFuture<Conta> atualizar(Tenant tenant, Long id, Conta updetable) {
        return null;
    }

    public void atualizar(Conta updetable) {

        EntityManager em = jpaApi.em();
        em.merge(updetable);
    }

    @Override
    public CompletableFuture<Conta> inserir(Tenant tenant, Conta novo) {

        EntityManager em = jpaApi.em();
        em.persist(novo);
        return CompletableFuture.completedFuture(novo);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }

    public CompletableFuture<Comissao> inserirComissao(Comissao comissao) {

        EntityManager em = jpaApi.em();
        em.persist(comissao);
        return CompletableFuture.completedFuture(comissao);
    }

    public CompletableFuture<SolicitacaoSaldo> inserirSolicitacaoSaldo(SolicitacaoSaldo solicitacaoSaldo) {

        EntityManager em = jpaApi.em();
        em.persist(solicitacaoSaldo);
        return CompletableFuture.completedFuture(solicitacaoSaldo);

    }
}
