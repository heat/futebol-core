package authenticators;

import models.seguranca.Permissao;
import models.seguranca.RegistroAplicativo;
import models.seguranca.Usuario;
import models.vo.Tenant;
import org.joda.time.*;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.AbstractUsernamePasswordAuthenticator;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.password.NopPasswordEncoder;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.util.InitializableWebObject;
import org.pac4j.http.credentials.CredentialUtil;
import play.db.jpa.DefaultJPAApi;
import play.db.jpa.JPAApi;
import play.db.jpa.JPAEntityManagerContext;
import play.inject.ApplicationLifecycle;
import repositories.TenantRepository;
import repositories.UsuarioRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Base64;
import java.util.Optional;

public class SysbetDatabaseAuthenticator extends InitializableWebObject implements Authenticator<SysbetAuthCredential> {

    Provider<JPAApi> jpaApiProvider;

    private PasswordEncoder passwordEncoder = new NopPasswordEncoder();

    @Inject
    public SysbetDatabaseAuthenticator(Provider<JPAApi> jpaApiProvider) {
        this.jpaApiProvider = jpaApiProvider;
    }

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotNull("passwordEncoder", this.passwordEncoder);
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void validate(SysbetAuthCredential credentials, WebContext context) throws HttpAction {

        if (credentials == null) {
            throwsException("No credential");
        }

        String username = credentials.getUsername();
        String password = passwordEncoder.encode(credentials.getPassword());
        String appCode = credentials.getAppCode();

        if (CommonHelper.isBlank(username)) {
            throwsException("Username cannot be blank");
        }
        if (CommonHelper.isBlank(password)) {
            throwsException("Password cannot be blank");
        }

        final JPAApi jpaApi = jpaApiProvider.get();

        CommonProfile p =
        jpaApi.withTransaction(em -> {

        TenantRepository tenantRepository = new TenantRepository(jpaApi);

        Optional<RegistroAplicativo> app = tenantRepository.buscar(appCode);

        if(!app.isPresent()) {
            throwsException("Só é possível se conectar por um aplicativo registrado");
        }

        RegistroAplicativo appRegistrado = app.get();

        if(appRegistrado.isSessao() && CommonHelper.isBlank(credentials.getPin())) {
            throwsException("Não foi possível conectar sem informar o PIN");
        }

        UsuarioRepository usuarioRepository = new UsuarioRepository(jpaApi);
        //TODO registrar conexao somente com tenant ou PIN
        Optional<Usuario> u = usuarioRepository.registro(Tenant.SYSBET, username, password );
        if(!u.isPresent()) {
            throwsException("Usuário não encontrado");
        }

        Usuario usuario = u.get();

        final CommonProfile profile = new CommonProfile();
        profile.setId(usuario.getId());

        profile.addAttribute(Pac4jConstants.USERNAME, username);

        if(appRegistrado.isSessao()) {
            //TODO retirar o tenant de uma consulta
            String appSession = String.format("%s:%d", usuario.getLogin(), usuario.getTenant());
            appSession = new String(Base64.getEncoder().encode(appSession.getBytes()));//CredentialUtil.encryptMD5(appSession);
            profile.addAttribute("app_session", appSession);
        }
        //TODO inserir as demais informacoes do ususario no profile CommonProfile
        profile.addRole(usuario.getPapel().getNome());
        for (Permissao permissao:
                usuario.getPermissoes()) {
            //TODO implementar lista de permissao baseado no escopo
            profile.addPermission(permissao.getNome());
        }
        profile.addAttribute(Tenant.NAME, usuario.getTenant());
            return profile;
        });
        credentials.setUserProfile(p);
    }

    protected void throwsException(final String message) {
        throw new CredentialsException(message);
    }
}