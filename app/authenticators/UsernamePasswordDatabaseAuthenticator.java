package authenticators;

import models.seguranca.Permissao;
import models.seguranca.Usuario;
import models.vo.Tenant;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.AbstractUsernamePasswordAuthenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import play.db.jpa.DefaultJPAApi;
import play.db.jpa.JPAApi;
import play.db.jpa.JPAEntityManagerContext;
import play.inject.ApplicationLifecycle;
import repositories.UsuarioRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

public class UsernamePasswordDatabaseAuthenticator extends AbstractUsernamePasswordAuthenticator{

    Provider<JPAApi> jpaApiProvider;

    @Inject
    public UsernamePasswordDatabaseAuthenticator(Provider<JPAApi> jpaApiProvider) {
        this.jpaApiProvider = jpaApiProvider;
    }

    @Override
    public void validate(UsernamePasswordCredentials credentials, WebContext context) throws HttpAction {

        if (credentials == null) {
            throwsException("No credential");
        }

        String username = credentials.getUsername();
        String password = credentials.getPassword();

        if (CommonHelper.isBlank(username)) {
            throwsException("Username cannot be blank");
        }
        if (CommonHelper.isBlank(password)) {
            throwsException("Password cannot be blank");
        }

        JPAApi jpaApi = jpaApiProvider.get();

        Optional<Usuario> u = Optional.empty();
        UsuarioRepository usuarioRepository = new UsuarioRepository(jpaApi);
        u = usuarioRepository.registro(Tenant.SYSBET, username, password );
        if(!u.isPresent()) {
            throwsException("Usuário não encontrado");
        }
        Usuario usuario = u.get();
        final CommonProfile profile = new CommonProfile();
        profile.setId(usuario.getId());

        profile.addAttribute(Pac4jConstants.USERNAME, username);
        profile.addRole(usuario.getPapel().getNome());
        for (Permissao p:
                usuario.getPermissoes()) {
            profile.addPermission(p.getNome());
        }
        profile.addAttribute("TENANT_ID", usuario.getIdTenant());
        credentials.setUserProfile(profile);
    }

    protected void throwsException(final String message) {
        throw new CredentialsException(message);
    }
}