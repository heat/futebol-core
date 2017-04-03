package repositories;

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

import static models.seguranca.Usuario.Status.CANCELADO;

public class UsuarioRepository implements Repository<Long,Usuario>{

    JPAApi jpaApi;

    @Inject
    public UsuarioRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public List<Usuario> todos(Tenant tenant) {
        EntityManager em = jpaApi.em();
        Query query = em.createQuery("FROM Usuario as u WHERE u.tenant = :tenant ORDER BY u.login ");
        query.setParameter("tenant", tenant.get());
        return query.getResultList();
    }

    @Override
    public Optional<Usuario> buscar(Tenant tenant, Long id) {
        try {
            EntityManager em = jpaApi.em();
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.tenant = :tenant and u.id = :id", Usuario.class);
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
    public CompletableFuture<Usuario> atualizar(Tenant tenant, Long id, Usuario us) {

        EntityManager em = jpaApi.em();
        Optional<Usuario> usuario = buscar(tenant, id);
        if(!usuario.isPresent())
            throw new NoResultException("Usuário não encontrado!");
        Usuario usuarioEntity = usuario.get();
        usuarioEntity.setPerfil(us.getPerfil());
        usuarioEntity.setPapel(us.getPapel());
        usuarioEntity.setSenha(us.getSenha());
        em.merge(usuarioEntity);
        return CompletableFuture.completedFuture(usuarioEntity);
    }


    public CompletableFuture<Usuario> atualizarUsuarioBilhete(Tenant tenant, Long id, Usuario us) {

        EntityManager em = jpaApi.em();
        Optional<Usuario> usuario = buscar(tenant, id);
        if(!usuario.isPresent())
            throw new NoResultException("Usuário não encontrado!");
        Usuario usuarioEntity = usuario.get();
        em.merge(usuarioEntity);
        return CompletableFuture.completedFuture(usuarioEntity);
    }

    @Override
    public CompletableFuture<Usuario> inserir(Tenant tenant, Usuario novo) {
        EntityManager em = jpaApi.em();
        novo.setTenant(tenant.get());
        em.persist(novo);
        return CompletableFuture.completedFuture(novo);
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        EntityManager em = jpaApi.em();
        Optional<Usuario> usuarioOptional = buscar(tenant, id);
        if(!usuarioOptional.isPresent())
            throw new NoResultException("Usuário não encontrado");
        Usuario usuario = usuarioOptional.get();
        usuario.setStatus(CANCELADO);
        em.merge(usuario);
        return CompletableFuture.completedFuture(Confirmacao.CONCLUIDO);

    }



    public Optional<Usuario> registro(Tenant tenant, String login, String senha) {
        try {
            return jpaApi.withTransaction((em) -> {

                TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.tenant = :tenant and u.login = :login AND u.senha = :senha", Usuario.class);
                query.setParameter("tenant", tenant.get());
                query.setParameter("login", login);
                query.setParameter("senha", senha);

                return Optional.of(query.getSingleResult());
            });
        } catch (NoResultException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
