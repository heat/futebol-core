package api.rest;

import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.bilhetes.BilheteAtualizarProcessador;
import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.bilhetes.Bilhete;
import models.seguranca.Usuario;
import models.vo.Chave;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.BilheteRepository;
import repositories.UsuarioRepository;
import repositories.ValidadorRepository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class BilheteController extends ApplicationController {

    BilheteRepository bilheteRepository;
    UsuarioRepository usuarioRepository;
    BilheteInserirProcessador inserirProcessador;
    BilheteAtualizarProcessador atualizarProcessador;
    ValidadorRepository validadorRepository;

    @Inject

    public BilheteController(PlaySessionStore playSessionStore, BilheteRepository bilheteRepository,
                             BilheteInserirProcessador inserirProcessador,
                             UsuarioRepository usuarioRepository, BilheteAtualizarProcessador atualizarProcessador, ValidadorRepository validadorRepository) {
        super(playSessionStore);
        this.bilheteRepository = bilheteRepository;
        this.usuarioRepository = usuarioRepository;
        this.inserirProcessador = inserirProcessador;
        this.atualizarProcessador = atualizarProcessador;
        this.validadorRepository = validadorRepository;
    }


    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result inserir(Long idUsuario) {

        Bilhete bilhete = Json.fromJson(Controller.request()
                .body()
                .asJson(), Bilhete.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), BilheteInserirProcessador.REGRA);

        CommonProfile profile = getProfile().get();
        Usuario usuario = usuarioRepository.buscar(getTenant(), profile.getId());

        bilhete.setUsuario(usuario);
        try {
            inserirProcessador.executar(getTenant(), bilhete, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }
        return created(Json.toJson(bilhete));
    }

    @Secure(clients = "headerClient")
    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result atualizar(Long id) {
        Bilhete bilhete = Json.fromJson(Controller.request()
                .body()
                .asJson(), Bilhete.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), BilheteAtualizarProcessador.REGRA);

        try {
            Chave chave = Chave.of(getTenant(), id);
            atualizarProcessador.executar(chave, bilhete, validadores);
        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        }

        return ok("Bilhete atualizado! ");
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result todos() {
        List todos = bilheteRepository.todos(getTenant());

        return ok(Json.toJson(todos));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result buscar(String codigo) {
        Optional<Bilhete> bilhete = bilheteRepository.buscar(getTenant(), codigo);

        if (!bilhete.isPresent()) {
            return notFound("Bilhete não encontrado!");
        }
        return ok(Json.toJson(bilhete));
    }

    @Secure(clients = "headerClient")
    @Transactional
    public Result cancelar(Long id) {
        try {
            bilheteRepository.excluir(getTenant(), id);
            return noContent(); // padrao para quando exclui uma entidade
        } catch (NoResultException e) {
            return notFound(e.getMessage());
        }
    }
    
    


}
