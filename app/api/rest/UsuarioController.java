package api.rest;

import controllers.ApplicationController;
import dominio.processadores.usuarios.UsuarioAtualizarProcessador;
import dominio.processadores.usuarios.UsuarioInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.seguranca.Usuario;
import models.vo.Chave;
import modules.SecurityModule;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.UsuarioRepository;
import repositories.ValidadorRepository;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class UsuarioController extends ApplicationController{

    UsuarioInserirProcessador usuarioInserirProcessador;
    UsuarioAtualizarProcessador usuarioAtualizarProcessador;
    UsuarioRepository usuarioRepository;
    ValidadorRepository validadorRepository;

    @Inject
    public UsuarioController( PlaySessionStore playSessionStore, UsuarioInserirProcessador usuarioInserirProcessador,
                              UsuarioAtualizarProcessador usuarioAtualizarProcessador,
                              UsuarioRepository usuarioRepository, ValidadorRepository validadorRepository
                              ) {

        super(playSessionStore);
        this.usuarioInserirProcessador = usuarioInserirProcessador;
        this.usuarioAtualizarProcessador = usuarioAtualizarProcessador;
        this.usuarioRepository = usuarioRepository;
        this.validadorRepository = validadorRepository;

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
        return ok("access_token : " + token);
    }

    @Secure(clients = "headerClient")
    public Result usuario() {
        Optional<CommonProfile> profile = getProfile();
        CommonProfile commonProfile = profile.get();

        return ok("teste" +
                commonProfile.getClass());
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir() {

        Usuario usuario = Json.fromJson(Controller.request()
                .body()
                .asJson(), Usuario.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), UsuarioInserirProcessador.REGRA);

        try {
            usuarioInserirProcessador.executar(getTenant(), usuario, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        return created(Json.toJson(usuario));
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {

        Usuario usuario = Json.fromJson(Controller.request()
                .body()
                .asJson(), Usuario.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), UsuarioInserirProcessador.REGRA);

        Chave chave = Chave.of(getTenant(), id);

        try {
            usuarioAtualizarProcessador.executar(chave, usuario, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        return created(Json.toJson(usuario));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result cancelar(Long id) {
        try {
            usuarioRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando cancela uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }


}
