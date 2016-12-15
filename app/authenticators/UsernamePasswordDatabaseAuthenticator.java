package authenticators;

import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.AbstractUsernamePasswordAuthenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import repositories.UsuarioRepository;

import javax.inject.Inject;

public class UsernamePasswordDatabaseAuthenticator extends AbstractUsernamePasswordAuthenticator{

    @Inject
    UsuarioRepository rep;

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


        if(rep.validarLoginSenha(username, password) == null){
            throwsException("Login e/ou senha inválido(s)");
        }

        final CommonProfile profile = new CommonProfile();
        profile.setId(username);
        profile.addAttribute(Pac4jConstants.USERNAME, username);
        profile.addPermission("eventos.visualizar");
        profile.addRole("ADMINISTRADOR");
        credentials.setUserProfile(profile);
    }

    protected void throwsException(final String message) {
        throw new CredentialsException(message);
    }
}