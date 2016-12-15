package api.rest;

import modules.SecurityModule;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.jwt.profile.JwtProfile;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class UsuarioRest extends Controller {

    private final PlaySessionStore playSessionStore;

    @Inject
    public UsuarioRest(PlaySessionStore playSessionStore) {
        this.playSessionStore = playSessionStore;
    }

    @Secure(clients = "DirectBasicAuthClient")
    public Result authenticar() {
        final Optional<CommonProfile> profile = getProfile();
        final JwtGenerator generator = new JwtGenerator(new SecretSignatureConfiguration(SecurityModule.JWT_SALT));
        String token = "";
        if (profile.isPresent()) {
            token = generator.generate(profile.get());
            System.out.println(profile.get().toString());
        }
        return ok("token:" + token);
    }

    @Secure(clients = "headerClient")
    public Result usuario() {
        Optional<CommonProfile> profile = getProfile();
        CommonProfile commonProfile = profile.get();

        return ok("teste" +
                commonProfile.getClass());
    }

    private Optional<CommonProfile> getProfile() {
        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        return profileManager.get(true);
    }


}
