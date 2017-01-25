package api.rest;


import com.google.inject.Inject;
import controllers.ApplicationController;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.libs.Json;
import play.mvc.Result;

import java.util.Optional;

public class PerfilController extends ApplicationController {

    @Inject
    public PerfilController(PlaySessionStore playSessionStore) {
        super(playSessionStore);
    }

    @Secure(clients = "headerClient")
    public Result perfil() {
        Optional<CommonProfile> profile = getProfile();
        CommonProfile commonProfile = profile.get();
        return ok(Json.toJson(commonProfile));
    }

}
