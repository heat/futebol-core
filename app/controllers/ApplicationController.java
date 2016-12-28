package controllers;

import models.vo.Tenant;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import play.mvc.Controller;

import java.util.Optional;

/**
 * Controlador geral com alguns métodos comuns aos controladores que dependem de autorização
 */
public abstract class ApplicationController extends Controller {

    private SessionStore<PlayWebContext> playSessionStore;

    public ApplicationController(SessionStore<PlayWebContext> playSessionStore) {
        this.playSessionStore = playSessionStore;
    }

    protected Tenant getTenant() {

        Optional<CommonProfile> commonProfile = getProfile();
        CommonProfile profile = commonProfile.get();
        return Tenant.of((Long) profile.getAttribute("TENANT_ID"));
    }

    protected Optional<CommonProfile> getProfile() {
        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        return profileManager.get(true);
    }
}
