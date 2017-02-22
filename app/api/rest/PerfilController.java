package api.rest;


import com.google.inject.Inject;
import controllers.ApplicationController;
import models.seguranca.Perfil;
import models.seguranca.Usuario;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import repositories.UsuarioRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PerfilController extends ApplicationController {

    UsuarioRepository usuarioRepository;

    @Inject
    public PerfilController(PlaySessionStore playSessionStore, UsuarioRepository usuarioRepository) {
        super(playSessionStore);
        this.usuarioRepository = usuarioRepository;
    }


    @Secure(clients = "headerClient")
    @Transactional
    public Result perfil() {
        Optional<CommonProfile> profile = getProfile();
        CommonProfile commonProfile = profile.get();

        Optional<Usuario> usuario  = usuarioRepository.buscar(getTenant(), Long.parseLong(commonProfile.getId()));

        if (!usuario.isPresent()) {
            return notFound("Usuário não encontrado!");
        }

        Perfil perfil = usuario.get().getPerfil();
        perfil.setNomeUsuario(usuario.get().getLogin());

        Map json = new HashMap();
        json.put("perfil", perfil);

        return ok(Json.toJson(json));
    }

}
