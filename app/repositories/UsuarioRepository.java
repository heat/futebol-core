package repositories;

import models.seguranca.Usuario;
import models.vo.Confirmacao;
import models.vo.Tenant;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by nuvemhost on 15/12/2016.
 */
public class UsuarioRepository implements Repository<Long,Usuario>{

    @Inject
    JPAApi jpaApi;

    @Override
    public List<Usuario> todos(Tenant tenant) {
        return null;
    }

    @Override
    public Optional<Usuario> registro(Tenant tenant, Long id) {
        return null;
    }

    @Override
    public CompletableFuture<Usuario> atualizar(Tenant tenant, Long id, Usuario updetable) {
        return null;
    }

    @Override
    public CompletableFuture<Usuario> inserir(Tenant tenant, Long id, Usuario novo) {
        return null;
    }

    @Override
    public CompletableFuture<Confirmacao> excluir(Tenant tenant, Long id) {
        return null;
    }

    public Usuario validarLoginSenha(String login, String senha) {

        try {
            return jpaApi.withTransaction((em) -> {

                Query query = em.createNamedQuery("validarLoginSenha", Usuario.class);
                query.setParameter("login", login);
                query.setParameter("senha", senha);
                return (Usuario) query.getSingleResult();
            });
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
