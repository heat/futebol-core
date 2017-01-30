package api.rest;

import api.json.BilheteJson;
import api.json.ObjectJson;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import controllers.ApplicationController;
import dominio.processadores.PagarComissaoProcessador;
import dominio.processadores.bilhetes.BilheteAtualizarProcessador;
import dominio.processadores.bilhetes.BilheteInserirProcessador;
import dominio.validadores.Validador;
import dominio.validadores.exceptions.ValidadorExcpetion;
import models.apostas.Taxa;
import models.bilhetes.Bilhete;
import models.bilhetes.Palpite;
import models.seguranca.Usuario;
import models.vo.Chave;
import models.vo.Tenant;
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
import views.html.bilhete;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class BilheteController extends ApplicationController {

    BilheteRepository bilheteRepository;

    UsuarioRepository usuarioRepository;

    BilheteInserirProcessador inserirProcessador;
    BilheteAtualizarProcessador atualizarProcessador;
    PagarComissaoProcessador pagarComissaoProcessador;
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
    public Result inserir() {

        Bilhete bilhete = Json.fromJson(Controller.request()
                .body()
                .asJson(), Bilhete.class);

        List<Validador> validadores = validadorRepository.todos(getTenant(), BilheteInserirProcessador.REGRA);

        CommonProfile profile = getProfile().get();
        Optional<Usuario> usuarioOptional = usuarioRepository.buscar(getTenant(), Long.parseLong(profile.getId()));

        if (!usuarioOptional.isPresent())
            return notFound("Usuário não encontrado!");
        final Usuario usuario = usuarioOptional.get();

        bilhete.setUsuario(usuario);
        try {
            bilhete = inserirProcessador.executar(getTenant(), bilhete, validadores)
                    .thenCompose(b -> {
                        return pagarComissaoProcessador.executar(getTenant().get(), b, Collections.emptyList());
                    } ).get();
            ObjectJson.JsonBuilder<BilheteJson> builder = ObjectJson.build(BilheteJson.TIPO, ObjectJson.JsonBuilderPolicy.OBJECT);
            builder.comEntidade(BilheteJson.of(bilhete));

        } catch (ValidadorExcpetion validadorExcpetion) {
            return status(Http.Status.UNPROCESSABLE_ENTITY, validadorExcpetion.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return internalServerError(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return internalServerError(e.getMessage());
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


    @Transactional
    public Result imprimir(String codigo) {
        Bilhete bilhete = new Bilhete();
        bilhete.setCodigo("TESTE");
        bilhete.setSituacao(Bilhete.Situacao.A);
        bilhete.setAlteradoEm(Calendar.getInstance());
        bilhete.setCliente("TESTE CLIENTE");
        bilhete.setCriadoEm(Calendar.getInstance());
        bilhete.setPalpites(
                Lists.newArrayList(new Palpite(Tenant.SYSBET.get(),
                        new Taxa(),
                        BigDecimal.TEN,
                        Palpite.Status.A )));
        bilhete.setTenant(Tenant.SYSBET.get());
        bilhete.setUsuario(new Usuario());
        bilhete.setValorAposta(BigDecimal.TEN);
        bilhete.setValorPremio(BigDecimal.TEN.multiply(BigDecimal.TEN));
        return ok(views.txt.bilhetes.render(bilhete));
    }


}
