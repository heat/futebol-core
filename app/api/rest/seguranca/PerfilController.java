package api.rest.seguranca;


import actions.TenantAction;
import api.json.ObjectJson;
import api.json.PerfilJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.usuarios.PerfilAtualizarProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.seguranca.Perfil;
import models.seguranca.Usuario;
import models.vo.Chave;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import repositories.UsuarioRepository;
import repositories.ValidadorRepository;

import java.util.List;
import java.util.Optional;

@With(TenantAction.class)
public class PerfilController extends ApplicationController {

    UsuarioRepository usuarioRepository;
    PerfilAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;

    @Inject
    public PerfilController(PlaySessionStore playSessionStore, UsuarioRepository usuarioRepository, PerfilAtualizarProcessador atualizarProcessador, ValidadorRepository validadorRepository) {
        super(playSessionStore);
        this.usuarioRepository = usuarioRepository;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
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

        ObjectJson.JsonBuilder<PerfilJson> builder = ObjectJson.build(PerfilJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);

        PerfilJson perfilJson = PerfilJson.of(usuario.get().getPerfil());
        builder.comEntidade(perfilJson);

        return ok(builder.build());

    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar() {
        Optional<CommonProfile> profile = getProfile();
        CommonProfile commonProfile = profile.get();

        JsonNode json = request().body().asJson();
        PerfilJson perfilJson = Json.fromJson(json.get("perfil"), PerfilJson.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), PerfilAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), Long.parseLong(commonProfile.getId()));
            atualizarProcessador.executar(chave, perfilJson.to(), validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        ObjectJson.JsonBuilder<PerfilJson> builder = ObjectJson.build(PerfilJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);

        PerfilJson perfilJ = PerfilJson.of(usuarioRepository.buscar(getTenant(), Long.parseLong(commonProfile.getId())).get().getPerfil());
        builder.comEntidade(perfilJ);

        return ok(builder.build());

    }

}
